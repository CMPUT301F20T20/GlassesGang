package com.example.glassesgang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;

import android.view.MenuItem;
import android.widget.SearchView;

import com.example.glassesgang.Notification.NotificationFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.example.glassesgang.browse.BrowseFragment;

/**
 * Home Activity for Borrower
 */
public class BorrowerHomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_borrower);

        //setup bottom navigation
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationListener);

        bottomNavigation.setSelectedItemId(R.id.nav_home);
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
                    selectedFragment = new BorrowerLibraryFragment();
                    break;
                case R.id.nav_notifications:
                    selectedFragment = new NotificationFragment();
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