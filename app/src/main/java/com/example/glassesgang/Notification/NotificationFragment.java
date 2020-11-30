package com.example.glassesgang.Notification;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.glassesgang.Book;
import com.example.glassesgang.BorrowerBookProfileActivity;
import com.example.glassesgang.CustomBookList;
import com.example.glassesgang.DatabaseManager;
import com.example.glassesgang.OwnerBookProfileActivity;
import com.example.glassesgang.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotificationFragment extends Fragment {

    private ListView notificationListView;
    private ArrayAdapter<Notification> notificationAdapter;
    private ArrayList<Notification> notificationList;
    private String user;
    final String TAG = "NotificationFragment";
    private Button sendNotification;
    private NotificationManagerCompat notificationManagerCompat;
    private static FirebaseFirestore db;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get user
        String filename = getResources().getString(R.string.email_account);
        SharedPreferences sharedPref = getActivity().getSharedPreferences(filename, Context.MODE_PRIVATE);
        user = sharedPref.getString("email", "null");

        if (user == "null") {
            Log.e("Email","No user email recorded");
        }

        // connect to the database
        db = FirebaseFirestore.getInstance();

        // setting up the array adapter
        notificationList = new ArrayList<Notification>();
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

        //display notifications TODO: check if notifications are displayed in order
        DocumentReference userRef = db.collection("users").document(user);
        userRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed. ", e);
                    return;
                }
                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data 1: " + snapshot.getData());
                    ArrayList<String> catalogue = (ArrayList<String>) snapshot.get("notificationCatalogue");
                    updateListView(catalogue, view);
                } else {
                    Log.d(TAG, "Current data 2: null");
                }
            }
        });

        notificationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // get the book id of the book that was pressed
                Notification not = (Notification) adapterView.getItemAtPosition(i);
                String notId = not.getNotificationId();

                // get notification reference from db
                DocumentReference notRef = db.collection("notifications").document(notId);

                // delete this notification
                DatabaseManager dbm = new DatabaseManager();
                dbm.deleteNotification(not);
            }
        });
    }

    private void updateListView(final ArrayList<String> catalogue, View v) {
        CollectionReference notificationsRef = db.collection("notifications");

        notificationsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                notificationList.clear();

                for (QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                    // if a book is in the catalogue, add it to the bookArrayList for the bookListView to be displayed.
                    if (catalogue.contains(doc.getId())) {
                        Notification notification = doc.toObject(Notification.class);
                        //notification.setNotificationId(doc.getId()); //might not need this as it is already set upon db write
                        notificationList.add(notification);
                    }
                }

                notificationAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched from the cloud
            }

        });
    }
}