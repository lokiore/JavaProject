package com.example.rishabh;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import java.lang.Exception;
import java.util.HashMap;

import android.content.Intent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private StorageTask mUploadTask;
    private StorageReference mStorageRef;
    //private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRef;
    private Uri mImageUri;
    static String new_email;

    public static GoogleSignInOptions gso;
    @BindView(com.example.rishabh.R.id.input_email) EditText _emailText;
    @BindView(com.example.rishabh.R.id.input_password) EditText _passwordText;
    @BindView(com.example.rishabh.R.id.btn_login) Button _loginButton;
    @BindView(com.example.rishabh.R.id.link_signup) TextView _signupLink;
    @BindView(R.id.link_forgot_password) TextView _forgotPasswordLink;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabase = database.getReference();
    SignInButton signInButton;
    String account_name;
    private final static int RC_SIGN_IN = 9114;
    //    GoogleSignInClient mGoogleSignInClient;
    GoogleApiClient mGoogleApiClient;
    FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.rishabh.R.layout.activity_login);

        ButterKnife.bind(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), com.example.rishabh.SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(com.example.rishabh.R.anim.push_left_in, com.example.rishabh.R.anim.push_left_out);
            }
        });

        _forgotPasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        //mAuth = FirebaseAuth.getInstance();
         gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        setGooglePlusButtonText(signInButton, "Sign in with MNIT account");
    }

    @Override
    protected void onStart() {
        super.onStart();
//        mAuth.addAuthStateListener(mAuthListener);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI1(currentUser);

    }


    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                com.example.rishabh.R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.v("Log","user");
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            try{
                                throw task.getException();
                            }catch (FirebaseAuthInvalidUserException invalidEmail)
                            {
                                Log.d(TAG, "onComplete: invalid_email");
                                Toast.makeText(LoginActivity.this,"Email Not Registered",Toast.LENGTH_SHORT).show();

                            }
                            catch (FirebaseAuthInvalidCredentialsException wrongPassword)
                            {
                                Log.d(TAG, "onComplete: wrong_password");
                                Toast.makeText(LoginActivity.this, "Invalid Password",
                                        Toast.LENGTH_SHORT).show();

                            }
                            catch (Exception e)
                            {
                                Log.d(TAG, "onComplete: " + e.getMessage());
                                Toast.makeText(LoginActivity.this, "Login Failed",
                                        Toast.LENGTH_SHORT).show();
                            }
                            updateUI(null);

                        }

                        // ...
                    }
                });

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    private void signIn() {
//        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        Log.v("TAG","AAYA");
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//        Intent signInIntent = AccountPicker.newChooseAccountIntent(null, null, new String[]{"com.google"},
//                false, null, null, null, null);
        Log.v("TAG", "sign in intent"+signInIntent.toString());
        startActivityForResult(signInIntent, RC_SIGN_IN);
        Log.v("TAG","YAHANB BHI AAYA");

    }

    private void updateUI(FirebaseUser user) {
        //hideProgressDialog();
        if (user != null) {
            if(user.isEmailVerified()) {
                Log.v("Log", "Login");
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
            }
            else{
                Log.v("TAG","NOT VERIFIED");
                Toast.makeText(LoginActivity.this, "Email not verified",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e);
                // [START_EXCLUDE]
                updateUI1(null);
                // [END_EXCLUDE]
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
//        showProgressDialog();
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI1(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Auth failed", Toast.LENGTH_SHORT).show();
//                            Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI1(null);
                        }

                        // ...
                    }
                });

    }

    public void signOut(){
        mAuth.signOut();
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI1(null);
                    }
                });
//        finish();
    }
    public void updateUI1(FirebaseUser currentUser) {
        if (currentUser != null) {
            boolean access = false;
            String msg = "";
            if (currentUser.getEmail().endsWith("@mnit.ac.in")) {
                access = true;
                msg = "User: " + currentUser.getDisplayName() + "\nEmail: " + currentUser.getEmail()
                        + "\nAccess: Granted";
            } else
                msg = "User: " + currentUser.getDisplayName() + "\nEmail: " + currentUser.getEmail()
                        + "\nAccess: Denied";

            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            if (access) {
                GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
                if (acct != null) {
                    String personName = acct.getDisplayName();
                    String personGivenName = acct.getGivenName();
                    String personFamilyName = acct.getFamilyName();
                    String personEmail = acct.getEmail();
                    String personId = acct.getId();
                    Uri personPhoto = acct.getPhotoUrl();
                    Log.v("TAG",personEmail );
                    DatabaseReference myRef = mDatabase.child("users");
                    myRef.push().setValue("User");
                    myRef = mDatabase.child("users");
                    new_email = personEmail.replaceAll("\\.","_dot_");
                    Log.v("TAG",new_email);
                    DatabaseReference mUser = myRef.child(new_email);
                    Log.v("Log","Hoja1");
                    HashMap<String,String> user = new HashMap<String, String>();
                    user.put("Name",personName);
                    user.put("Email",new_email);
                    user.put("Mobile","Mobile");
                    user.put("Password","Google Password");
                    mUser.setValue(user);

                    mImageUri=personPhoto;
                    uploadFile();

                }

                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
            else {
                //signOut();
                signOut();
            }
        }
        else {
            //Toast.makeText(this, "Sign in your account", Toast.LENGTH_SHORT).show();
        }

    }
    private  String getFileExtention(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    private void uploadFile(){
//        Log.v("TAG",mImageUri.toString());
        mStorageRef = FirebaseStorage.getInstance().getReference(new_email).child("profile");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Photos").child(new_email).child("profile");
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
                    Toast.makeText(LoginActivity.this,"Upload successful",Toast.LENGTH_LONG).show();
                    TextView tname = findViewById(R.id.update_name);
                    String name = tname.getText().toString();
                    Upload upload = new Upload(name,taskSnapshot.getDownloadUrl().toString());
                    //String uploadId = mDatabaseRef.getKey();
                    mDatabaseRef.setValue(upload);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.v("GMAIL",e.getMessage());
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
    public void onBackPressed() {
        // Disable going back to the MainActivity1
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
    protected void setGooglePlusButtonText(SignInButton signInButton, String buttonText) {
        // Find the TextView that is inside of the SignInButton and set its text
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText(buttonText);
                return;
            }
        }
    }
}
