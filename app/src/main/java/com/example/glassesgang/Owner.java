package com.example.glassesgang;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

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

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Owner extends User {
    // Once book object is implemented
    //public ArrayList<Books> catalogue;
    public ArrayList<String> catalogue;
    //private FirebaseFirestore db;
    //DocumentReference ownerDatabase = FirebaseFirestore.getInstance().collection("users").document(this.email);
    CollectionReference ownerDatabase = this.userDatabase.document(this.email)
            .collection("owners");

    public Owner(Context context) {
        super(context);
        this.catalogue = new ArrayList<String>();
        //addOwnerToDatabase();
    }

    public Owner() {
    }

    public void setCatalogue(ArrayList<String> catalogue) {
        this.catalogue = catalogue;
    }

    public void setOwnerDatabase(CollectionReference ownerDatabase) {
        this.ownerDatabase = ownerDatabase;
    }

    public ArrayList<String> getCatalogue() {
        return catalogue;
    }

    public CollectionReference getOwnerDatabase() {
        return ownerDatabase;
    }


    public void addBooktoCatalogue(String newBook) {
        this.catalogue.add(newBook);
        ownerDatabase.document().update("owners", this);
    }

    public void deleteBookFromCatalogue(String delBook) {
        // I'm not sure which to use, this.catalogue or this.getCatalogue...
        this.getCatalogue().remove(delBook);
        ownerDatabase.document().update("owners", this);
    }

    public void addOwnerToDatabase() {
        ownerDatabase.document().set(this)
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

