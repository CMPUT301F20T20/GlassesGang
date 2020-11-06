package com.example.glassesgang.Requests;

public class Request {
    private String bookId;
    private String email;

    public Request(String bookId, String email) {
        this.bookId = bookId;
        this.email = email;
    }

    public String getBookId() {
        return bookId;
    }

    public String getEmail() {
        return email;
    }
}
