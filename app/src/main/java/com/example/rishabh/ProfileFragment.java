package com.example.rishabh;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        TextView clickMyProf = v.findViewById(R.id.click_my_prof);

        clickMyProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(),DispProfileActivity.class);
                startActivity(intent);
            }
        });
        ImageView imageView = v.findViewById(R.id.profile_picture);
        TextView username = v.findViewById(R.id.username);
        TextView email = v.findViewById(R.id.email);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
        FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();
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
            }
            else
            {
                Log.v("TAG","Error Signing in");
            }

        }
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.photo);
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(),bitmap);
        roundedBitmapDrawable.setCircular(true);
        imageView.setImageDrawable(roundedBitmapDrawable);
        return v;
    }

}
