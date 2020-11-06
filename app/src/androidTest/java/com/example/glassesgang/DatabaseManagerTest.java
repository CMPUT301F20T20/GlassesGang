package com.example.glassesgang;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class DatabaseManagerTest {
    private FirebaseFirestore database;

    @Before
    public void setUp() throws Exception{
        // connect to database
        database = FirebaseFirestore.getInstance();
    }

    @Test
    public void createBookTest(){

    }
    /*
    @Test
    public void addBookTest() {
        Book newbook = new Book("Percy Jackon", "Rick Riodan", "103982", "Test1","chelsea4@ualberta.ca");
        FirebaseFirestore mockFirestore = Mockito.mock(FirebaseFirestore.class)

        DatabaseManager.addBook(newbook, "chelsea4@ualberta.ca");
        database.collection("books").document("Test1").get().getResult();
    }

     */
}
