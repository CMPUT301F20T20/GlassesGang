package com.example.glassesgang.Requests;
import java.util.UUID;

/**
 * This is a class that represents the Request object, allowing for comparison of requests by bookId
 */
public class Request implements Comparable<Request>{
    private String bookId;
    private String email;
    private String requestId;

    /**
     * This is the constructor for a Request object
     *
     * @param bookId This is the name of the new Request object
     * @param email This is the email of the borrower making the request
     */
    public Request(String bookId, String email) {
        this.bookId = bookId;
        this.email = email;
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
     * This returns the String email, the bookId's email
     *
     * @return Returns the bookId email
     */
    public String getEmail() {
        return email;
    }

    /**
     * This returns the String requestId
     * @return
     */
    public String getRequestId() {
        return this.requestId;
    }

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
