package com.example.glassesgang;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {
    private String TAG = "SignInActivity";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

    }

    private void updateUI(FirebaseUser user) {
        // successful sign in
        if (user != null) {
            // save email to sharedPreferences
            String filename = getResources().getString(R.string.email_account);
            SharedPreferences sharedPref = getSharedPreferences(filename, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("email", user.getEmail());
            editor.apply();
            User userobj = new User(user.getEmail());
            DatabaseManager.createUser(userobj);
            // redirect to user home page
            Intent homeIntent = new Intent(this, OwnerHomeActivity.class);
            startActivity(homeIntent);
        } else {
            System.out.println("Error Signing In");
        }
    }
}
