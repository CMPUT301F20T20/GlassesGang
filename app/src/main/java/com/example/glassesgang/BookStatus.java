package com.example.glassesgang;

public class BookStatus {
    public enum Status {
        AVAILABLE,
        PENDING,
        ACCEPTED,
        DECLINED
    }

    public static String stringStatus(Status status) {
        switch(status) {
            case AVAILABLE:
                return "Available";
            case PENDING:
                return "Pending";
            case ACCEPTED:
                return "Accepted";
            case DECLINED:
                return "Declined";
        }
        return "";
    }
}
