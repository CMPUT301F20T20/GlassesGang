package com.example.glassesgang;

import com.example.glassesgang.Transaction.Request;
import com.google.android.gms.maps.model.LatLng;

import org.junit.*;
import static org.junit.Assert.*;

public class RequestTest {

    String testBookId;
    String testBorrower;
    String testOwner;
    String testRequestId;
    LatLng testLocation;
    Request request;

    @Before
    public void setUp() throws Exception {
        // initializing testing variables
        testBookId = "testBookId";
        testBorrower = "testBorrower";
        testOwner = "testOwner";
        testRequestId = "testRequestId";
        testLocation = new LatLng(-30, 150);

        // creating a request
        request = new Request(testBookId, testBorrower, testOwner);
        request.setRequestId(testRequestId);
        request.setLocation(testLocation);
    }

    @Test
    public void testSetLocation() {
        LatLng newLocation = new LatLng(-35, 155);
        request.setLocation(newLocation);

        assertEquals(newLocation, request.getLocation());
    }

    @Test
    public void testGetLocation() {
        assertEquals(testLocation, request.getLocation());
    }

    @Test
    public void testGetBookId() {
        assertEquals(testBookId, request.getBookId());
    }

    @Test
    public void testGetBorrowerEmail() {
        assertEquals(testBorrower, request.getBorrowerEmail());
    }

    @Test
    public void testGetOwnerEmail() {
        assertEquals(testOwner, request.getOwnerEmail());
    }

    @Test
    public void testGetRequestId() {
        assertEquals(testRequestId, request.getRequestId());
    }

    @Test
    public void testSetRequestId() {
        String newRequestId = "newRequestId";

        request.setRequestId(newRequestId);
        assertEquals(newRequestId, request.getRequestId());
    }
}
