package com.example.glassesgang.Notification;

/**
 * This is a class that represents the Notification object, allowing for comparison of notifications by bookId
 */
public class Notification {
    private String message;

    /**
     * This is the constructor for a Notification object
     *
     * @param message This is the text displayed in a new Notification object
     */
    public Notification(String message) {
        this.message = message;
    }

    /**
     * This returns the String bookId, the bookId's name
     *
     * @return Returns the bookId name
     */
    public String getMessage() {
        return message;
    }

}