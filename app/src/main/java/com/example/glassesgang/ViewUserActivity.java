package com.example.glassesgang;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * This shows the user profile when card is clicked
 */
public class ViewUserActivity extends AppCompatActivity {

    TextView userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_user_activity);

        String user_info = getIntent().getStringExtra("user_info");
        userInfo = findViewById(R.id.user_info);
        userInfo.setText(user_info);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // redirects user to prev activity when back button clicked
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
