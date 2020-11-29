package com.example.glassesgang;

import com.example.glassesgang.Notification.Notification;

import org.junit.*;
import static org.junit.Assert.*;

public class NotificationTest {

    Notification notification;
    String testNotificationId;
    String testSender;
    String testReceiver;
    String testBook;
    String defaultTitle;
    String defaultText;

    @Before
    public void setUp() throws Exception {
        // initialize testing variables
        testNotificationId= "testId";
        testSender = "testSender";
        testReceiver = "testReceiver";
        testBook = "testBook";
        defaultTitle = "New notification!";
        defaultText = "You have received a new notification from " + testSender;

        // create notification type for testing and creating a notification
        Notification.NotificationType testNotificationType = Notification.NotificationType
                .valueOf("NEW_REQUEST");
        notification = new Notification(testSender, testReceiver, testNotificationType,
                testBook);
        notification.setNotificationId(testNotificationId);
    }

    @Test
    public void testGetBody() {
        assertEquals(testSender + " has made a request to borrow " + testBook,
                notification.getBody());
    }

    @Test
    public void testGetSenderName() {
        assertEquals(testSender, notification.getSenderName());
    }

    @Test
    public void testGetReceiverEmail() {
        assertEquals(testReceiver, notification.getReceiverEmail());
    }

    @Test
    public void testSetNotificationId() {
        notification.setNotificationId("newNotificationId");

        assertEquals("newNotificationId", notification.getNotificationId());
    }

    @Test
    public void testGetNotificationId() {
        assertEquals(testNotificationId, notification.getNotificationId());
    }

    @Test
    public void testGetPopupTitle() {
        assertEquals(defaultTitle, notification.getPopupTitle());
    }

    @Test
    public void testGetPopupText() {
        assertEquals(defaultText, notification.getPopupText());
    }
}
