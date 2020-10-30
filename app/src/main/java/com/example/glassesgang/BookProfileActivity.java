package com.example.glassesgang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class BookProfileActivity extends AppCompatActivity {
    private TextView sampleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_profile);

        Intent intent = getIntent();
        String sampleText = intent.getStringExtra("mockString");

        sampleTextView = findViewById(R.id.textView);
        sampleTextView.setText(sampleText);
    }
}