package com.example.glassesgang;

import java.util.ArrayList;

public class BookList {
    private ArrayList<Book> books;

    public BookList() {
        this.books = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void removeBook(Book book) {
        books.remove(book);
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public void clearBookList() {
        this.books.clear();
    }
}
