package com.example.glassesgang;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


public abstract class User {
    //private String TAG;
    public String userName;
    public String email;
    //public Object notifications;
    // private FirebaseFirestore db;
    CollectionReference userDatabase = FirebaseFirestore.getInstance().collection("users");
    String tag = "User";

    public User(Context context) {
        String filename = context.getResources().getString(R.string.email_account);
        SharedPreferences sharedPref = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        this.email = sharedPref.getString("email", "False");
    }

    public User(){
        // empty constructor for database access
    }

    public void editContactInfo(String currentEmail, String newEmail){
        userDatabase.document(currentEmail).update("email", newEmail)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(tag, "contact information successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(tag, "Error updating contact information", e);
                    }
                });
    }

    public String getTag() {
        return tag;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }



    public CollectionReference getUserDatabase() {
        return userDatabase;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public void setUserDatabase(CollectionReference userDatabase) {
        this.userDatabase = userDatabase;
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
