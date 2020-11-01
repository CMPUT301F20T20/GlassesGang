package com.example.glassesgang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class BorrowerHomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private static final String TAG = "HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_borrower);

        //setup bottom navigation
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationListener);

        // added this, to test OwnerBookProfileActivity -Cholete
        // automatically launches OwnerBookProfileActivity when user goes to OwnerHomeActivity.
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("books").document("book4");   // sample document reference
        Intent bookProfileIntent = new Intent(BorrowerHomeActivity.this, BorrowerBookProfileActivity.class);
        // OwnerBookProfileActivity is passed a path to the  book document
        bookProfileIntent.putExtra("path", docRef.getPath());
        startActivity(bookProfileIntent);


    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch(item.getItemId()) {
                case R.id.nav_home:
                    selectedFragment = new BrowseFragment();
                    break;
                case R.id.nav_books:
                    //implement fragment, random fragment for testing purposes
                    selectedFragment = new LibraryFragment();
                    break;
                case R.id.nav_notifications:
                    //implement fragment:
                    break;
                case R.id.nav_user:
                    // send current user to position 1 (Borrower) to fragment
                    Bundle bundle = new Bundle();
                    bundle.putInt("currentUser", 1);
                    selectedFragment = new UserProfileFragment();
                    selectedFragment.setArguments(bundle);
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit(); //displays fragment

            return true; //clicked item marked as selected. not selected = false
        }
    };
}