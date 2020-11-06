/*
Unit test for Borrower.java class
These test are meant for helping with submission part 4 when owner implementation is finished
 */
package com.example.glassesgang;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class borrowerClassTest {

    @Test
    public void getBorrowerEmailTest(){
        Borrower user = new Borrower("test@gmail.com");
        assertEquals( "test@gmail.com", user.getEmail());
    }

    @Test
    public void getBorrowerCatalogue(){
        Borrower user = new Borrower("test@gmail.com");
        ArrayList<String> expectation = new ArrayList<String>();
        assertEquals(expectation, user.getCatalogue());
    }


    @Test
    public void setBorrowerCatalogue(){
        Borrower user = new Borrower("test@gmail.com");
        ArrayList<String> expectation = new ArrayList<String>();
        expectation.add("BID1");
        expectation.add("BID2");
        user.setCatalogue(expectation);
        assertEquals(expectation, user.getCatalogue());
    }

}

