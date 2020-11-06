/*
Unit test for User.java class
 */

package com.example.glassesgang;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class userClassTest {

    @Test
    public void getUserEmailTest(){
        User user = new User("test@gmail.com");
        assertEquals(user.getEmail(), "test@gmail.com");
    }

    @Test
    public void getUserOwnerCatalogue(){
        User user = new User("test@gmail.com");
        ArrayList<String> expectation = new ArrayList<String>();
        assertEquals(expectation, user.getOwnerCatalogue());
    }

    @Test
    public void getUserBorrowerCatalogue(){
        User user = new User("test@gmail.com");
        ArrayList<String> expectation = new ArrayList<String>();
        assertEquals(expectation, user.getBorrowerCatalogue());
    }

    @Test
    public void setUserOwnerCatalogue(){
        User user = new User("test@gmail.com");
        ArrayList<String> expectation = new ArrayList<String>();
        expectation.add("BID1");
        expectation.add("BID2");
        user.setOwnerCatalogue(expectation);
        assertEquals(expectation, user.getOwnerCatalogue());
    }

    @Test
    public void setUserBorrowerCatalogue(){
        User user = new User("test@gmail.com");
        ArrayList<String> expectation = new ArrayList<String>();
        expectation.add("BID1");
        expectation.add("BID2");
        user.setBorrowerCatalogue(expectation);
        assertEquals(expectation, user.getBorrowerCatalogue());
    }



}
