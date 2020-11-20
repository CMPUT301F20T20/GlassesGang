package com.example.glassesgang;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.glassesgang.Transaction.Request;
import com.example.glassesgang.BookStatus.Status;
import com.example.glassesgang.Transaction.RequestReference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Object for handling transactions in the database
 */
public class DatabaseManager {
    private static FirebaseFirestore db;
    private static final String TAG = "DatabaseManager";


    public DatabaseManager() {
        db = FirebaseFirestore.getInstance();
    }

    /**
     * adds a book to the owner catalogue of given user
     * @param newBook a Book object. It is the book to be added
     * @param user a String consisting of the user's email.
     */
    public static void addBook(final Book newBook , final String user) {
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
    public static void deleteBook(Book bookToDelete) {
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

    private static void addBookInOwnerCatalogue(String bid, String user) {
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

    // User database interactions begin
    public static void createUser(final User user){
        final CollectionReference usersDatabase = FirebaseFirestore.getInstance().collection("users");
        usersDatabase.document(user.getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "User already exist: " + document.getData());
                    } else {
                        Map<String, ArrayList<String>> userCatalogue = new HashMap<>();
                        userCatalogue.put("ownerCatalogue", new ArrayList<String>());
                        userCatalogue.put("borrowerCatalogue", new ArrayList<String>());
                        //userCatalogue.put("email", new String());
                        // adding the user
                        usersDatabase.document(user.getEmail())
                                .set(userCatalogue)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "User successfully added to database");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error adding user to database", e);
                                    }
                                });
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }


    public static void editContactInfo(String currentEmail, String newEmail){
        final CollectionReference usersDatabase = FirebaseFirestore.getInstance().collection("users");
        usersDatabase.document(currentEmail).update("email", newEmail)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "contact information successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating contact information", e);
                    }
                });
    }

    /**
     * Make a request as a borrower. Creates a request object and attaches it to the book
     * @param request   The request object to be added to the database
     */
    public static void addRequest(Request request) {
        // add the request to the requests collection in the database
        db.collection("requests")
                .add(request)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Request added successfully, written with ID: " + documentReference.getId());
                        documentReference.update("requestId", documentReference.getId());
                        addRequestToBook(documentReference.getId(), request.getBookId());
                        addRequestToUser(documentReference.getId(), request.getBookId(), request.getBorrowerEmail());
                        //addNotificationToUser(documentReference, request.getOwnerEmail);
                        changeBookStatus(Status.REQUESTED, request.getBookId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    public static void addRequestToBook(String requestId, String bid) {
        db.collection("books").document(bid)
                .update("requests", FieldValue.arrayUnion(requestId))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Request successfully added to book " + bid);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding request to book " + bid, e);
                    }
                });
    }

    public static void addRequestToUser(String requestId, String bid, String userEmail) {
        db.collection("users").document(userEmail).collection("borrowerCatalogue").document(bid)
                .set( new RequestReference(Status.REQUESTED, requestId))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Request reference for request " + requestId + " successfully added to user");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Failed to add request reference for request " + requestId + " to user", e);
                    }
                })

        ;
    }

    public static void changeBookStatus(Status status, String bid) {
        db.collection("books").document(bid).update("status", status)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Book status successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating book status", e);
                    }
                });
    }

    public static void deleteRequest(Request requestToDelete, Status newStatus) {
        String bid = requestToDelete.getBookId();
        DocumentReference bookRef = db.collection("books").document(bid);
        DocumentReference requestRef = db.collection("requests").document(requestToDelete.getRequestId());

        // send notification to borrower on request status


        // delete request from its books request list
        bookRef.update("ownerCatalogue", FieldValue.arrayRemove(requestToDelete.getRequestId()));

        //handle the book status to its new status
        changeBookStatus(newStatus, bid);

        // delete request from database
        requestRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Request " + requestToDelete.getRequestId() + " successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting request " + requestToDelete.getRequestId(), e);
                    }
                });

    }

    //TODO: awaiting notification schema to be finished to complete this method
    /*
    public static void addNotificationToUser(String requestId)
    */
}
