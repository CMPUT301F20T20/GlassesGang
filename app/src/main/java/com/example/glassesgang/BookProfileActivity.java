package com.example.glassesgang;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class BookProfileActivity extends AppCompatActivity {
    private TextView titleTextView;
    private TextView authorTextView;
    private TextView isbnTextView;
    private TextView statusTextView;
    private Button deleteButton;
    private Button editButton;
    private String author;
    private String title;
    private String isbn;
    private String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_profile);

        findViewsById();

        Intent intent = getIntent();
        final Book book = (Book) intent.getSerializableExtra("Book");
        author = book.getAuthor();
        title = book.getTitle();
        isbn = book.getISBN();
        status = book.getStatus();

        setTextViews();

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // implement delete feature
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editIntent = new Intent(BookProfileActivity.this, EditBookActivity.class);
                editIntent.putExtra("Book", book);
//                startActivity(editIntent);
                startActivityForResult(editIntent, 1);
            }
        });

    }

    // assigns each attribute to the proper textView.
    private void findViewsById() {
        titleTextView = findViewById(R.id.title_textView);
        authorTextView = findViewById(R.id.author_textView);
        isbnTextView = findViewById(R.id.isbn_textView);
        statusTextView = findViewById(R.id.status_textView);

        deleteButton = findViewById(R.id.delete_button);
        editButton = findViewById(R.id.edit_button);
    }

    // just set each TextViews text with the appropriate text
    private void setTextViews() {
        titleTextView.setText(title);
        authorTextView.setText(author);
        isbnTextView.setText(isbn);
        statusTextView.setText(status);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // If user chose to save changes made in EditBookActivity
        // Update the book profile upon returning
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                Book newBook = (Book) data.getSerializableExtra("Book");
                title = newBook.getTitle();
                author = newBook.getAuthor();
                isbn = newBook.getISBN();
                setTextViews();
            }
        }
    }
}