package com.example.glassesgang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

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
    private String bid;
    private String status;
    private static final String TAG = "EditBookActivity";

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
        bid = book.getBID();

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
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference bookRef = db.collection("books").document(bid);

                title = titleEditText.getText().toString();
                author = authorEditText.getText().toString();
                isbn = isbnEditText.getText().toString();

                if (title.length()>0 && author.length()>0 && isbn.length()>0) {
//                     book.setTitle(title);
//                     book.setAuthor(author);
//                     book.setISBN(isbn);
                    HashMap<String, Object> newBookInfo = new HashMap<>();
                    newBookInfo.put("title", title);
                    newBookInfo.put("author", author);
                    newBookInfo.put("isbn", isbn);
                    // somehow add to the system and make sure photos are attached

                    // update the database
                    bookRef
                            .update(newBookInfo)
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
