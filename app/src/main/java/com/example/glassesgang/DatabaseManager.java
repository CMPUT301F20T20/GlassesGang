package com.example.glassesgang;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Object for handling transactions in the database
 */
public class DatabaseManager {
    private FirebaseFirestore db;
    private static final String TAG = "DatabaseManager";


    public DatabaseManager() {
        db = FirebaseFirestore.getInstance();
    }
    /**
     * adds a book to the owner catalogue of given user
     * @param newBook a Book object. It is the book to be added
     * @param user a String consisting of the user's email.
     */
    public void addBook(final Book newBook , final String user) {
        // add the book to the database books collection
        db.collection("books")
                .add(newBook)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        documentReference.update("bid", documentReference.getId());
                        addBookInOwnerCatalogue(documentReference.getId(), user);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

    }

    /**
     * deletes the book from the database
     * @param bookToDelete a Book object representing the book to delete
     */
    public void deleteBook(Book bookToDelete) {
        String owner = bookToDelete.getOwner();
        String borrower = bookToDelete.getBorrower();
        String bid = bookToDelete.getBID();
        DocumentReference bookRef = db.collection("books").document(bid);

        // delete from owner Catalogue
        DocumentReference ownerRef = db.collection("users").document(owner);
        ownerRef.update("ownerCatalogue", FieldValue.arrayRemove(bid));

        // delete from borrower Catalogue if book is borrowed
        if (!borrower.equals("")) {
            DocumentReference borrowerRef = db.collection("users").document(borrower);
            borrowerRef.update("borrowerCatalogue", FieldValue.arrayRemove(bid));
        }

        // delete book from database
        bookRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });

        // check request list as well once requesting is implemented




    }


    private void addBookInOwnerCatalogue(String bid, String user) {
        db.collection("users").document(user)
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
    }
}
