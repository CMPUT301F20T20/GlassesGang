package com.example.glassesgang;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.glassesgang.BookStatus.Status;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.UUID;

/**
 * Activity for editing book in the database
 * possible fields to update are author, title, isbn
 */
public class EditBookActivity extends AppCompatActivity {
    private Button backButton;
    private Button scanButton;
    private Button addImageButton;
    private Button deleteImageButton;
    private Button saveButton;
    private EditText titleEditText;
    private EditText authorEditText;
    private EditText isbnEditText;
    private ImageView bookImageView;
    private Status status;
    private Book book;
    FirebaseFirestore db;
    private static final String TAG = "EditBookActivity";
    private final int CAMERA_PHOTO_TAKEN = 102;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private String bookImageUrl;
    // scanner variables
    private String ISBN;
    private final int SCAN_TAKEN = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_book);

        findViewsById();

        db = FirebaseFirestore.getInstance();
        String bid = getIntent().getStringExtra("bid");   // get the bid
        final DocumentReference docRef = db.collection("books").document(bid);    // get reference to the book object using bid

        // convert book document to Book object
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                book = documentSnapshot.toObject(Book.class);
                setTextViews(book);
            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // meant to return to the previous activity without editing a book
            }
        });

        // save the changes if there is no empty field or invalid input
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isThereInvalidInput()) {
                    // get the text from edit text fields
                    String title = titleEditText.getText().toString();
                    String author = authorEditText.getText().toString();
                    String isbn = isbnEditText.getText().toString();
                    
                    book.setTitle(title);
                    book.setAuthor(author);
                    book.setISBN(isbn);
                    book.setImageUrl(bookImageUrl);

                    updateDatabase(docRef, book);

                    setResult(Activity.RESULT_OK, getIntent());   // so when we go back to OwnerBookProfileActivity, it knows that it must update itself.
                    CameraActivity.cleanBookImage();
                    finish();
                }
            }
        });

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditBookActivity.this, ScannerActivity.class);
                startActivityForResult(intent, SCAN_TAKEN);
            }
        });

        deleteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CameraActivity.cleanBookImage();
                bookImageView.setImageBitmap(null);
                bookImageUrl = null;
            }
        });

        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Encapsulate Camera in its own class
                Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
                startActivityForResult(intent, CAMERA_PHOTO_TAKEN);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_PHOTO_TAKEN) {
            Bitmap bookImage = CameraActivity.getBookImage();
            bookImageView.setImageBitmap(bookImage);

            if (bookImage != null) {
                uploadPictureToStorage(bookImage);
            }
        }
        if (requestCode == SCAN_TAKEN){
            if (data != null) {
                ISBN = data.getStringExtra("ISBN");  // data returned from scanner activity
                // TODO implment google books API here
                isbnEditText.setText(ISBN);
            }
        }
    }

    /**
     * assigns each attribute to the proper textView.
     */
    private void findViewsById() {
        addImageButton = findViewById(R.id.add_image_button);
        backButton = findViewById(R.id.back_button);
        scanButton = findViewById(R.id.scan_button);
        saveButton = findViewById(R.id.finish_button);
        deleteImageButton = findViewById(R.id.delete_image_button);
        titleEditText = findViewById(R.id.book_title_bar);
        authorEditText = findViewById(R.id.author_name_bar);
        isbnEditText = findViewById(R.id.isbn_bar);
        bookImageView = findViewById(R.id.login_book_image_view);
    }

    /**
     * Update the title, author, and isbn fields in the database if user choses to save the changes made.
     * @param docRef document location in the Firestore data
     */
    private void updateDatabase(DocumentReference docRef, Book newBook) {
        // put the new info in a hash map
        HashMap<String, Object> newBookInfo = new HashMap<>();
        newBookInfo.put("title", newBook.getTitle());
        newBookInfo.put("author", newBook.getAuthor());
        newBookInfo.put("isbn", newBook.getISBN());
        newBookInfo.put("imageUrl", bookImageUrl);

        // update the database
        docRef
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
    }

    /**
     * Upload picture taken from user's camera to Firebase Storage
     * @param bitmap object contains picture to upload
     */
    private void uploadPictureToStorage(Bitmap bitmap) {
        // save image
        String path = "bookImages/" + UUID.randomUUID() + ".jpg";
        StorageReference storageRef = storage.getReference();
        StorageReference reference = storageRef.child(path);

        // Create file metadata including the content type
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/jpg")
                .build();

        // convert bitmap to bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        // Upload the file and metadata
        UploadTask uploadTask = reference.putBytes(data, metadata);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return reference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    setBookImageUrl(downloadUri.toString());
                } else {
                    Toast.makeText(EditBookActivity.this, "Failed to upload image", Toast.LENGTH_SHORT);
                }
            }
        });
    }

    /**
     * Sets the image for a book
     * @param book object that contains necessary image url
     */
    private void setBookImage(Book book) {
        bookImageUrl = book.getImageUrl();
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
        }
    }

    /**
     * Updates the book image url with the one passed in the parameter
     * @param newBookImageUrl contains new book image url to set
     */
    private void setBookImageUrl(String newBookImageUrl) {
        bookImageUrl = newBookImageUrl;
    }

    /**
     * just set each TextViews text with the appropriate text
     */
    private void setTextViews(Book selectedBook) {
        titleEditText.setText(selectedBook.getTitle());
        authorEditText.setText(selectedBook.getAuthor());
        isbnEditText.setText(selectedBook.getISBN());
        setBookImage(selectedBook);
    }

    /**
     * Checks for empty fields in the title, author and isbn fields and that isbn is only of length 13.
     * Displays error message for the invalid fields.
     * @return true if there is an invalid input, false otherwise.
     */
    public boolean isThereInvalidInput() {
        boolean invalidInput = false;
        if (titleEditText.getText().toString().trim().equalsIgnoreCase("")) {
            titleEditText.setError("This field can not be blank");
            invalidInput = true;
        }
        if (authorEditText.getText().toString().trim().equalsIgnoreCase("")) {
            authorEditText.setError("This field can not be blank");
            invalidInput = true;
        }
        if (isbnEditText.getText().toString().trim().equalsIgnoreCase("")) {
            isbnEditText.setError("This field can not be blank");
            invalidInput = true;
        } else if (isbnEditText.getText().toString().length() != 13) {   // ISBN can only be of length 13
            isbnEditText.setError("invalid ISBN");
            invalidInput = true;
        }
        return invalidInput;
    }

}
