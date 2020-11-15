package com.example.safe2home;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    //views
    TextView mNotHaveAccountTv, mRecoverPasswordTv;
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
        mRecoverPasswordTv = findViewById(R.id.login_recover_password_Tv);

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

        //recover password textView click
        mRecoverPasswordTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRecoverPasswordDialog();
            }
        });


        //init progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging in...");
    }

    private void showRecoverPasswordDialog() {
        //AlterDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Recover Password");
        //set layout linear layout
        LinearLayout linearLayout = new LinearLayout(this);
        //views to set in the dialog
        EditText emailEt = new EditText(this);
        emailEt.setHint("Email");
        emailEt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        linearLayout.addView(emailEt);
        linearLayout.setPadding(10,10,10,10);

        builder.setView(linearLayout);

        //button recover
        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //input email
                String email = mEmailEt.getText().toString().trim();
                beginRecovery(email);
            }
        });
        //button cancel
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dismiss dialog
                dialog.dismiss();
            }
        });

        //show dialog
        builder.create().show();
    }

    private void beginRecovery(String email) {
        mAuth.sendPasswordResetEmail(email).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
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