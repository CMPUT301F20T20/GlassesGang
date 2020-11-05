package com.example.glassesgang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.SearchView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class BorrowerHomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private static final String TAG = "HomeActivity";
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_borrower);

        //setup bottom navigation
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationListener);

        SearchView searchView = (SearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                firebaseSearch(query.toLowerCase());
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //System.out.println(newText);
                return true;
            }

            private void firebaseSearch(String query) {
                System.out.println(query + " submit");
                db = FirebaseFirestore.getInstance();
                CollectionReference bookRef = db.collection("books");
            }

        });

        // added this, to test BorrowerBookProfileActivity -Cholete
        // automatically launches BorrowerBookProfileActivity when user goes to BorrowerHomeActivity.
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        DocumentReference docRef = db.collection("books").document("book2");   // sample document reference
//        Intent bookProfileIntent = new Intent(BorrowerHomeActivity.this, BorrowerBookProfileActivity.class);
//        // OwnerBookProfileActivity is passed a path to the  book document
//        bookProfileIntent.putExtra("bid", "book2");
//        startActivity(bookProfileIntent);


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