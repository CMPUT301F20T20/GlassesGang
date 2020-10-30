package com.example.glassesgang;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddBookActivity extends AppCompatActivity {
    private Button backButton;
    private Button scanButton;
    private Button saveButton;
    private EditText titleEditText;
    private EditText authorEditText;
    private EditText isbnEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_book);

        findViewsById();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // return
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // implement adding
            }
        });

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // implement scanning
            }
        });
    }

    private void findViewsById() {
        backButton = findViewById(R.id.back_button);
        scanButton = findViewById(R.id.scan_button);
        saveButton = findViewById(R.id.finish_button);
        titleEditText = findViewById(R.id.book_title_bar);
        authorEditText = findViewById(R.id.author_name_bar);
        isbnEditText = findViewById(R.id.isbn_bar);
    }

}