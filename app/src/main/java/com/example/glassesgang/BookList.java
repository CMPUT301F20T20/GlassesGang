package com.example.glassesgang;

import java.util.ArrayList;

public class BookList {
    private ArrayList<Book> books;

    public BookList() {
        this.books = new ArrayList<>();
    }

    /**
     * Adds a book object to the book list
     * @param book the book to be added
     */
    public void addBook(Book book) {
        books.add(book);
    }

    /**
     * Removes the specified book from the book list
     * @param book the book to be removed
     */
    public void removeBook(Book book) {
        books.remove(book);
    }

    /**
     * returns the whole book lsit
     * @return an ArrayList of all the books
     */
    public ArrayList<Book> getBooks() {
        return books;
    }

    /**
     * clears the whole book list
     */
    public void clearBookList() {
        this.books.clear();
    }
}
