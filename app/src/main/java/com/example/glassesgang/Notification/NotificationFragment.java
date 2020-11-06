package com.example.glassesgang.Notification;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.glassesgang.R;

import java.util.ArrayList;

public class NotificationFragment extends Fragment {

    private ListView notificationListView;
    private ArrayAdapter<Notification> notificationAdapter;
    private ArrayList<Notification> notificationList;
    final String TAG = "NotificationFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //need to get notifications from db here. using dummy for now

        notificationList = new ArrayList<>();
        notificationList.add(new Notification("test message"));

        notificationAdapter = new NotificationListAdapter(this.getActivity(), notificationList);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_notification, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // setting up the notificationList view
        notificationListView = view.findViewById(R.id.notification_listview);
        notificationListView.setAdapter(notificationAdapter);
    }
}