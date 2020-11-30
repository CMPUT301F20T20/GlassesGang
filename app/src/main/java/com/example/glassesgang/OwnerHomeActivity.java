package com.example.glassesgang;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import com.example.glassesgang.Notification.Notification;
import com.example.glassesgang.Notification.NotificationFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.example.glassesgang.Notification.App;


/**
 * Home Activity for Owner
 */
public class OwnerHomeActivity extends AppCompatActivity {
    private ImageButton addButton;
    private FirebaseFirestore db;


    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_owner);

        //first time launch we want to have library open
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new OwnerLibraryFragment()).commit();
        }

        //setup bottom navigation
        addButton = findViewById(R.id.add_button);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationListener);
        bottomNavigation.setSelectedItemId(R.id.nav_books);


        // go to add screen once user presses the add button
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addBookIntent = new Intent(OwnerHomeActivity.this, AddBookActivity.class);
                startActivity(addBookIntent);
            }
        });

        final String TAG = "Listener";
        db = FirebaseFirestore.getInstance();
        CollectionReference notificationsRef = db.collection("notifications");
        notificationsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                    if ( dc.getDocument().get("receiverEmail") != null && ((String)dc.getDocument().get("receiverEmail")).equals("mockuser2@gmail.com")) {
                        switch (dc.getType()) {
                            case ADDED:
                                Toast.makeText(getApplicationContext(), "NEW NOTIFICATION", Toast.LENGTH_SHORT).show();
//                                Log.d(TAG, "New city: " + dc.getDocument().getData());
//
//                                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),
//                                        App.CHANNEL_ID)
//                                        .setSmallIcon(R.drawable.ic_baseline_notifications_24)
//                                        .setContentTitle((String)dc.getDocument().get("popupTitle"))
//                                        .setContentText((String)dc.getDocument().get("popupText"));
//                                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
//                                notificationManagerCompat.notify(1, builder.build());
                                break;
                            case MODIFIED:
                                Toast.makeText(getApplicationContext(), "MODIFIED NOTIFICATION", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "Modified city: " + dc.getDocument().getData());
                                break;
                            case REMOVED:
                                Toast.makeText(getApplicationContext(), "REMOVED NOTIFICATION", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "Removed city: " + dc.getDocument().getData());
                                break;
                        }
                    }
                }
            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

                switch(item.getItemId()) {
                    case R.id.nav_books:
                        addButton.setVisibility(View.VISIBLE);
                        selectedFragment = new OwnerLibraryFragment();
                        break;
                    case R.id.nav_notifications:
                        addButton.setVisibility(View.GONE);
                        //implement fragment:
                        selectedFragment = new NotificationFragment();
                        break;
                    case R.id.nav_user:
                        // send current user to position 0 (Owner) to fragment
                        addButton.setVisibility(View.GONE);
                        Bundle bundle = new Bundle();
                        bundle.putInt("currentUser", 0);
                        selectedFragment = new UserProfileFragment();
                        selectedFragment.setArguments(bundle);
                        break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit(); //displays fragment

            return true; //clicked item marked as selected. not selected = false
        }
    };
}