package com.example.glassesgang;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.glassesgang.R;
import com.example.glassesgang.User;

public class UserProfile extends AppCompatActivity {
    public User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        int darkGreyBackground = 282828;
        getWindow().getDecorView().setBackgroundColor(darkGreyBackground);
    }
}
