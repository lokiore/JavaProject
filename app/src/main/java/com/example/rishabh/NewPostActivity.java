package com.example.rishabh;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import id.zelory.compressor.Compressor;

public class NewPostActivity extends AppCompatActivity {

    private ImageView newPostImage;
    private EditText newPostDesc;
    private Button newPostBtn;

    private Uri postImageUri = null;
    //private static final String TAG = MyPhotos.class.getSimpleName();

    //public static final int PICK_IMAGE_REQUEST = 1;


   // private Uri mImageUri;

    private ProgressBar newPostProgress;

    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private String current_user_id;

    private Bitmap compressedImageFile;
    private static final String TAG = MyPhotos.class.getSimpleName();

    public static final int PICK_IMAGE_REQUEST = 1;


    private Uri mImageUri;

    private StorageReference mStorageRef;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;
    private ImageView imageView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        current_user_id = firebaseAuth.getCurrentUser().getUid();

        //newPostToolbar = findViewById(R.id.new_post_toolbar);
        //setSupportActionBar(newPostToolbar);
        //getSupportActionBar().setTitle("Add New Post");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        newPostImage = findViewById(R.id.new_post_image);
        newPostDesc = findViewById(R.id.new_post_desc);
        newPostBtn = findViewById(R.id.post_btn);
        newPostProgress = findViewById(R.id.new_post_progress);




        newPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
                postImageUri=mImageUri;

            }
        });
        final ImageView profile = findViewById(R.id.new_post_image);
        final FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();
        // FirebaseAuth mauth = FirebaseAuth.getInstance();

        String new_email = mAuth.getEmail();
        new_email = new_email.replaceAll("\\.", "_dot_");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        timestamp = timestamp.replaceAll("\\.",":");
        Log.v("TAAG",timestamp);
        mStorageRef = FirebaseStorage.getInstance().getReference(new_email).child("Posts").child(timestamp);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Posts").child(new_email).child(timestamp);
        Log.v("TAG","SID");
        DatabaseReference profRef = databaseReference.child("Posts").child(new_email).child("lolo");
        Log.v("TAG","RISH");
        if(profRef!=null) {
            Log.v("TAG","LOK");
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
            ImageView profilee = findViewById(R.id.new_post_image);
            profilee.setImageDrawable(getResources().getDrawable(R.drawable.no_profile));
        }

        newPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
                Intent intent = new Intent(NewPostActivity.this,MyPost.class);
                startActivity(intent);

            }
        });


    }
    private void openFileChooser(){

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
        //Log.v("TAG","IMAGE"+mImageUri.toString());

    }
    private  String getFileExtention(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile(){
        Log.v("TAG",mImageUri.toString());
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
                    Toast.makeText(NewPostActivity.this,"Upload successful",Toast.LENGTH_LONG).show();
                    TextView textView = findViewById(R.id.new_post_desc);
                    String desc = textView.getText().toString();
                    Log.v("TAG",desc);
                    Upload upload = new Upload(desc,taskSnapshot.getDownloadUrl().toString());
                    Log.v("TAG",desc);
                    //String uploadId = mDatabaseRef.getKey();
                    mDatabaseRef.setValue(upload);
                    Log.v("TAG",desc);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(NewPostActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            mImageUri = data.getData();
            Log.v("TAG","IMAGE"+mImageUri.toString());

            Picasso.get().load(mImageUri).into(newPostImage);
            Log.v("TAG","IMAGE"+mImageUri.toString());

        }

    }
}




