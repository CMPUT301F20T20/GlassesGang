package com.example.glassesgang.Notification;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {

    public static final String CHANNEL_ID = "notificationChannel";
    // name and desc will be shown to user in settings
    public static final String CHANNEL_NAME = "Requests";
    public static final String CHANNEL_DESC = "New and Accepted Requests";
    public static final int CHANNEL_IMPORTANCE = NotificationManager.IMPORTANCE_DEFAULT;
    // creates notification channel at the start of app
    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();
    }

    private void createNotificationChannel() {
        // creates notification channel, importance and other behaviours can't be changed once
        // created
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, CHANNEL_IMPORTANCE);
            channel.setDescription(CHANNEL_DESC);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
