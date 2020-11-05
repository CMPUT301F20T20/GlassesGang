package com.example.glassesgang.Books;

import android.media.Image;
import android.provider.MediaStore;

import java.io.Serializable;
import java.util.ArrayList;

public class Book implements Serializable {
    private String title;
    private String author;
    private String ISBN;
    private String BID;
    private String status;
    private String owner;
    private String borrower;
//    private ArrayList<Image> images;
    //private ArrayList<Request> requestList; //  getter and setter currently commented out but ready to implement as soon as Request exists


    public Book() {
        // no argument constructor for converting a book document snapshot to custom object
    }

    public Book(String title, String author, String ISBN, String BID, String owner) {
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.BID = BID;
        this.owner = owner;
        this.status = "available";
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
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status of the book
     *
     * @param status a string of the status to set the book status to, one of available, requested, accepted, or borrowed
     */
    public void setStatus(String status) {
        this.status = status;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getBorrower() {
        return borrower;
    }

    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }

    /**
     * Gets the images of the book
     *
     * @return an array of images that are attached to the book
     */
//    public ArrayList<Image> getImages() {
//        return images;
//    }
//
//    /**
//     * Sets the images of the book
//     *
//     * @param images an array of the images attached to a book
//     */
//    public void setImages(ArrayList<Image> images) {
//        this.images = images;
//    }

    /**
     * Gets the requests of the book
     *
     * @return an array of requests that are attached to the book
     */
    /*
    public ArrayList<Request> getRequests() {
        return requests;
    }
    */

    /**
     * Sets the requests of the book
     *
     * @param requests an array of the requests attached to a book
     */
    /*
    public void setRequests(ArrayList<Request> requests) {
        this.requests = requests;
    }
    */
}
