package com.example.glassesgang;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.glassesgang.Notification.App;
import com.example.glassesgang.Notification.Notification;
import com.google.android.gms.tasks.Continuation;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
     *
     * @param newBook a Book object. It is the book to be added
     * @param user    a String consisting of the user's email.
     */
    public static void addBook(final Book newBook, final String user) {
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
     *
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
            // get reference to the borrower catalogue
            CollectionReference borrowerCatRef = db.collection("users").document(borrower).collection("borrowerCatalogue");
            // delete the book document from borrower catalogue
            borrowerCatRef.document(bid)
                    .delete()
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
    public static void createUser(final User user) {
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
                        userCatalogue.put("notificationCatalogue", new ArrayList<String>());
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

    public static void editContactInfo(String currentEmail, String newEmail) {
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
     *
     * @param request The request object to be added to the database
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
                        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Request requestObj = documentSnapshot.toObject(Request.class);
                                addNotification(new Notification(requestObj.getBorrowerEmail(),
                                        requestObj.getOwnerEmail(),
                                        Notification.NotificationType.NEW_REQUEST,
                                        requestObj.getBookId()));
                            }
                        });

                        //create notification and add it
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
                .set(new RequestReference(Status.REQUESTED, requestId))
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

    public static void deleteRequest(String userId, String bid) {
        //get user's request id for that book from their borrowerCatalogue
        DocumentReference borrowerCatReqRef = db.collection("users").document(userId).collection("borrowerCatalogue").document(bid);
        if (borrowerCatReqRef != null) {
            borrowerCatReqRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        String requestId = (String) documentSnapshot.get("requestRefId");
                        DocumentReference bookRef = db.collection("books").document(bid);

                        //delete request from books request list
                        bookRef.update("requests", FieldValue.arrayRemove(requestId));
                        bookRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        ArrayList<String> requests = (ArrayList<String>) document.get("requests");
                                        if (requests.size() == 0) { // it means the book has no more requests and so its status must be set to available
                                            changeBookStatus(Status.AVAILABLE, bid);
                                        }
                                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                    } else {
                                        Log.d(TAG, "No such document");
                                    }
                                } else {
                                    Log.d(TAG, "get failed with ", task.getException());
                                }
                            }
                        });

                        //delete user's request from db
                        DocumentReference reqRef = db.collection("requests").document(requestId);
                        reqRef.delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "Request " + requestId + " successfully deleted!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error deleting request " + requestId, e);
                                    }
                                });
                        //delete request from user's borrowerCatalogue
                        borrowerCatReqRef.delete();
                    }
                }
            });
        }
    }

    public static void addNotification(Notification notification) {
        // add the request to the requests collection in the database
        db.collection("notifications")
                .add(notification)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Notification added successfully, written with ID: " + documentReference.getId());
                        documentReference.update("notificationId", documentReference.getId());
                        notification.setNotificationId(documentReference.getId());
                        addNotificationToUser(notification);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    private static void addNotificationToUser(Notification notification) {
        // adds to receiver's notification array
        db.collection("users")
                .document(notification.getReceiverEmail())
                .update("notificationCatalogue", FieldValue.arrayUnion(notification.getNotificationId()))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Notification successfully added to user " + notification.getReceiverEmail());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding notification to user" + notification.getReceiverEmail(), e);
                    }
                });
/*
        // TODO: create a notification to be displayed as a popup
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,
                App.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setContentTitle(notification.getPopupTitle())
                .setContentText(notification.getPopupText());

        // TODO: create the notification alert on receiver's phone
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(1, builder.build());

 */
    }

    public static void deleteNotification(Notification not) {
        String notId = not.getNotificationId();
        DocumentReference notRef = db.collection("notifications").document(notId);
        DocumentReference userRef = db.collection("users").document(not.getReceiverEmail());

        // delete notification from users notificationCatalogue //TODO: add a check for if the notification exists
        userRef.update("notificationCatalogue", FieldValue.arrayRemove(notId));

        // delete notification from database
        notRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Notification " + notId + " successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting request " + notId, e);
                    }
                });

    }

    public static void acceptRequest(String bid, String borrowerEmail, String ownerEmail, String requestId) {
        //change book status to accepted
        changeBookStatus(Status.ACCEPTED, bid);

        //change accepted borrower's request in borrowerCatalogue to have status of accepted
        DocumentReference borrowerRequest = db.collection("users")
                .document(borrowerEmail)
                .collection("borrowerCatalogue")
                .document(bid);
        borrowerRequest.update("requestRefStatus", Status.ACCEPTED);


        //send notification to borrower that their request has been accepted
        addNotification(new Notification(ownerEmail, borrowerEmail, Notification.NotificationType.ACCEPT_REQUEST, bid));

        //delete all other requests of other borrowers of that book
        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<String> userList = new ArrayList<String>();
                    for (QueryDocumentSnapshot document : task.getResult()) {  //add rejected borrowers to a list
                        if (!document.getId().equals(borrowerEmail)) //exclude accepted borrower
                            userList.add(document.getId());
                    }
                    userList.forEach(user -> deleteRequest(user, bid));
                } else {
                    Log.d(TAG, "Error getting users: ", task.getException());
                }
            }
        });

        /* //not needed, but could be useful as a final check to ensure no other requests exist at the moment of accepting a request
        // delete all other requests from book's request list
        DocumentReference bookRef = db.collection("books").document(bid);
        bookRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ArrayList<String> requestsToRemove = (ArrayList<String>) documentSnapshot.get("requests");
                requestsToRemove.remove(request.getRequestId()); //remove accepted request from requestsToRemove
                requestsToRemove.forEach(request -> {
                    bookRef.update("requests", FieldValue.arrayRemove(request));
                });
            }
        });
        */
    }
}