package com.example.glassesgang.Requests;

public class Request {
    public final String bookId;
    public final String email;

    public Request(String id, String content) {
        this.bookId = id;
        this.email = content;
    }

}
