package com.example.glassesgang.Books;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.glassesgang.R;
import com.example.glassesgang.Requests.Request;
import com.example.glassesgang.Requests.RequestHandlingFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class OwnerBookProfileActivity extends AppCompatActivity implements RequestHandlingFragment.OnFragmentInteractionListener {
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
    private String bid;
    private String borrower;
    private Book book;
    private  FirebaseFirestore db;
    private static final String TAG = "OwnerBookProfileActivity";

    private ListView requestListView;
    private ArrayAdapter<Request> requestArrayAdapter;
    private ArrayList<Request> requestArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_book_profile);
        getSupportFragmentManager().beginTransaction().add(R.id.owner_book_profile_fragment_container, new RequestHandlingFragment()).commit();

        findViewsById();
        
        db = FirebaseFirestore.getInstance();
        bid = getIntent().getStringExtra("bid");  // get book id of the book clicked
        DocumentReference docRef = db.collection("books").document(bid);

        // convert the book document to a Book object
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                book = documentSnapshot.toObject(Book.class);
                author = book.getAuthor();
                title = book.getTitle();
                isbn = book.getISBN();
                status = book.getStatus();
                borrower = book.getBorrower();
                if (borrower == null || borrower.equals("")) {
                    borrower = "None";
                }
                updateTextViews();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // implement delete feature
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editIntent = new Intent(OwnerBookProfileActivity.this, EditBookActivity.class);
                editIntent.putExtra("bid", bid);   // pass in the bid of the book
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
    private void updateTextViews() {
        titleTextView.setText(title);
        authorTextView.setText(author);
        isbnTextView.setText(isbn);
        statusTextView.setText(status);
        //borrowerTextView.setText(borrower); if important remember to bring back the textview for this
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // If user chose to save changes made in EditBookActivity
        // update the book profile upon returning
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){

                String bid =  data.getStringExtra("bid");
                DocumentReference docRef = db.collection("books").document(bid);
                // get the book document from firestore using the document reference
                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        book = documentSnapshot.toObject(Book.class);   // convert the book document to Book Object
                        author = book.getAuthor();
                        title = book.getTitle();
                        isbn = book.getISBN();
                        status = book.getStatus();
                        borrower = book.getBorrower();
                        if (borrower == null || borrower.equals("")) {
                            borrower = "None";
                        }
                        updateTextViews();
                    }
                });
            }
        }
    }

    @Override
    public void onDeclineRequest() {
        //set status of requested item to declined (or delete request)
    }

    @Override
    public void onAcceptRequest() {
        //set status of request to accepted and switch fragment from requestlist to transaction
    }
}