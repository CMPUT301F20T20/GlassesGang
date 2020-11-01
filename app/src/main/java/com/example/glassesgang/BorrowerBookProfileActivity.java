package com.example.glassesgang;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class BorrowerBookProfileActivity extends AppCompatActivity {
    private TextView titleTextView;
    private TextView authorTextView;
    private TextView isbnTextView;
    private TextView statusTextView;
    private TextView ownerTextView;
    private String author;
    private String title;
    private String isbn;
    private String status;
    private String bookID;
    private String owner;
    private Book book;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrower_book_profile);

        findViewsById();

        db = FirebaseFirestore.getInstance();
        final String path = getIntent().getStringExtra("path");   // get the path to the book document
        DocumentReference docRef = db.document(path);    // get reference to the book object using path

        // get the book document from firestore using the document reference
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                book = documentSnapshot.toObject(Book.class);   // convert the book document to Book Object
                author = book.getAuthor();
                title = book.getTitle();
                isbn = book.getISBN();
                status = book.getStatus();
                owner = book.getOwner();
                setTextViews();
            }
        });
    }

    // assigns each attribute to the proper textView.
    private void findViewsById() {
        titleTextView = findViewById(R.id.title_textView);
        authorTextView = findViewById(R.id.author_textView);
        isbnTextView = findViewById(R.id.isbn_textView);
        statusTextView = findViewById(R.id.status_textView);
        ownerTextView = findViewById(R.id.owner_textView);
    }

    // just set each TextViews text with the appropriate text
    private void setTextViews() {
        titleTextView.setText(title);
        authorTextView.setText(author);
        isbnTextView.setText(isbn);
        statusTextView.setText(status);
        ownerTextView.setText(owner);
    }
}