package com.example.glassesgang.Notification;

/**
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

    public Notification() {}

    /**
     *  This is the constructor for a Notification object
     *
     * @param nt Type of notification, owner or borrower
     * @param body content of the notification message
     * @param popupTitle title of that the user sees when phone gets alerted
     * @param popupText text body the user sees when phone gets alerted
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

    /**
     * This returns the message for the notification depending
     * on the notification type
     *
     * @return the notification content
     */
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
     * @return the bookId name
     */
    public String getBody() {
        return body;
    }

    /**
     * This returns the email of the user triggering the notification
     *
     * @return the user email of notification sender
     */
    public String getSenderName() { return senderName; }

    /**
     * This returns the email of the user receiving the notification
     *
     * @return the user email of notification receiver
     */
    public String getReceiverEmail() { return receiverEmail; }

    /**
     * This sets the message the user sees when their phone is alerted
     * of a notification
     */
    public void setPopup(String title, String text) {
        this.popupTitle = title;
        this.popupText = text;
    }

    /**
     * This sets the notificationId
     */
    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    /**
     * This returns the notificationId
     *
     * @return the notification ID
     */
    public String getNotificationId() {
        return notificationId;
    }

    /**
     * This returns the title of the notification alert
     *
     * @return notification title
     */
    public String getPopupTitle() {
        return popupTitle;
    }

    /**
     * This returns the notification body
     *
     * @return notification body
     */
    public String getPopupText() {
        return popupText;
    }
}