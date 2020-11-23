package com.example.glassesgang;

import com.example.glassesgang.BookStatus.Status;
import android.media.Image;
import android.provider.MediaStore;

import java.io.Serializable;
import java.util.ArrayList;

public class Book implements Serializable {
    private String title;
    private String author;
    private String ISBN;
    private String BID;
    private Status status;
    private String owner;
    private String borrower;
    private String imageUrl;
    private ArrayList<String> requests;


    public Book() {
        // no argument constructor for converting a book document snapshot to custom object
    }

    public Book(String title, String author, String ISBN, String BID, String owner, String imageUrl) {
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.BID = BID;
        this.owner = owner;
        this.borrower = "";
        this.status = Status.AVAILABLE;
        this.requests = new ArrayList<String>();
        this.imageUrl = imageUrl;
    }

    public Book(String title, String author, String ISBN, String owner, String imageUrl) {
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.owner = owner;
        this.status = Status.AVAILABLE;
        this.borrower = "";
        this.imageUrl = imageUrl;
        this.requests = new ArrayList<String>();
    }


    // Getters and Setters for the Class Objects
    /**
     * Gets the title of the book
     *
     * @return a string of the book title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the book
     *
     * @param title a string of the title to set the book title to
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the author of the book
     *
     * @return a string of the book author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets the author of the book
     *
     * @param author a string of the author to set the book author to
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Gets the ISBN of the book
     *
     * @return a string of the book ISBN
     */
    public String getISBN() {
        return ISBN;
    }

    /**
     * Sets the ISBN of the book
     *
     * @param ISBN a string of the ISBN to set the book ISBN to
     */
    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    /**
     * Gets the BID of the book
     *
     * @return a string of the book BID
     */
    public String getBID() {
        return BID;
    }

    /**
     * Sets the BID of the book
     *
     * @param BID a string of the BID to set the BID to
     */
    public void setBID(String BID) {
        this.BID = BID;
    }

    /**
     * Gets the status of the book
     *
     * @return a string of the book status, one of available, requested, accepted, or borrowed
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Sets the status of the book
     *
     * @param status a string of the status to set the book status to, one of available, requested, accepted, or borrowed
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * Get owner of the book
     * @return owner of the book
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Sets the owner of the book
     * @param owner of the book
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * Get who is borrowing the book
     * @return current borrower of the book
     */
    public String getBorrower() {
        return borrower;
    }

    /**
     * Set who is borrowing the book
     * @param borrower of the book
     */
    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }

    /**
     * Gets the images of the book
     *
     * @return an array of images that are attached to the book
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Sets the images of the book
     *
     * @param newImageUrl an array of the images attached to a book
     */
    public void setImageUrl(String newImageUrl) {
        this.imageUrl = newImageUrl;
    }

    /**
     * Gets the requests of the book
     *
     * @return an array of requests that are attached to the book
     */
    public ArrayList<String> getRequests() {
        return requests;
    }


    /**
     * Sets the requests of the book
     *
     * @param requests an array of the requests attached to a book
     */
    public void setRequests(ArrayList<String> requests) {
        this.requests = requests;
    }

}
