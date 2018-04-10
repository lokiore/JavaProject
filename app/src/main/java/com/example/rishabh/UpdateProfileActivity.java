package com.example.rishabh;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.text.style.UpdateAppearance;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class UpdateProfileActivity extends AppCompatActivity {




    private static final String TAG = MyPhotos.class.getSimpleName();

    public static final int PICK_IMAGE_REQUEST = 1;


    private Uri mImageUri;

    private StorageReference mStorageRef;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;
    private ImageView imageView ;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();
       // FirebaseAuth mauth = FirebaseAuth.getInstance();

        String new_email = mAuth.getEmail();
        new_email = new_email.replaceAll("\\.", "_dot_");
        mStorageRef = FirebaseStorage.getInstance().getReference(new_email).child("profile");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Photos").child(new_email).child("profile");
        setContentView(R.layout.activity_update_profile);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference mDatabase = database.getReference();
        final TextView email = findViewById(R.id.update_email);
        final TextView name = findViewById(R.id.update_name);
        final TextView mobile = findViewById(R.id.update_mobile);
        final TextView college = findViewById(R.id.update_college);
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
                personEmail = personEmail.replaceAll("_dot_", "\\.");

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
                        String new_email = email.replaceAll("\\.", "_dot_");
                        String mobile = tmobile.getText().toString();
                        String name = tname.getText().toString();

                        DatabaseReference myRef = mDatabase.child("users");
                        DatabaseReference mUser = myRef.child(new_email);

                        HashMap<String, String> user = new HashMap<String, String>();
                        user.put("Name", name);
                        user.put("Email", email);
                        user.put("Mobile", mobile);
                        user.put("Password", password);
                        mUser.setValue(user);
                        uploadFile();
                        Intent intent = new Intent(UpdateProfileActivity.this, DispProfileActivity.class);
                        startActivity(intent);
                        finish();
                    }

                });

                LinearLayout change_pass = findViewById(R.id.change_pass);
                change_pass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Intent intent  = new Intent(UpdateProfileActivity.this,ChangePasswordActivity.class);
                        //startActivity(intent);
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        String emailAddress = mAuth.getEmail();
                        auth.sendPasswordResetEmail(emailAddress)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d("TAG", "Email sent.");
                                            Toast.makeText(UpdateProfileActivity.this,"Password reset link to your EmailId ",Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });
                        //finish();
                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        final ImageView profile = findViewById(R.id.disp_profile_picture);
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
            ImageView profilee = findViewById(R.id.disp_profile_picture);
            profilee.setImageDrawable(getResources().getDrawable(R.drawable.no_profile));
        }
        ImageView imageView1 = findViewById(R.id.disp_profile_picture);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
        imageView = findViewById(R.id.disp_profile_picture);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.photo);
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        roundedBitmapDrawable.setCircular(true);
        imageView.setImageDrawable(roundedBitmapDrawable);

    }
    private  String getFileExtention(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile(){
//        Log.v("TAG",mImageUri.toString());
        if(mImageUri != null){
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()+"."+getFileExtention(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //mProgressBar.setProgress(0);
                        }
                    },500);
                    Toast.makeText(UpdateProfileActivity.this,"Upload successful",Toast.LENGTH_LONG).show();
                    Upload upload = new Upload("Pic",taskSnapshot.getDownloadUrl().toString());
                    //String uploadId = mDatabaseRef.getKey();
                    mDatabaseRef.setValue(upload);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UpdateProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0*taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    //mProgressBar.setProgress((int) progress);

                }
            });

            //mImageUri = null;

        }else{
            //Toast.makeText(this,"No file selected",Toast.LENGTH_SHORT).show();
        }
    }

    private void openFileChooser(){

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
        //Log.v("TAG","IMAGE"+mImageUri.toString());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            mImageUri = data.getData();
            Log.v("TAG","IMAGE"+mImageUri.toString());

            Picasso.get().load(mImageUri).into(imageView);
            Log.v("TAG","IMAGE"+mImageUri.toString());

        }

    }
}