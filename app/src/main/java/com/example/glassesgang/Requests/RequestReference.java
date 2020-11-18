package com.example.glassesgang.Requests;

import com.example.glassesgang.BookStatus.Status;

public class RequestReference {
    private Status bookStatus;
    private String requestId;

    public RequestReference(Status bookStatus, String requestId) {
        this.bookStatus = bookStatus;
        this.requestId = requestId;
    }

    public void setRequestRefStatus(Status bookStatus) {
        this.bookStatus = bookStatus;
    }

    public void setRequestRefId(String requestId) {
        this.requestId = requestId;
    }

    public Status getRequestRefStatus() {
        return this.bookStatus;
    }

    public String getRequestRefId() {
        return this.requestId;
    }

}
