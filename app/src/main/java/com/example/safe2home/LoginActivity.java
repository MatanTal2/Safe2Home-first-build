package com.example.safe2home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    //views
    TextView mNotHaveAccountTv;
    Button mLoginBtn;
    EditText mEmailEt, mPasswordEt;

    //Declare an instance of FirebaseAuth
    private FirebaseAuth mAuth;

    //progress dialog
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //ActionBar and it's title
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Login");
        //enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //init
        mEmailEt = findViewById(R.id.login_email_ET);
        mPasswordEt = findViewById(R.id.login_password_Et);
        mLoginBtn = findViewById(R.id.login_loginBtn);
        mNotHaveAccountTv = findViewById(R.id.login_not_have_accountTv);

        //initialize the FirebaseAuth instance.
        mAuth = FirebaseAuth.getInstance();

        //login button click
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //input data
                String email = mEmailEt.getText().toString().trim();
                String password = mPasswordEt.getText().toString().trim();
                //validate
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    //invalid email pattern set error
                    mEmailEt.setError("Invalid Email");
                    mEmailEt.setFocusable(true);
                } else {
                    //valid email
                    LoginActivity.this.loginUser(email, password);
                }
            }
        });

        //not have account click
        mNotHaveAccountTv.setOnClickListener(view -> startActivity(
                new Intent(LoginActivity.this, RegisterActivity.class)));

        //init progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging in...");
    }

    private void loginUser(String email, String password) {
        //show progress dialog
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        //dismiss progress dialog
                        progressDialog.dismiss();
                        // Sign in success, update UI with the signed-in user's information

                        FirebaseUser user = mAuth.getCurrentUser();
                        //User is sing in, start LoginActivity
                        startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
                        finish();
                    } else {
                        //dismiss progress dialog
                        progressDialog.dismiss();
                        // If sign in fails, display a message to the user.

                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(e -> {
                    //dismiss progress dialog
                    progressDialog.dismiss();
                    //error, get and shoe error message
                    Toast.makeText(LoginActivity.this, "" + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();    //go previous Activity
        return super.onSupportNavigateUp();
    }
}