package com.example.glassesgang;

import com.example.glassesgang.Requests.Request;
import com.example.glassesgang.Requests.RequestList;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RequestListTest {
    private RequestList mockRequestList() {
        RequestList requestList = new RequestList();
        requestList.add(mockRequest());
        return requestList;
    }

    private Request mockRequest() {
        return new Request("12345", "borrower@gmail.com");
    }

    @Test
    public void testAdd() {
        RequestList RequestList = mockRequestList();
        assertEquals(1, RequestList.getRequests().size());
        Request Request = new Request("12345", "borrower@gmail.com");
        RequestList.add(Request);
        assertEquals(2, RequestList.getRequests().size());
        assertTrue(RequestList.getRequests().contains(Request));
    }

    @Test
    public void testAddException() {
        RequestList requestList = mockRequestList();
        Request request = new Request("12345", "borrower@gmail.com");
        requestList.add(request);
        assertThrows(IllegalArgumentException.class, () -> {
            requestList.add(request);
        });
    }

    @Test
    public void testDelete() {
        RequestList requestList = mockRequestList();
        assertEquals(1, requestList.getRequests().size());
        Request request = requestList.getRequests().get(0);
        requestList.delete(request);
        assertEquals(0, requestList.getRequests().size());
        assertTrue(requestList.getRequests().isEmpty());
    }

    @Test
    public void testDeleteException() {
        RequestList requestList = mockRequestList();
        Request request = new Request("12345", "borrower@gmail.com");
        assertThrows(IllegalArgumentException.class, () -> {
            requestList.delete(request);
        });
    }

    @Test
    public void testHasRequest() {
        RequestList requestList = mockRequestList();
        Request request = requestList.getRequests().get(0);
        assertTrue(requestList.hasRequest(request));
        Request request2 = new Request("12345", "borrower@gmail.com");
        assertFalse(requestList.hasRequest(request2));
    }

    @Test
    public void testCountRequests() {
        RequestList requestList = mockRequestList();
        assertEquals(1, requestList.countRequests());
        requestList.add(new Request("12345", "borrower@gmail.com"));
        assertEquals(2, requestList.countRequests());
    }

    @Test
    public void testGetRequests() {
        RequestList requestList = mockRequestList();
        assertEquals(0, mockRequest().compareTo(requestList.getRequests().get(0)));
        Request request = new Request("54321", "borrower@gmail.com");
        requestList.add(request);
        assertTrue((request.compareTo(requestList.getRequests().get(0))) > 0); //first entry less than second
        assertEquals(0, request.compareTo(requestList.getRequests().get(1))); //equal to itself
    }
}
