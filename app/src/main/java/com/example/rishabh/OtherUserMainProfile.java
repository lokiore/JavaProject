package com.example.rishabh;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class OtherUserMainProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_user_main_profile);

        Intent intent = getIntent();
        final String pEmail = intent.getStringExtra("Email");

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        DatabaseReference reference = databaseReference.child("Photos").child(pEmail).child("profile");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Upload upload = dataSnapshot.getValue(Upload.class);
                ImageView imageView = findViewById(R.id.other_profile_picture);
                Picasso.get().load(Uri.parse(upload.getImageUrl())).into(imageView);
                TextView textView=findViewById(R.id.other_username);
                textView.setText(upload.getName());
                TextView textView1 = findViewById(R.id.other_email);
                String Email=pEmail.replaceAll("_dot_","\\.");
                textView1.setText(Email);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}


