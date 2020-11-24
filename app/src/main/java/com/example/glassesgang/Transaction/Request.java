package com.example.glassesgang.Transaction;

import com.google.android.gms.maps.model.LatLng;

/**
 * This is a class that represents the Request object, allowing for comparison of requests by bookId
 */
public class Request implements Comparable<Request>{
    private String bookId;
    private String borrowerEmail;
    private String ownerEmail;
    private String requestId;
    private LatLng location;

    /**
     * This is the constructor for a Request object
     *
     * @param bookId This is the name of the new Request object
     * @param borrowerEmail This is the email of the borrower making the request
     * @param borrowerEmail This is the email of the owner receiving the request
     */
    public Request(String bookId, String borrowerEmail, String ownerEmail) {
        this.bookId = bookId;
        this.borrowerEmail = borrowerEmail;
        this.ownerEmail = ownerEmail;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public LatLng getLocation() {
        return location;
    }

    /**
     * This returns the String bookId, the bookId's name
     *
     * @return Returns the bookId name
     */
    public String getBookId() {
        return bookId;
    }

    /**
     * This returns the borrower email
     *
     * @return Returns the String borrowerEmail
     */
    public String getBorrowerEmail() {
        return this.borrowerEmail;
    }

    /**
     * This returns the owner email
     *
     * @return Returns the String ownerEmail
     */
    public String getOwnerEmail() {
        return this.ownerEmail;
    }

    /**
     * This returns the String requestId
     * @return
     */
    public String getRequestId() {
        return this.requestId;
    }

    public void setRequestId(String requestId) { this.requestId = requestId; }

    /**
     * This is an override of compareTo() method which compares two strings by the Unicode value of each character in the strings
     * This method runs the compareTo method on the bookId string of this Request object with the bookId string of another Request object
     *
     * @param request This is the Request object which we are comparing to
     * @return  Returns 0 if the strings are equal;
     *          Returns a negative integer if the bookId string is less than the given bookId's bookId string;
     *          Returns a positive integer if the bookId string is greater than the given bookId's bookId string.
     */
    @Override
    public int compareTo(Request request) {
        return this.bookId.compareTo(request.getBookId());
    }
}
