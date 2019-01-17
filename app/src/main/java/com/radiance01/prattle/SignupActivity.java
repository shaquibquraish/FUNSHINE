package com.radiance01.prattle;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import maes.tech.intentanim.CustomIntent;

public class SignupActivity extends AppCompatActivity{

    EditText text_email;
    EditText text_password;
    EditText repassword;
    EditText firstname;
    EditText lastname;
    FirebaseAuth mAuth;

    String emails;
    String password;
    Button signup;
    ConstraintLayout layout;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_main);

        layout = findViewById(R.id.layout);

        mAuth = FirebaseAuth.getInstance();
        text_email = findViewById(R.id.email);
        text_password = findViewById(R.id.password);
        repassword = findViewById(R.id.repassword);
        firstname = findViewById(R.id.first_name);
        lastname = findViewById(R.id.last_name);

        signup = findViewById(R.id.signup_button);

        layout.setOnClickListener(new View.OnClickListener() {
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
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firstname.getText().toString().isEmpty())
                {
                    firstname.setError("FirstName is required");
                    firstname.requestFocus();
                    return;
                }
                else if(lastname.getText().toString().isEmpty())
                {
                    lastname.setError("LastName is required");
                    lastname.requestFocus();
                    return;
                }
                else if(text_email.getText().toString().isEmpty())
                {
                    text_email.setError("Email is required");
                    text_email.requestFocus();
                    return;
                }
                else if(text_password.getText().toString().isEmpty())
                {
                    text_password.setError("Password is required");
                    text_password.requestFocus();
                    return;
                }
                else if(repassword.getText().toString().isEmpty())
                {
                    repassword.setError("Re-entering password is required");
                    repassword.requestFocus();
                    return;
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(text_email.getText().toString().trim()).matches())
                {
                    text_email.setError("Invalid Email");
                    text_email.requestFocus();
                    return;
                }
                else if(text_password.length() < 6)
                {
                    text_password.setError("Password cannot be less than 5 characters");
                    text_password.requestFocus();
                    return;
                }
                else if(!repassword.getText().toString().matches(text_password.getText().toString()))
                {
                    repassword.setError("Re-entered Password doesnot match");
                    repassword.requestFocus();
                    return;
                }
                 else {
                    dialog = new ProgressDialog(SignupActivity.this);
                    dialog.setMessage("Please wait.");
                    dialog.setTitle("Signing Up");
                    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    dialog.setIndeterminate(true);
                    dialog.show();
                    mAuth.createUserWithEmailAndPassword(text_email.getText().toString(), text_password.getText().toString())

                            .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Snackbar.make(findViewById(R.id.coordinator_layout), "Signup Successfull!", Snackbar.LENGTH_SHORT).show();
                                        dialog.dismiss();

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Snackbar.make(findViewById(R.id.coordinator_layout), "Email is already registered!", Snackbar.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                }
                            });

                }
            }
        });

    }

    /*@Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Snackbar.make(findViewById(R.id.main_layout),"User is alredy registered!",Snackbar.LENGTH_SHORT);
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(intent);
        CustomIntent.customType(this,"up-to-bottom");
    }
}
