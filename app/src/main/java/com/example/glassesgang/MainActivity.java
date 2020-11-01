package com.example.glassesgang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_owner);

        mAuth = FirebaseAuth.getInstance();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser user) {
        // checks if account signed in
        if (user != null) {
            // redirect to user home page
            Intent homeIntent = new Intent(this, OwnerHomeActivity.class);
            homeIntent.putExtra("DisplayName", user.getDisplayName());
            startActivity(homeIntent);
        } else {
            // redirect to sign in page
            Intent signInIntent = new Intent(this, GoogleSignInActivity.class);
            startActivity(signInIntent);
        }
    }
}