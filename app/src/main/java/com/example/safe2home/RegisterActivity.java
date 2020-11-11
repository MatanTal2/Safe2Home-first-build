package com.example.safe2home;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private static final int passwordlength = 6;
    //views
    EditText mEmailET, mPasswordET;
    Button mRegisterBtn;

    //progressbar to disply while registering user
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //ActionBar and it's title
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Create Account");
        //enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //init
        mEmailET = findViewById(R.id.emailET);
        mPasswordET = findViewById(R.id.passwordET);
        mRegisterBtn = findViewById(R.id.registerBtn);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering User...");

        //handle register btn click
        mRegisterBtn.setOnClickListener(view -> {
            // input email, password
            String email = mEmailET.getText().toString().trim();
            String password = mPasswordET.getText().toString().trim();
            //validate
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            {
                // set error and focus to email editText
                mEmailET.setError("Invalid Email");
                mEmailET.setFocusable(true);
            }
            else if (password.length() < 6)
            {
                // TODO make the password more secure.(letetrs uppercase smallercase digit and signs)
                // set error and focus to password editText
                mPasswordET.setError(String.format("Password length at least %d characters", passwordlength));
            }
            else
            {
                registerUser(email, password);  // register the user
            }

        });


    }

    private void registerUser(String email, String password) {
        // email and password pattern is valid, show progress dialog  and start registering user

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();    //go previous Activity
        return super.onSupportNavigateUp();
    }
}