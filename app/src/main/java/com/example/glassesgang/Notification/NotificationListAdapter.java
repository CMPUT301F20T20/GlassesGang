package com.example.glassesgang.Notification;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.glassesgang.R;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.annotation.Nullable;

public class NotificationListAdapter extends ArrayAdapter<Notification> {

    private ArrayList<Notification> notifications;
    private Context context;
    private String TAG = "Notification properties";
    private FirebaseFirestore db;

    public NotificationListAdapter(Context context, ArrayList<Notification> notifications) {
        super(context, 0, notifications);
        this.notifications = notifications;
        this.context = context;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.notification, parent, false);
        }

        Notification notification = notifications.get(position);

        TextView notificationMessage = view.findViewById(R.id.notification_text);
        notificationMessage.setText(notification.getBody());

        return view;
    }

}