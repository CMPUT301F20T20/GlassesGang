package com.example.glassesgang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.glassesgang.BookStatus.Status;
import static com.example.glassesgang.BookStatus.stringStatus;

import com.example.glassesgang.Transaction.Request;
import com.example.glassesgang.Transaction.RequestHandlingFragment;
import com.example.glassesgang.Transaction.TransactionFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import java.net.URL;

/**
 * Book profile for Owner View (edit book functionality)
 */
public class OwnerBookProfileActivity extends AppCompatActivity implements DeleteBookDialogFragment.DeleteBookDialogListener, RequestHandlingFragment.OnFragmentInteractionListener{
    private TextView titleTextView;
    private TextView authorTextView;
    private TextView isbnTextView;
    private Button statusButton;
    private TextView borrowerTextView;
    private Button deleteButton;
    private Button editButton;
    private ImageView bookImageView;
    private String author;
    private String title;
    private String isbn;
    private Status status;
    private String bid;
    private String borrower;
    private Book book;
    private String owner;
    private ArrayList<String> requests;
    private  FirebaseFirestore db;
    private static final String TAG = "OwnerBkProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_book_profile);

        // get email of owner
        String filename = getResources().getString(R.string.email_account);
        SharedPreferences sharedPref = this.getSharedPreferences(filename, Context.MODE_PRIVATE);
        owner = sharedPref.getString("email", "default value");

        if (owner == null) {
            Log.e("Email","No user email recorded");
        }

        findViewsById();
        
        db = FirebaseFirestore.getInstance();
        bid = getIntent().getStringExtra("bid");  // get book id of the book clicked
        DocumentReference docRef = db.collection("books").document(bid);

        // convert the book document to a Book object
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("ResourceType") //bypass layout check for request_list_layout
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                book = documentSnapshot.toObject(Book.class);
                author = book.getAuthor();
                title = book.getTitle();
                isbn = book.getISBN();
                status = book.getStatus();
                borrower = book.getBorrower();
                requests = book.getRequests();
                if (borrower == null || borrower.equals("")) {
                    borrower = "None";
                }
                updateTextViews();
                setBookImage(book);
                if (requests.size() > 0) inflateRequestFragment();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DeleteBookDialogFragment().show(getSupportFragmentManager(), "DELETE_BOOK");
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

    /**
     * assigns each attribute to the proper textView.
     */
    private void findViewsById() {
        titleTextView = findViewById(R.id.title_textView);
        authorTextView = findViewById(R.id.author_textView);
        isbnTextView = findViewById(R.id.isbn_textView);
        statusButton = findViewById(R.id.status_button);
        borrowerTextView = findViewById(R.id.borrower_textView);
        deleteButton = findViewById(R.id.delete_button);
        editButton = findViewById(R.id.edit_button);
        bookImageView = findViewById(R.id.borrowerBook_image_view);
    }

    /**
     * just set each TextViews text with the appropriate text
     */
    private void updateTextViews() {
        titleTextView.setText(title);
        authorTextView.setText(author);
        isbnTextView.setText(isbn);
        statusButton.setText(stringStatus(status));
        borrowerTextView.setText(borrower);
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
                        setBookImage(book);
                        updateTextViews();

                    }
                });
            }
        }
    }

    private void inflateRequestFragment() {
        //inflate requestList fragment inside framelayout fragment container
        Bundle bundle = new Bundle();
        bundle.putString("bin", book.getBID()); //store bin for later use in request handling
        Fragment requestFragment = new RequestHandlingFragment();
        requestFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.owner_book_profile_fragment_container, requestFragment).commit();
    }

    /**
     * When user confirms the deletion of a book, this method is called.
     * It deletes that book from the database.
     */
    @Override
    public void onConfirmPressed() {
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.deleteBook(book);
    }
    
    @Override
    public void onDeclineRequest() {

    }
    
    @Override
    public void onAcceptRequest(Request request) {
        //inflate requestList fragment inside framelayout fragment container
        Bundle bundle = new Bundle();
        bundle.putString("requestId", request.getRequestId()); //store bin for later use in request handling
        bundle.putString("borrowerEmail", request.getBorrowerEmail());
        Fragment transactionFragment = new TransactionFragment();
        transactionFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.owner_book_profile_fragment_container, transactionFragment).commit();
    }

    private void setBookImage(Book book) {
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
                    bookImageView.setImageBitmap(bmp);
                } catch (IOException e) {
                    Toast.makeText(
                            this,
                            "There was a problem fetching the image for the book " + book.getTitle(),
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        } else {
            bookImageView.setImageBitmap(null);
        }
    }
}