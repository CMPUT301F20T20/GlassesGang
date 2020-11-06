package com.example.glassesgang.Notification;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a class that keeps track of a list of notification objects
 */
public class NotificationList {
    private List<Notification> notifications = new ArrayList<>();

    /**
     * This adds a notification to the list
     *
     * @param notification This is a candidate notification to add
     */
    public void add(Notification notification) {
        notifications.add(notification);
    }

    /**
     * This removes a notification to the list if the notification does exist
     * Throws an IllegalAccessException if notification does not exists in the NotificationList
     *
     * @param notification This is a candidate notification to add
     */
    public void delete(Notification notification) {
        if (notifications.contains(notification)) {
            notifications.remove(notification);
        }
        else throw new IllegalArgumentException("notification does not exist in list");
    }

    /**
     * This checks whether a given notification exists in the NotificationList
     *
     * @param notification This is a candidate notification to add
     * @return  Return true if notification exists in NotificationList;
     *          Return false if notification does not exist in NotificationList
     */
    public boolean hasNotification(Notification notification) {
        if (notifications.contains(notification)) return true;
        return false;
    }

    /**
     * This returns the size of the NotificationList, denoting how many notifications it contains
     *
     * @return Return integer of the number of notifications within NotificationList
     */
    public int countNotifications() {
        return notifications.size();
    }

    public List<Notification> getNotifications() {
        List<Notification> list = notifications;
        return list;
    }

}