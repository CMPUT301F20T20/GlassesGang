package com.example.glassesgang;

import android.media.Image;
import android.provider.MediaStore;

import java.util.ArrayList;

public class Book {
    private String title;
    private String author;
    private String ISBN;
    private String BID;
    private String status;
    private ArrayList<Image> images;
    //private ArrayList<Request> requestList;
    // Also implement a getter and setter for the requestList


    public Book(String title, String author, String ISBN, String BID, String status) {
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.BID = BID;
        this.status = status;
    }

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
     * @param title a string containing the title to set the book title to
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
     * @param author a string containing the author to set the book author to
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
     * @param ISBN a string containing the ISBN to set the book ISBN to
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
     * @param BID a string containing the BID to set the BID to
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
     * @param status a string containing the status to set the BID to, one of available, requested, accepted, or borrowed
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets the images of the book
     *
     * @return an array of images that are attached to the book
     */
    public ArrayList<Image> getImages() {
        return images;
    }

    /**
     * Sets the images of the book
     *
     * @param images a string containing the status to set the BID to, one of available, requested, accepted, or borrowed
     */
    /**  for the images I was curious about if rather than setImages it would be a method that would add images into the array
    public void setImages(ArrayList<Image> images) {
        this.images = images;
    }
     */
}
