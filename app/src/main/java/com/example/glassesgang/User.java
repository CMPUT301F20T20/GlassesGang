package com.example.glassesgang;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

public class User {
    private static String TAG = "User Class";
    public String userName;
    public String email;
    private Object notifications;
    public ArrayList<String> ownerCatalogue;
    public ArrayList<String> borrowerCatalogue;


    public User(Context context) {
        String filename = context.getResources().getString(R.string.email_account);
        SharedPreferences sharedPref = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        this.email = sharedPref.getString("email", "False");
        ownerCatalogue = new ArrayList<String>();
        borrowerCatalogue = new ArrayList<String>();

    }

    public User(){
        // empty constructor for database access
    }
    public User(String email){
        this.email = email;
        ownerCatalogue = new ArrayList<String>();
        borrowerCatalogue = new ArrayList<String>();
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

    public ArrayList<String> getOwnerCatalogue() {
        return ownerCatalogue;
    }

    public void setOwnerCatalogue(ArrayList<String> ownerCatalogue) {
        this.ownerCatalogue = ownerCatalogue;
    }

    public ArrayList<String> getBorrowerCatalogue() {
        return borrowerCatalogue;
    }

    public void setBorrowerCatalogue(ArrayList<String> borrowerCatalogue) {
        this.borrowerCatalogue = borrowerCatalogue;
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
