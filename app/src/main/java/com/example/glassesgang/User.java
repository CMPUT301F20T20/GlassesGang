package com.example.glassesgang;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * TODO: Currently, database stores attributes fields not objects, will implement Object storage for submission 4
 */
public class User {
    private static String TAG = "User Class";
    public String userName;
    public String email;
    private Object notifications;


    public User(Context context) {
        String filename = context.getResources().getString(R.string.email_account);
        SharedPreferences sharedPref = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        this.email = sharedPref.getString("email", "False");
    }

    public User(){
        // empty constructor for database access
    }
    public User(String email){
        this.email = email;
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
