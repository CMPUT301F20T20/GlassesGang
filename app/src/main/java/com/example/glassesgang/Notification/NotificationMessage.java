package com.example.glassesgang.Notification;

public class NotificationMessage {
    private Message messageEnum;
    private String senderName;
    private String bookName;
    private String finalMessage;

    public enum Message {
        NEW_REQUEST,
        ACCEPT_REQUEST,
        DECLINE_REQUEST
    }

    public NotificationMessage(Message message, String senderName, String bookName)
    {
        this.messageEnum = message;
        this.senderName = senderName;
        this.bookName = bookName;
        this.finalMessage = getFinalMessage(message, senderName, bookName);

    }

    private String getFinalMessage(Message message, String senderName, String bookName) {
        switch(message) {
            case NEW_REQUEST:
                return senderName + " has made a request to borrow " + bookName;
            case ACCEPT_REQUEST:
                return senderName + " has accepted your request to borrow " + bookName;
            case DECLINE_REQUEST:
                return senderName + " has declined your request to borrow " + bookName;
        }
    }

}
