package com.example.rishabh;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.text.style.UpdateAppearance;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import java.util.HashMap;

public class OtherUserMyProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_user_my_profile);

        final ImageView profile = findViewById(R.id.other_disp_profile_picture);

                Intent intent = getIntent();
                String new_email = intent.getStringExtra("Email");
                //new_email = new_email.replaceAll("\\.", "_dot_");
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
                        TextView username = findViewById(R.id.other_profile_name);
                        TextView email = findViewById(R.id.other_profile_email);
                        TextView mobile = findViewById(R.id.other_profile_mobile);
                        //TextView password = findViewById(R.id.profile_pass);

                        username.setText(personName);
                        email.setText(personEmail);
                        mobile.setText(personMobile);
                        //personPassword = personPassword.replaceAll(".","*");
                        //password.setText(personPassword);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

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
                    ImageView profile2 = findViewById(R.id.other_disp_profile_picture);
                    profile2.setImageDrawable(getResources().getDrawable(R.drawable.no_profile));
                }
            }
        }



