package com.example.rishabh;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by lokiore on 7/4/18.
 */

public class NewsFeed extends AppCompatActivity{
    private GoogleSignInClient mGoogleSignInClient;
    final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_feed);

        //mGoogleSignInClient = GoogleSignIn.getClient(this);

        Button button =  findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
    }

    private void signOut() {
        try {
            mGoogleSignInClient.signOut()
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.v("TAG","Google");
                            Intent intent = new Intent(NewsFeed.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
        }catch (Exception e){
            Log.v("TAG","SI<PLE");
            mAuth.signOut();
            Intent intent = new Intent(NewsFeed.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

}
