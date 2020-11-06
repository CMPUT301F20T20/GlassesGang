package com.example.glassesgang;

import com.example.glassesgang.Notification.Notification;
import com.example.glassesgang.Notification.NotificationList;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NotificationListTest {
    private NotificationList mockNotificationList() {
        NotificationList notificationList = new NotificationList();
        notificationList.add(mockNotification());
        return notificationList;
    }

    private Notification mockNotification() {
        return new Notification("borrower@gmail.com has requested your book");
    }

    @Test
    public void testAdd() {
        NotificationList NotificationList = mockNotificationList();
        assertEquals(1, NotificationList.getNotifications().size());
        Notification Notification = new Notification("borrower@gmail.com has requested your book");
        NotificationList.add(Notification);
        assertEquals(2, NotificationList.getNotifications().size());
        assertTrue(NotificationList.getNotifications().contains(Notification));
    }

    @Test
    public void testDelete() {
        NotificationList notificationList = mockNotificationList();
        assertEquals(1, notificationList.getNotifications().size());
        Notification notification = notificationList.getNotifications().get(0);
        notificationList.delete(notification);
        assertEquals(0, notificationList.getNotifications().size());
        assertTrue(notificationList.getNotifications().isEmpty());
    }

    @Test
    public void testDeleteException() {
        NotificationList notificationList = mockNotificationList();
        Notification notification = new Notification("borrower@gmail.com has requested your book");
        assertThrows(IllegalArgumentException.class, () -> {
            notificationList.delete(notification);
        });
    }

    @Test
    public void testHasNotification() {
        NotificationList notificationList = mockNotificationList();
        Notification notification = notificationList.getNotifications().get(0);
        assertTrue(notificationList.hasNotification(notification));
        Notification notification2 = new Notification("borrower@gmail.com has requested your book");
        assertFalse(notificationList.hasNotification(notification2));
    }

    @Test
    public void testCountNotifications() {
        NotificationList notificationList = mockNotificationList();
        assertEquals(1, notificationList.countNotifications());
        notificationList.add(new Notification("borrower@gmail.com has requested your book"));
        assertEquals(2, notificationList.countNotifications());
    }

}