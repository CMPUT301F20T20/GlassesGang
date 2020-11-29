package com.example.glassesgang;

import com.example.glassesgang.Notification.Notification;
import com.example.glassesgang.Transaction.RequestReference;

import org.junit.*;
import static org.junit.Assert.*;

public class RequestReferenceTest {

    RequestReference requestReference;
    BookStatus.Status testBookStatus;
    String testBookId;

    @Before
    public void setUp() throws Exception {
        // initialize testing variables
        testBookStatus = BookStatus.Status.valueOf("AVAILABLE");
        testBookId = "testBookId";

        requestReference = new RequestReference(testBookStatus, testBookId);
    }

    @Test
    public void testSetRequestRefStatus() {
        BookStatus.Status newBookStatus = BookStatus.Status.valueOf("REQUESTED");
        requestReference.setRequestRefStatus(newBookStatus);

        assertEquals(newBookStatus, requestReference.getRequestRefStatus());
    }

    @Test
    public void testSetRequestRefId() {
        String newTestBookId = "newTestBookId";
        requestReference.setRequestRefId(newTestBookId);

        assertEquals(newTestBookId, requestReference.getRequestRefId());
    }

    @Test
    public void testGetRequestRefStatus() {
        assertEquals(testBookStatus, requestReference.getRequestRefStatus());
    }

    @Test
    public void testGetRequestRefId() {
        assertEquals(testBookId, requestReference.getRequestRefId());
    }

}
