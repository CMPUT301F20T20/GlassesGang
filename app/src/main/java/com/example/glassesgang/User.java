package com.example.glassesgang;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.glassesgang.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;


/**
 * TODO: Currently, database stores attributes fields not objects, will implement Object storage for submission 4
 */
public class User {
    private static String TAG = "User Class";
    public String userName;
    public String email;
    private Object notifications;
    // private FirebaseFirestore db;
    CollectionReference userDatabase = FirebaseFirestore.getInstance().collection("users");

    public User(Context context) {
        String filename = context.getResources().getString(R.string.email_account);
        SharedPreferences sharedPref = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        this.email = sharedPref.getString("email", "False");
    }

    public User(){
        // empty constructor for database access
    }
    public User(String email) {
        this.email = email;
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

    public String getTAG() {
        return TAG;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public Object getNotifications() {
        return notifications;
    }


    public CollectionReference getUserDatabase() {
        return userDatabase;
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
