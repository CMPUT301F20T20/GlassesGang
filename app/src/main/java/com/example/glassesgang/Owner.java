package com.example.glassesgang;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.json.JSONObject;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Owner {
    // Once book object is implemented
    //public ArrayList<Books> catalogue;
    public ArrayList<String> catalogue;
    public String email;
    //public Object notifications;
    // private FirebaseFirestore db;



    public Owner(Context context) {
        //super(context);
        String filename = context.getResources().getString(R.string.email_account);
        SharedPreferences sharedPref = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        this.email = sharedPref.getString("email", "False");
        this.catalogue = new ArrayList<String>();
        //addOwnerToDatabase();
    }

    public Owner() {
    }

    public String getEmail() {
        return email;
    }



    public void setEmail(String email) {
        this.email = email;
    }



    public void setCatalogue(ArrayList<String> catalogue) {
        this.catalogue = catalogue;
    }


    public ArrayList<String> getCatalogue() {
        return catalogue;
    }




    public void addBooktoCatalogue(String newBook) {
        this.catalogue.add(newBook);
        //userDatabase.document().update("owners", this);
    }

    public void deleteBookFromCatalogue(String delBook) {
        // I'm not sure which to use, this.catalogue or this.getCatalogue...
        this.getCatalogue().remove(delBook);
        //userDatabase.document().update("owners", this);
    }

    public void addOwnerToDatabase() throws JsonProcessingException {
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(this.getEmail())
                .collection("owners").document("ownerObject");
        String json = new ObjectMapper().writeValueAsString(this);
        Map<String, Object> map =
                new ObjectMapper().convertValue(this, new TypeReference<Map<String, Object>>() {});
        documentReference.set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("help12", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("help 13", "Error writing document", e);
                    }
                });
        Log.d("HELP", "add owner to Database");

    }
}

    /* Originally had method to update book objects within catalog

    public ArrayList<Book> getCatalogue() {
        return catalogue;
    }

    @Override
    public FirebaseFirestore getDb() {
        return db;
    }

    public DocumentReference getOwnerDatabase() {
        return ownerDatabase;
    }

    public boolean editCatalogue(Book updatedbook){
        for(Book bookItem : getCatalogue()) {
            if(bookItem.getBID().equals(updatedbook.getBID())) {
                // book found in catalog
                this.catalogue.set(this.catalogue.indexOf(bookItem), updatedbook);
                // writes owner into database
                ownerDatabase.update("owners", this);
            }
            else{
                // book item was not found in database
                return false;
            }
        }
        return true;
    }
    */

