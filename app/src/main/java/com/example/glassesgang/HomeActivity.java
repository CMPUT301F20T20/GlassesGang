package com.example.glassesgang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.library);

        Intent bookProfileIntent = new Intent(this, BookProfileActivity.class);
        bookProfileIntent.putExtra("mockString", "mockStringValue");
        startActivity(bookProfileIntent);


    }
}