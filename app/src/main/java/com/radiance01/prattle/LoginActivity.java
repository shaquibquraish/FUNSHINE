package com.radiance01.prattle;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

import maes.tech.intentanim.CustomIntent;

public class LoginActivity extends AppCompatActivity {
    Button login_button;
    TextView signup;
    Button google_signup_button;

    EditText email;
    EditText password;

    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth auth;

    ProgressBar progressBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);

        findViewById(R.id.main_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                if(inputMethodManager != null)
                {
                    if(inputMethodManager.isActive())
                    {
                        inputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(),0);
                    }
                }
            }
        });

        progressBar = findViewById(R.id.progressBar);
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        auth = FirebaseAuth.getInstance();

        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
        login_button = findViewById(R.id.login_button);
        signup = findViewById(R.id.signup);
        google_signup_button = findViewById(R.id.google_signup);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                if(inputMethodManager != null)
                {
                    if(inputMethodManager.isActive())
                    {
                        inputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(),0);
                    }
                }

                if(email.getText().toString().isEmpty())
                {
                    email.setError("Email is required");
                    email.requestFocus();
                    return;
                }

                else if(password.getText().toString().isEmpty())
                {
                    password.setError("Password is required");
                    password.requestFocus();
                    return;
                }
                else
                {
                    progressBar.setVisibility(View.VISIBLE);
                    auth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        FirebaseUser user = auth.getCurrentUser();
                                        progressBar.setVisibility(View.GONE);
                                        Snackbar.make(findViewById(R.id.main_layout),"Login Successful!",Snackbar.LENGTH_SHORT).show();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        progressBar.setVisibility(View.GONE);
                                        Snackbar.make(findViewById(R.id.main_layout),"Invalid EmailId or Password!",Snackbar.LENGTH_SHORT).show();

                                    }
                                }
                            });
                }
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SignupActivity.class);
                startActivity(intent);
                CustomIntent.customType(LoginActivity.this,"bottom-to-up");
            }
        });
        google_signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 101);
            }
        });
    }

    public void firebaseAuthWithGoogle(GoogleSignInAccount account)
    {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            if(auth.getCurrentUser() != null)
                            {
                                Snackbar.make(findViewById(R.id.main_layout), "Already Logged in!", Snackbar.LENGTH_SHORT).show();
                            }
                            else {
                                FirebaseUser user = auth.getCurrentUser();
                                Snackbar.make(findViewById(R.id.main_layout), "Login Successfull.", Snackbar.LENGTH_SHORT).show();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 101) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Snackbar.make(findViewById(R.id.main_layout), "Google sign in failed!", Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

}
