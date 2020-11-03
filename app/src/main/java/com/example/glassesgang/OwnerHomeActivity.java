package com.example.glassesgang;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class OwnerHomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_owner);


        //setup bottom navigation
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationListener);
        bottomNavigation.setSelectedItemId(R.id.nav_books);

        // added this, to test OwnerBookProfileActivity -Cholete
        // automatically launches OwnerBookProfileActivity when user goes to OwnerHomeActivity.
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        DocumentReference docRef = db.collection("books").document("book3");   // sample document reference
//        Intent bookProfileIntent = new Intent(OwnerHomeActivity.this, OwnerBookProfileActivity.class);
//        bookProfileIntent.putExtra("bid", "book3");  // pass book id
//        startActivity(bookProfileIntent);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

                switch(item.getItemId()) {
                    case R.id.nav_books:
                        selectedFragment = new LibraryFragment();
                        break;
                    case R.id.nav_notifications:
                        //implement fragment:
                        break;
                    case R.id.nav_user:
                        // send current user to position 0 (Owner) to fragment
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