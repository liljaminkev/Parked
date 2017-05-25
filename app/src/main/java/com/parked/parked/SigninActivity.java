package com.parked.parked;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SigninActivity extends AppCompatActivity {
    // Adview
    private AdView mAdView;

    // Firebase auth client and listener
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    // TAGS
    private static final String TAG = "LoginActivity";

    // Text input
    private EditText mEmailField;
    private EditText mPasswordField;

    // Buttons
    private Button signInButton;
    private Button registerButton;

    // Progress dialog
    public ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set view to activity signin
        setContentView(R.layout.activity_signin);

        // View assets
        mEmailField = (EditText) findViewById(R.id.field_email);
        mPasswordField = (EditText) findViewById(R.id.field_password);


        // initialize advertisements
        MobileAds.initialize(this, "ca-app-pub-6935643869644905~2786908477");

        //authenticator
        mAuth = FirebaseAuth.getInstance();


        // Buttons
        signInButton = (Button) findViewById(R.id.sign_in_button);

        signInButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
            }
        });

        registerButton = (Button) findViewById(R.id.create_account_button);


        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
            }
        });

        //get an ad from admob
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        //listen for sign in or sign out
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };



    }

    @Override
    public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }

    @Override
    public void onStop(){
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    public void createAccount(String email, String password){
        Log.d(TAG, "createAccount:" + email);
        if(!isFormValid())
            return;

        showProgressDialog();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete (@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SigninActivity.this, "User is already Registered.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        hideProgressDialog();
                    }
                });
    }

    public void signIn(String email, String password){
        Log.d(TAG, "signIn:" + email);

        if(!isFormValid()){
            return;
        }

        showProgressDialog();

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "signInWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    Intent intent = new Intent(SigninActivity.this, NavActivity.class);
                    startActivity(intent);

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                    Toast.makeText(SigninActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }

                hideProgressDialog();

            }
        });

    }

    public boolean isFormValid(){
        boolean valid = true;

        String email = mEmailField.getText().toString();

        if(TextUtils.isEmpty(email)){
            mEmailField.setError("Required.");
            valid = false;
        }
        else{
            mEmailField.setError(null);
        }
        //check that email add is not empty
        String password = mPasswordField.getText().toString();
        if(TextUtils.isEmpty(password)){
            mEmailField.setError("Required.");
            valid = false;
        }
        else{
            mEmailField.setError(null);
        }

        return valid;
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }



}
