package com.example.glassesgang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.glassesgang.BookStatus.Status;
import static com.example.glassesgang.BookStatus.stringStatus;
import com.example.glassesgang.Transaction.Request;
import com.example.glassesgang.Transaction.RequestHandlingFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


/**
 * Book Profile for Borrower view (no edit book functionality)
 */
public class BorrowerBookProfileActivity extends AppCompatActivity {
    private TextView titleTextView;
    private TextView authorTextView;
    private TextView isbnTextView;
    private Button statusButton;
    private TextView ownerTextView;
    private String author;
    private String title;
    private String isbn;
    private Status status;
    private String bid;
    private String owner;
    private Book book;
    private String userEmail;
    private FirebaseFirestore db;
    final String TAG = "Database error";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrower_book_profile);

        findViewsById();

        //get user
        String filename = getResources().getString(R.string.email_account);
        SharedPreferences sharedPref = this.getSharedPreferences(filename, Context.MODE_PRIVATE);
        userEmail = sharedPref.getString("email", null);

        //get db
        db = FirebaseFirestore.getInstance();
        bid = getIntent().getStringExtra("bid");
        DocumentReference docRef = db.collection("books").document(bid);

        // get the book document from firestore using the document reference
        docRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        book = documentSnapshot.toObject(Book.class);   // convert the book document to Book Object
                        author = book.getAuthor();
                        title = book.getTitle();
                        isbn = book.getISBN();
                        status = book.getStatus();
                        owner = book.getOwner();
                        setTextViews();
                        switch(status) {
                            case AVAILABLE: //make a request for this book
                                statusButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // get the values for title, author, and isbn from the EditTexts
                                            Request newRequest = new Request(bid, userEmail, owner);
                                            DatabaseManager database = new DatabaseManager();
                                            database.addRequest(newRequest);
                                            // TODO: somehow add to the system and make sure photos are attached
                                            finish();
                                    }
                                });
                                break;
                            case REQUESTED: //cancel request
                                statusButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //find request object in db
                                        //TODO: get requestid from borrower catalogue
                                        DatabaseManager database = new DatabaseManager();
                                        //database.deleteRequest(Status.AVAILABLE);
                                        finish();
                                    }
                                });
                                break;
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "Data could not be fetched " + e.toString());
                    }
                });
    }

    /**
     * assigns each attribute to the proper textView.
     */
    private void findViewsById() {
        titleTextView = findViewById(R.id.title_textView);
        authorTextView = findViewById(R.id.author_textView);
        isbnTextView = findViewById(R.id.isbn_textView);
        statusButton = findViewById(R.id.status_button);
        ownerTextView = findViewById(R.id.owner_textView);
    }

    /**
     * just set each TextViews text with the appropriate text
     */
    private void setTextViews() {
        titleTextView.setText(title);
        authorTextView.setText(author);
        isbnTextView.setText(isbn);
        statusButton.setText(stringStatus(status));
        ownerTextView.setText(owner);
    }
}