package com.example.rishabh;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;

/**
 * Created by lokiore on 9/4/18.
 */

public class ForgotPasswordActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
        //@BindView(com.example.rishabh.R.id.password_email) EditText emailPassword;


        Button sendEmail = findViewById(R.id.btn_password);
        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView _emailPassword = findViewById(R.id.password_email);
                FirebaseAuth auth = FirebaseAuth.getInstance();
                final String emailAddress = _emailPassword.getText().toString();
                auth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("TAG", "Email sent.");

                                    Toast.makeText(ForgotPasswordActivity.this, "Email Sent",
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ForgotPasswordActivity.this,LoginActivity.class);
                                    startActivity(intent);
                                }
                            }
                        });
            }
        });



    }
}
