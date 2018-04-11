package com.example.rishabh;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;



public class NewsFeedFragment extends Fragment {

    static String pName = "Google";
    static String pTime="";
    static String pDesc="";
    static Uri pProfile=null;
    static Uri pImage=null;
    static String pEmail="";
    static ArrayList<Timeline> posts = new ArrayList<Timeline>();

    public NewsFeedFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static NewsFeedFragment newInstance(String param1, String param2) {
        NewsFeedFragment fragment = new NewsFeedFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView=inflater.inflate(R.layout.time_list, container, false);
        FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
        String personEmail;
        if(acct!=null){
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            //pName=personName;
            pProfile=personPhoto;
            //pEmail=personEmail;

        }
        else{
            if(mAuth!=null) {
                String new_email = mAuth.getEmail();
                new_email = new_email.replaceAll("\\.", "_dot_");
                //pEmail=new_email;
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference mDatabase = database.getReference();
                DatabaseReference myRef = mDatabase.child("users");
                DatabaseReference mUser = myRef.child(new_email);
                mUser.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        HashMap<String, String> user;
                        user = (HashMap<String, String>) dataSnapshot.getValue();
                        String personName = user.get("Name");
                        String personEmail = user.get("Email");
                        String personMobile = user.get("Mobile");
                        String personPassword = user.get("Password");
                        //pName=personName;

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                //final ImageView profile = findViewById(R.id.disp_profile_picture);
                //personEmail=personEmail.replaceAll("\\.", "_dot_");
            }
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference profRef = databaseReference.child("Posts");
        if(profRef!=null) {
            profRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //DataSnapshot profileSnapshot = dataSnapshot.getChildren();
                    posts.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        //DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference();
                        //DatabaseReference prfRef = databaseReference1.child("Photos");
                        //Log.v("EMAIL1",postSnapshot.getKey());
                        for(DataSnapshot postPostSnapshot : postSnapshot.getChildren()) {
                            Upload upload = postPostSnapshot.getValue(Upload.class);
                            //Log.v("TAA",postSnapshot.getKey());
                            //Log.v("TAA",dataSnapshot.getKey());
                            pImage = Uri.parse(upload.getImageUrl());
                            pDesc = upload.getName();
                            pTime = postPostSnapshot.getKey();
                            String email = postSnapshot.getKey();
                            //Log.v("EMAIL",email);
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                            DatabaseReference profRef = databaseReference.child("Photos").child(email).child("profile");
                            if(profRef!=null) {
                                profRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        //DataSnapshot profileSnapshot = dataSnapshot.getChildren();
                                        //for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                        if(dataSnapshot.getValue()!=null) {
                                            Upload upload = dataSnapshot.getValue(Upload.class);
                                            pProfile=Uri.parse(upload.getImageUrl());
                                            pName = upload.getName();
                                            Log.v("EMAILNAME",pName);
                                            //Picasso.get().load(upload.getImageUrl()).into(profile);
                                        }
                                        else{
                                            //profile.setImageDrawable(getResources().getDrawable(R.drawable.no_profile));
                                        }

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                            else {
                            }
                            Log.v("EMAIL","YAHOO"+email+" "+pName);
                            if (pImage.equals("")) {
                                posts.add(new Timeline(1, pName, pDesc, pProfile, pTime, "lk"));
                            } else {
                                posts.add(new Timeline(1, pName, pImage, pDesc, pProfile, pTime, "lk"));
                            }
                        }
                        //Picasso.get().load(upload.getImageUrl()).into(profile);
                    }

                    Collections.sort(posts,new SortArrayList());
                    TimelineAdapter postsAdapter = new TimelineAdapter(getActivity(), posts);

                    ListView listView = rootView.findViewById(R.id.list);
                    listView.setAdapter(postsAdapter);


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else{
            Toast.makeText(getActivity(),"Make Your First Post",Toast.LENGTH_LONG).show();
        }
    return rootView;
    }



}
