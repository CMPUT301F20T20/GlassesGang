package com.example.glassesgang;

public class BookStatus {
    public enum Status {
        AVAILABLE,
        REQUESTED,
        ACCEPTED,
        BORROWED
    }

    public static String stringStatus(Status status) {
        switch(status) {
            case AVAILABLE:
                return "Available";
            case REQUESTED:
                return "Requested";
            case ACCEPTED:
                return "Accepted";
            case BORROWED:
                return "Declined";
        }
        return "";
    }
}
