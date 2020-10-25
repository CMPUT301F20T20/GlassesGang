package com.example.glassesgang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // checks if there's an account signed
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        // redirect to appropriate activity
        updateUI(account);
    }

    private void updateUI(GoogleSignInAccount user) {
        // checks if account signed in
        if (user != null) {
            // redirect to user home page
            Intent homeIntent = new Intent(this, HomeActivity.class);
            startActivity(homeIntent);
            finish();
        } else {
            // redirect to sign in page
            Intent signInIntent = new Intent(this, GoogleSignInActivity.class);
            startActivity(signInIntent);
            finish();
        }
    }
}