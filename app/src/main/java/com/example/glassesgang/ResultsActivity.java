package com.example.glassesgang;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.SearchView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ResultsActivity extends AppCompatActivity {

    private ArrayAdapter<Book> bookArrayAdapter;
    private ArrayList<Book> bookArrayList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        // connect to the database
        db = FirebaseFirestore.getInstance();

        // setting up the array adapter
        bookArrayList = new ArrayList<Book>();
        bookArrayAdapter = new CustomBookList(getBaseContext(), bookArrayList, "b");

        Intent intent = getIntent();
        String query = intent.getExtras().getString("query");

        firebaseSearch(query);
    }

    private void firebaseSearch(final String query) {
        CollectionReference booksRef = db.collection("books");

        booksRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                bookArrayList.clear();

                for (QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                    // turning doc into book object
                    Book book = doc.toObject(Book.class);
                    book.setBID(doc.getId());

                    // grabbing book description
                    String title = book.getTitle().toLowerCase();
                    String author = book.getAuthor().toLowerCase();
                    String ISBN = book.getISBN().toLowerCase();

                    // filtering through results and adding to list if match
                    if (title.contains(query)) {
                        bookArrayList.add(book);
                    } else if (author.contains(query)){
                        bookArrayList.add(book);
                    } else if (ISBN.contains(query)) {
                        bookArrayList.add(book);
                    }

                    System.out.println(bookArrayList);
                }

                bookArrayAdapter.notifyDataSetChanged();
                System.out.println("hello " + bookArrayList);
            }
        });
    }
}