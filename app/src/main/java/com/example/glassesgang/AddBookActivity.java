package com.example.glassesgang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddBookActivity extends AppCompatActivity {
    private Button backButton;
    private Button scanButton;
    private Button saveButton;
    private EditText titleEditText;
    private EditText authorEditText;
    private EditText isbnEditText;
    private static final String TAG = "AddBookActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_book);

        findViewsById();

        // finish activity without adding a book
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // if given all the correct information, add the book to the database
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get the values for title, author, and isbn from the EditTexts
                final String title = titleEditText.getText().toString();
                final String author = authorEditText.getText().toString();
                final String isbn = isbnEditText.getText().toString();

                // if all three EditTexts have contents then add the book
                if (title.length()>0 && author.length()>0 && isbn.length()>0) {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    final String bid = java.util.UUID.randomUUID().toString(); // used a randomly generated UUID
                    final String owner = getIntent().getStringExtra("ownerName");
                    Book newBookInfo = new Book(title, author, isbn, bid, owner);  // the newly created book

                    // add the book to the list of total books
                    db.collection("books").document(bid)
                            .set(newBookInfo)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully updated!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error updating document", e);
                                }
                            });

                    // add the book to the owner's catalogue of owned books
                    db.collection("users").document(owner)
                            .update("ownerCatalogue", FieldValue.arrayUnion(bid))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully updated!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error updating document", e);
                                }
                            });
                    finish();  // the book has been added and we return to the previous activity
                }
            }
        });

        // scans a barcode, returns the ISBN and adds it to the EditText
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
