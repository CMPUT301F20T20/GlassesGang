package com.example.glassesgang;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ResultsActivity extends AppCompatActivity {

    private ArrayAdapter<Book> bookAdapter;
    private ArrayList<Book> bookList;
    private FirebaseFirestore db;
    ListView resultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        resultList = findViewById(R.id.result_list_view);

        // connect to the database
        db = FirebaseFirestore.getInstance();

        // setting up the array adapter
        bookList = new ArrayList<Book>();
        bookAdapter = new CustomBookList(getBaseContext(), bookList, "b");
        resultList.setAdapter(bookAdapter);

        // getting query string
        Intent intent = getIntent();
        String query = intent.getExtras().getString("query");

        firebaseSearch(query);

        // making each item clickable and open book profile
        resultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // get the book id of the book that was pressed
                Book book = (Book) adapterView.getItemAtPosition(i);
                String bid = book.getBID();

                // get book reference from db
                DocumentReference bookRef = db.collection("books").document(bid);

                // open up the appropriate book profile
                Intent bookProfileIntent = new Intent(getBaseContext(), BorrowerBookProfileActivity.class);
                bookProfileIntent.putExtra("bid", bid);
                startActivity(bookProfileIntent);
            }
        });
    }

    private void firebaseSearch(final String query) {
        CollectionReference booksRef = db.collection("books");

        booksRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                bookList.clear();

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
                        bookList.add(book);
                    } else if (author.contains(query)){
                        bookList.add(book);
                    } else if (ISBN.contains(query)) {
                        bookList.add(book);
                    }
                }
                bookAdapter.notifyDataSetChanged();
            }
        });
    }
}