package com.example.rishabh;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by lokiore on 11/4/18.
 */

class SortArrayList implements Comparator<Timeline>{
    public int compare(Timeline a, Timeline b){
        if(a.getTimeStamp().compareTo(b.getTimeStamp())<0){
            return 1;
        }
        else{
            return -1;
        }
    }
}



public class MyPost extends AppCompatActivity {
    static String pName = "";
    static String pTime="";
    static String pDesc="";
    static Uri pProfile=null;
    static Uri pImage=null;
    static String pEmail="";
    static ArrayList<Timeline> posts = new ArrayList<Timeline>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypost);

        FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        String personEmail;
        if(acct!=null){
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            pName=personName;
            pProfile=personPhoto;
            pEmail=personEmail;

        }
        else{
            if(mAuth!=null) {
                String new_email = mAuth.getEmail();
                new_email = new_email.replaceAll("\\.", "_dot_");
                pEmail=new_email;
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
                        pName=personName;

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                //final ImageView profile = findViewById(R.id.disp_profile_picture);
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
                                pProfile=Uri.parse(upload.getImageUrl());
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
                    Log.v("TAG","YAHOO");
                }
            }
        }




        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Log.v("TAA",pEmail);
        pEmail=pEmail.replaceAll("\\.","_dot_");
        Log.v("TAA","LLKLKLKLJ");
        Log.v("TAA",pEmail+"ll");
        Log.v("TAA","LOLOLO");
        DatabaseReference profRef = databaseReference.child("Posts").child(pEmail);
        if(profRef!=null) {
            profRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //DataSnapshot profileSnapshot = dataSnapshot.getChildren();
                    posts.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Upload upload = postSnapshot.getValue(Upload.class);
                        //Log.v("TAA",postSnapshot.getKey());
                        //Log.v("TAA",dataSnapshot.getKey());
                        pImage = Uri.parse(upload.getImageUrl());
                        pDesc = upload.getName();
                        pTime = postSnapshot.getKey();
                        if(pImage.equals("")) {
                            posts.add(new Timeline(1, pName, pDesc, pProfile, pTime, "lk"));
                        }
                        else {
                            posts.add(new Timeline(1, pName, pImage, pDesc, pProfile, pTime, "lk"));
                        }
                        //Picasso.get().load(upload.getImageUrl()).into(profile);
                    }
                    Collections.sort(posts,new SortArrayList());
                    MyTimelineAdapter postsAdapter = new MyTimelineAdapter(MyPost.this, posts);

                    ListView listView = findViewById(R.id.list_view);
                    listView.setAdapter(postsAdapter);


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else{
            Toast.makeText(MyPost.this,"Make Your First Post",Toast.LENGTH_LONG).show();
        }






        findViewById(R.id.new_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPost.this,NewPostActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //egisterForContextMenu(findViewById(R.id.post_menu));
    }
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        findViewById(R.id.action_settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return super.onOptionsItemSelected(item);
    }*/

}
