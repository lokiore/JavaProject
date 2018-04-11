package com.example.rishabh;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import butterknife.BindView;
//import com.squareup.picasso.Picasso;

// globally

public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();

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
        //in your OnCreate() method

        final View v =  inflater.inflate(R.layout.fragment_profile, container, false);

        ImageView imageView = v.findViewById(R.id.profile_picture);
        final TextView username = v.findViewById(R.id.username);
        TextView email = v.findViewById(R.id.email);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
        FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();
         Uri mImageUri;
        final ImageView profile = v.findViewById(R.id.profile_picture);
        if (acct != null)
        {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            username.setText(personName);
            email.setText(personEmail);
            Picasso.get().load(personPhoto).into(profile);
        }
        else
        {
            if(mAuth!=null) {
                String new_email = mAuth.getEmail();
                new_email = new_email.replaceAll("\\.", "_dot_");
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference mDatabase = database.getReference();
                DatabaseReference myRef = mDatabase.child("users");
                DatabaseReference mUser = myRef.child(new_email);
                mUser.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        HashMap<String, String> user;
                        user = (HashMap<String, String>) dataSnapshot.getValue();
                        String personName = user.get("Name");
                        String personEmail = user.get("Email");
                        TextView username = v.findViewById(R.id.username);
                        TextView email = v.findViewById(R.id.email);
                        username.setText(personName);
                        email.setText(personEmail);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                //final ImageView profile = v.findViewById(R.id.profile_picture);
                //personEmail=personEmail.replaceAll("\\.", "_dot_");
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                DatabaseReference profRef = databaseReference.child("Photos").child(new_email).child("profile");
                if(profRef!=null) {
                    profRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //DataSnapshot profileSnapshot = dataSnapshot.getChildren();
                            //for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            if(dataSnapshot.getValue()!=null) {
                                Upload upload = dataSnapshot.getValue(Upload.class);
                                Picasso.get().load(upload.getImageUrl()).into(profile);
                                //mImageUri = Uri.parse(upload.getImageUrl());
                            }
                            else{
                                profile.setImageDrawable(getResources().getDrawable(R.drawable.no_profile));
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else {
                    Log.v("TAG","YAHOO");
                    ImageView profilee = v.findViewById(R.id.profile_picture);
                    profilee.setImageDrawable(getResources().getDrawable(R.drawable.no_profile));
                }
            }
            else
            {
                Log.v("TAG","Error Signing in");
            }

        }
        LinearLayout myProfile = v.findViewById(R.id.my_profile);
        myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(),DispProfileActivity.class);
                startActivity(intent);
            }
        });
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.photo);
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(),bitmap);
        roundedBitmapDrawable.setCircular(true);
        imageView.setImageDrawable(roundedBitmapDrawable);

        TextView signoutView = v.findViewById(R.id.sign_out);
        signoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                if(mAuth!=null){
                    mAuth.signOut();

                    //finish();
                }
                if(acct!=null){
                    GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), LoginActivity.gso);
                    mGoogleSignInClient.signOut().addOnCompleteListener(getActivity(),
                            new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    //updateUI1(null);
                                }
                            });
                }
                Intent intent = new Intent(getActivity(),LoginActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout myPhotos = v.findViewById(R.id.layout_photo);
        myPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),MyPhotos.class);
                startActivity(intent);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),DetailsActivity.class);
                ImageView imageView1 = v.findViewById(R.id.profile_picture);
                int[] screenLocation = new int[2];
                imageView1.getLocationOnScreen(screenLocation);

                //Pass the image title and url to DetailsActivity
                intent.putExtra("left", screenLocation[0]).
                        putExtra("top", screenLocation[1]).
                        putExtra("width", imageView1.getWidth()).
                        putExtra("height", imageView1.getHeight()).
                        putExtra("title", username.getText()).
                        putExtra("image", "Profile Picture");

                //Start details activity
                startActivity(intent);
            }
        });

        v.findViewById(R.id.myPost).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),MyPost.class);
                startActivity(intent);
            }
        });






        return v;
    }

}
