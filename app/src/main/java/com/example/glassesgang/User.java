package com.example.glassesgang;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public abstract class User {
    private String TAG;
    public String userName;
    public String email;
    private Object notifications;
    public FirebaseUser googleUserAccount;
    private FirebaseFirestore db;
    CollectionReference userDatabase = db.collection("users");

    public User(FirebaseUser googleUserAccount) {
        this.googleUserAccount = googleUserAccount;
        this.userName = googleUserAccount.getEmail();
        this.email = googleUserAccount.getEmail();
        addUserToDatabase();

    }

    public void addUserToDatabase(){
        Map<String, Object> data1 = new HashMap<>();
        data1.put("borrowerID", this.email);
        data1.put("ownerID", this.email);
        data1.put("email", this.email);
        // data1.put("regions", Arrays.asList("west_coast", "norcal"));
        userDatabase.document(this.email).set(data1);
    }

    public void editContactInfo(String currentEmail, String newEmail){
        userDatabase.document(currentEmail).update("email", newEmail)
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

    /*
    public void editUsername(String newUserName) {
        if (userName.equals(this.userName)) {
            this.userName = newUserName;
            // update database here

        }
    };
    public void editPassword(String userName, String password, String newPassword){
        if (password.equals(this.password) && userName.equals(this.userName)){
            this.password = newPassword;
            // update database here
        }

    };

     */
}
