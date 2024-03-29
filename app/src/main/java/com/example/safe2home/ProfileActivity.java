package com.example.safe2home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "Profile Activity";
    // Firebase auth
    FirebaseAuth firebaseAuth;

    //views
    TextView mProfileTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //ActionBar and it's title
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Profile");

        // initialize the FirebaseAuth instance
        firebaseAuth = FirebaseAuth.getInstance();

        //init views

    }

    private void checkUserStatus()
    {
        //Get current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null)
        {
            //the user is still signed in, stay here
            //set email of logged in user
            Log.d(TAG, "checkUserStatus: ");
            //mProfileTv.setText(user.getEmail());
            Toast.makeText(this, "Logged as" + user.getEmail(), Toast.LENGTH_SHORT).show();
        }
        else
        //the user is not sign in, go to main Activity
        {
            startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStart() {
        //check on start of app
        checkUserStatus();
        super.onStart();
    }

    //inflate option menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflating menu
        getMenuInflater().inflate(R.menu.new_item_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    /*handle menu item clicks*/

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //get item id
        int id = item.getItemId();
        if (id == R.id.action_logout)
        {
            firebaseAuth.signOut();
            checkUserStatus();
        }
        return super.onOptionsItemSelected(item);
    }
}