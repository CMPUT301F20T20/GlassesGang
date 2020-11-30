package com.example.glassesgang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.glassesgang.Transaction.TransactionFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.example.glassesgang.BookStatus.Status;
import static com.example.glassesgang.BookStatus.stringStatus;
import com.example.glassesgang.Transaction.Request;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * Book Profile for Borrower view (no edit book functionality)
 */
public class BorrowerBookProfileActivity extends AppCompatActivity implements TransactionFragment.OnTransactionInteractionListener{
    private Button statusButton;
    private TextView titleTextView;
    private TextView authorTextView;
    private TextView isbnTextView;
    private TextView ownerTextView;
    private ImageView bookImageView;
    private Status status;
    private String title;
    private String author;
    private String isbn;
    private String owner;

    private String bid;
    private Book book;
    private FirebaseFirestore db;
    private String user;
    final String TAG = "Database error";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrower_book_profile);

        findViewsById();

        // get the user email
        String filename = getResources().getString(R.string.email_account);
        SharedPreferences sharedPref = getSharedPreferences(filename, Context.MODE_PRIVATE);
        user = sharedPref.getString("email", "null");
        if (user == "null") {
            Log.e("Email","No user email recorded");
        }

        db = FirebaseFirestore.getInstance();
        bid = getIntent().getStringExtra("bid");
        DocumentReference docRef = db.collection("books").document(bid);

        // get the book document from firestore using the document reference and display book information
        docRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        book = documentSnapshot.toObject(Book.class);   // convert the book document to Book Object
                        author = book.getAuthor();
                        title = book.getTitle();
                        isbn = book.getISBN();
                        owner = book.getOwner();
                        setBorrowerStatus(book);  // text views updated inside this method after the status is set
                        setBookImage(book, bookImageView);
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
        ownerTextView = findViewById(R.id.bookOwner_textView);
        bookImageView = findViewById(R.id.borrowerBook_image_view);
    }

    /**
     * just set each TextViews text with the appropriate text
     */
    private void setTextViews() {
        titleTextView.setText(title);
        authorTextView.setText(author);
        isbnTextView.setText(isbn);
        ownerTextView.setText(owner);
        statusButton.setText(stringStatus(status));
        setStatusButtonListeners();
    }

    private void setBorrowerStatus(Book book) {
        DocumentReference borrowerCatRef = db.collection("users").document(user).collection("borrowerCatalogue").document(bid);
        //TODO: fix bug, for some reason it keeps failing here...
        borrowerCatRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        book.setStringStatus(document.get("requestRefStatus").toString());  // if book is in borrower catalogue, use the status from there
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        // in this case the book is requested by other borrowers, but not the current borrower
                        // so it should appear as available to them
                        if (book.getStatus() == Status.REQUESTED) {
                            book.setStatus(Status.AVAILABLE);
                        }
                        Log.d(TAG, "No such document");
                    }
                    // set the text for status text view and update all the text views
                    status = book.getStatus();
                    setTextViews();
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private void inflateTransactionFragment(String requestId, String ownerEmail) {
        //inflate requestList fragment inside framelayout fragment container
        db.collection("requests").document(requestId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot req) {
                Bundle bundle = new Bundle();
                bundle.putString("requestId", requestId); //store bin for later use in request handling
                bundle.putString("userEmail", ownerEmail);
                bundle.putString("userType", "b");
                Map<String, Double> locationHashMap = (Map<String, Double>) req.get("location");
                if (locationHashMap != null) bundle.putParcelable("givenLocation", new LatLng(locationHashMap.get("latitude"), locationHashMap.get("longitude")));
                Fragment transactionFragment = new TransactionFragment();
                transactionFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.borrower_book_profile_fragment_container, transactionFragment).commit();
            }
        });
    }

    private void setBookImage(Book book, ImageView bookImage) {
        String bookImageUrl = book.getImageUrl();
        if (bookImageUrl != null && bookImageUrl != "") {
            int SDK_INT = android.os.Build.VERSION.SDK_INT;

            if (SDK_INT > 8) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                URL url;
                try {
                    url = new URL(bookImageUrl);
                } catch (MalformedURLException e) {
                    Log.d(TAG, "URL not valid " + bookImageUrl);
                    return;
                }

                try {
                    Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    bookImage.setImageBitmap(bmp);
                } catch (IOException e) {
                    Toast.makeText(
                            this,
                            "There was a problem fetching the image for the book " + book.getTitle(),
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        }
    }

    public void setStatusButtonListeners() {
        switch(status) {
            case AVAILABLE: //make a request for this book
                statusButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get the values for title, author, and isbn from the EditTexts
                        Request newRequest = new Request(bid, user, owner);
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
                        database.deleteRequest(user, bid);
                        finish();
                    }
                });
                break;
            case ACCEPTED: //show transaction fragment to accept book at owner specified location
                db.collection("users").document(user).collection("borrowerCatalogue").document(bid).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                inflateTransactionFragment(documentSnapshot.get("requestRefId").toString(), book.getOwner());
                            }
                        });
        }
    }

    @Override
    public void onTransactionPressed() {
        //TODO: open scanner
        finish();
    }
}