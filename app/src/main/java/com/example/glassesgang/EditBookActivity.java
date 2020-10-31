package com.example.glassesgang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditBookActivity extends AppCompatActivity {
    private Button backButton;
    private Button scanButton;
    private Button saveButton;
    private EditText titleEditText;
    private EditText authorEditText;
    private EditText isbnEditText;
    private String author;
    private String title;
    private String isbn;
    private String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_book);

        findViewsById();

        Intent intent = getIntent();
        // implementation of book stats from Cholete
        final Book book = (Book) intent.getSerializableExtra("Book");
        author = book.getAuthor();
        title = book.getTitle();
        isbn = book.getISBN();

        titleEditText.setText(title);
        authorEditText.setText(author);
        isbnEditText.setText(isbn);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // meant to return to the previous activity without editing a book
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = titleEditText.getText().toString();
                author = authorEditText.getText().toString();
                isbn = isbnEditText.getText().toString();

                if (title.length()>0 && author.length()>0 && isbn.length()>0) {
//                     book.setTitle(title);
//                     book.setAuthor(author);
//                     book.setISBN(isbn);
                    // somehow add to the system and make sure photos are attached
                    finish();
                }

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
