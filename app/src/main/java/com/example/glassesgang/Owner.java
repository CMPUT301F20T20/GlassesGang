package com.example.glassesgang;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
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
    public ArrayList<Book> catalogue;
    private FirebaseFirestore db;
    DocumentReference ownerDatabase = db.collection("users").document(this.email);

    public Owner(Context context) {
        super(context);
        this.catalogue = new ArrayList<Book>();
        addOwnerToDatabase();
    }

    public Owner(){
        super();
        this.catalogue = new ArrayList<>();
    }

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

    public void addBooktoCatalogue(Book newBook){
        this.catalogue.add(newBook);
        ownerDatabase.update("owners", this);
    }

    public void deleteBookFromCatalogue(Book delBook){
        // I'm not sure which to use, this.catalogue or this.getCatalogue...
        this.getCatalogue().remove(delBook);
        ownerDatabase.update("owners", this);
    }

    public void addOwnerToDatabase(){
        ownerDatabase.update("owners", this);
    }

}
