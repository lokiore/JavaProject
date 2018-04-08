package com.example.rishabh;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.text.style.UpdateAppearance;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.HashMap;

public class UpdateProfileActivity extends AppCompatActivity {


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabase = database.getReference();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        ImageView imageView = findViewById(R.id.disp_profile_picture);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.photo);
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(),bitmap);
        roundedBitmapDrawable.setCircular(true);
        imageView.setImageDrawable(roundedBitmapDrawable);
        final TextView email = findViewById(R.id.update_email);
        final TextView name = findViewById(R.id.update_name);
        final TextView mobile = findViewById(R.id.update_mobile);
        final TextView college = findViewById(R.id.update_college);
        String new_email = mAuth.getEmail();
        new_email = new_email.replaceAll("\\.", "_dot_");
        DatabaseReference myRef = mDatabase.child("users");
        DatabaseReference mUser = myRef.child(new_email);

        mUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, String> user;
                user = (HashMap<String, String>) dataSnapshot.getValue();
                String personName = user.get("Name");
                String personEmail = user.get("Email");
                final String password = user.get("Password");
                personEmail = personEmail.replaceAll("_dot_","\\.");
                String personMobile = user.get("Mobile");
                name.setText(personName);
                email.setText(personEmail);
                mobile.setText(personMobile);
                college.setText("MNIT Jaipur");

                LinearLayout update_done = findViewById(R.id.update_done);
                update_done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TextView temail = findViewById(R.id.update_email);
                        TextView tname = findViewById(R.id.update_name);
                        TextView tmobile = findViewById(R.id.update_mobile);
                        String email = temail.getText().toString();
                        String new_email = email.replaceAll("\\.","_dot_");
                        String mobile = tmobile.getText().toString();
                        String name = tname.getText().toString();

                        DatabaseReference myRef = mDatabase.child("users");
                        DatabaseReference mUser = myRef.child(new_email);

                        HashMap<String,String> user = new HashMap<String, String>();
                        user.put("Name",name);
                        user.put("Email",email);
                        user.put("Mobile",mobile);
                        user.put("Password",password);
                        mUser.setValue(user);
                        Intent intent = new Intent(UpdateProfileActivity.this,DispProfileActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

}
