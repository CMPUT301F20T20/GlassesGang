package com.example.glassesgang;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Owner extends User {
    private String TAG = "User message";
    // Once book object is implemented
    //public ArrayList<Books> catalogue;
    public ArrayList<String> catalogue;
    private FirebaseFirestore db;
    CollectionReference ownerDatabase = db.collection("owners");
    DocumentReference documentReference = ownerDatabase.document(this.email);


    public Owner(FirebaseUser googleUserAccount) {
        super(googleUserAccount);
        this.catalogue = new ArrayList<>();
        addOwnertoDatabase();
    }

    public void addBook(String newBook){
        // just updates catalogue no return value
        this.catalogue.add(newBook);
        addCatalogToDatabase();
    }

    public ArrayList<String> getCatalog() {
        // get data from database
        return catalogue;
    }

    documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        }
    });

// could probably make generic method called add to database
    private void addCatalogToDatabase(){
        Map<String, Object> data1 = new HashMap<>();
        data1.put("catalogue", this.catalogue);
        ownerDatabase.document(userName).set(data1);
    }

    private void addOwnertoDatabase(){
        Map<String, Object> data1 = new HashMap<>();
        data1.put("catalogue", catalogue);
        userDatabase.document(this.email).set(data1);

    }
}
