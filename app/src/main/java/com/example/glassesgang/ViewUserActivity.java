package com.example.glassesgang;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class ViewUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_user_activity);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // redirects user to prev activity when back button clicked
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
