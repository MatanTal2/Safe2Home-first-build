package com.example.safe2home;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
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

    private static final String TAG = "Login activity";
    //views
    TextView mNotHaveAccountTv, mRecoverPasswordTv;
    Button mLoginBtn;
    EditText mEmailEt, mPasswordEt;

    //Declare an instance of FirebaseAuth
    private FirebaseAuth FirebaseAuth;

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
        FirebaseAuth = FirebaseAuth.getInstance();

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
        mNotHaveAccountTv.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            finish();
        });

        //recover password textView click
        mRecoverPasswordTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRecoverPasswordDialog();
            }
        });


        //init progress dialog
        progressDialog = new ProgressDialog(this);

    }

    private void showRecoverPasswordDialog() {
        //AlterDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Recover Password");
        //set layout linear layout
        LinearLayout linearLayout = new LinearLayout(this);
        //views to set in the dialog
        EditText recoverDialogEmailEt = new EditText(this);
        recoverDialogEmailEt.setHint("Email");
        recoverDialogEmailEt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        /*
         *   sets the min width of a EditView to fit a text of n 'M' letters
         * regardless of the actual text extension and text size
         */
        recoverDialogEmailEt.setMinEms(16);


        linearLayout.addView(recoverDialogEmailEt);
        linearLayout.setPadding(10, 10, 10, 10);

        builder.setView(linearLayout);

        //button recover
        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //input email
                String emailAddress = recoverDialogEmailEt.getText().toString().trim();
                Log.d(TAG, "onClick: Recover password send an email to " + emailAddress);
                beginRecovery(emailAddress);
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

    private void beginRecovery(String emailAddress) {
        //show progress dialog
        progressDialog.setMessage("Sending email...");
        progressDialog.show();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        Log.d(TAG, "beginRecovery: email adrress is " + emailAddress);
        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                        }
                    }
                });



//        FirebaseAuth.sendPasswordResetEmail(emailAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                progressDialog.dismiss();
//                if (task.isSuccessful()) {
//                    Toast.makeText(LoginActivity.this, "Email sent", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                progressDialog.dismiss();
//                //get and show proper error message
//                Toast.makeText(LoginActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void loginUser(String email, String password) {
        //show progress dialog
        progressDialog.setMessage("Logging in...");
        progressDialog.show();

        FirebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        //dismiss progress dialog
                        progressDialog.dismiss();
                        // Sign in success, update UI with the signed-in user's information

                        FirebaseUser user = FirebaseAuth.getCurrentUser();
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