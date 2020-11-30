package com.example.glassesgang.Transaction;

//import com.google.android.gms.maps.model.LatLng;

/**
 * This is a class that represents the Request object, allowing for comparison of requests by bookId
 */
public class Request {
    private String bookId;
    private String borrowerEmail;
    private String ownerEmail;
    private String requestId;
    private LatLngCustom location;

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

    public Request() {}

    public void setLocation(LatLngCustom location) {
        this.location = location;
    }

    public LatLngCustom getLocation() {
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

}