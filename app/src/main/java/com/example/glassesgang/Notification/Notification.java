package com.example.glassesgang.Notification;

/**
 * TODO: flesh out notification javadocs
 * This is a class that represents the Notification object, allowing for comparison of notifications by bookId
 */
public class Notification {

    public NotificationType getNotification_type() {
        return notification_type;
    }

    public enum NotificationType {
        NEW_REQUEST,
        ACCEPT_REQUEST,
        DECLINE_REQUEST
    }
    
    private String senderName;
    private String receiverEmail;
    private NotificationType notification_type;
    private String body;
    private String popupTitle;
    private String popupText;
    private String notificationId;

    /**
     *  This is the constructor for a Notification object
     *
     * @param nt Type of notification, owner or borrower
     * @param body
     * @param popupTitle
     * @param popupText
     */
    public Notification(String senderName, String receiverEmail, NotificationType nt, String body, String popupTitle, String popupText) {
        this.senderName = senderName;
        this.receiverEmail = receiverEmail;
        this.notification_type = nt;
        this.body = body;
        this.popupTitle = popupTitle;
        this.popupText = popupText;
    }

    //default popup message and message for request notifications
    public Notification(String senderName, String receiverEmail, NotificationType nt, String bookName)
    {
        this.receiverEmail = receiverEmail;
        this.senderName = senderName;
        this.notification_type = nt;
        this.body = getRequestMessage(nt, senderName, bookName);
        this.popupTitle = "New notification!";
        this.popupText = "You have received a new notification from " + senderName;
    }

    private String getRequestMessage(NotificationType nt, String senderName, String bookName) {
        switch(nt) {
            case NEW_REQUEST:
                return senderName + " has made a request to borrow " + bookName;
            case ACCEPT_REQUEST:
                return senderName + " has accepted your request to borrow " + bookName;
            case DECLINE_REQUEST:
                return senderName + " has declined your request to borrow " + bookName;
            default:
                return "default notification message";
        }

    }


    /**
     * This returns the String body, the body message of the notification
     *
     * @return Returns the bookId name
     */
    public String getBody() {
        return body;
    }

    public String getSenderName() { return senderName; }

    public String getReceiverEmail() { return receiverEmail; }

    public void setPopup(String title, String text) {
        this.popupTitle = title;
        this.popupText = text;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public String getPopupTitle() {
        return popupTitle;
    }

    public String getPopupText() {
        return popupText;
    }
}