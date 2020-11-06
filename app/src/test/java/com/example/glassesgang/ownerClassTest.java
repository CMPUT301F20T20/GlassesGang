/*
Unit test for Owner.java class
These test are meant for helping with submission part 4 when owner implementation is finished
 */

package com.example.glassesgang;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ownerClassTest {
    
    @Test
    public void getOwnerEmailTest(){
        Owner user = new Owner("test@gmail.com");
        assertEquals(user.getEmail(), "test@gmail.com");
    }

    @Test
    public void getOwnerCatalogue(){
        Owner user = new Owner("test@gmail.com");
        ArrayList<String> expectation = new ArrayList<String>();
        assertEquals(expectation, user.getOwnerCatalogue());
    }


    @Test
    public void setOwnerCatalogue(){
        Owner user = new Owner("test@gmail.com");
        ArrayList<String> expectation = new ArrayList<String>();
        expectation.add("BID1");
        expectation.add("BID2");
        user.setOwnerCatalogue(expectation);
        assertEquals(expectation, user.getOwnerCatalogue());
    }

}

