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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.core.OrderBy;
import com.google.firebase.firestore.core.OrderBy.Direction;

import java.util.HashMap;

import static com.google.firebase.firestore.core.OrderBy.Direction.*;

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

        final Intent intent = getIntent();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // meant to return to the previous activity without adding a book
            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String title = titleEditText.getText().toString();
                final String author = authorEditText.getText().toString();
                final String isbn = isbnEditText.getText().toString();

                if (title.length()>0 && author.length()>0 && isbn.length()>0) {

                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    HashMap<String, Object> newBookInfo = new HashMap<>();
                    newBookInfo.put("title", title);
                    newBookInfo.put("author", author);
                    newBookInfo.put("isbn", isbn);
                    newBookInfo.put("status", "available");

                    CollectionReference books = db.collection("books");

                    // somehow get a reference to the most recent book to make new bookname
                    // also figure out how to do images
                    //
                    final String newBID = "book100";
                    books
                            .document(newBID)
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
