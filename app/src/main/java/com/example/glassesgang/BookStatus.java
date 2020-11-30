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
            default: return "";
        }
    }

    public static Status statusString(String status) {
        switch (status) {
            case "AVAILABLE":
                return Status.AVAILABLE;
            case "REQUESTED":
                return Status.REQUESTED;
            case "ACCEPTED":
                return Status.ACCEPTED;
            case "BORROWED":
                return Status.BORROWED;
            default: return Status.AVAILABLE;
        }
    }
}
