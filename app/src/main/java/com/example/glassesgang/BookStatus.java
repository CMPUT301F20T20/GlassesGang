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
                return "AVAILABLE";
            case REQUESTED:
                return "REQUESTED";
            case ACCEPTED:
                return "ACCEPTED";
            case BORROWED:
                return "BORROWED";
        }
        return "";
    }
}
