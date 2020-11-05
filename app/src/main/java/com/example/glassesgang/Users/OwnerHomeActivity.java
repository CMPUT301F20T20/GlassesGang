package com.example.glassesgang.Users;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.glassesgang.LibraryFragment;
import com.example.glassesgang.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class OwnerHomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_owner);

        //first time launch we want to have library open
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.owner_fragment_container, new LibraryFragment()).commit();
        }

        //setup bottom navigation
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationListener);
        bottomNavigation.setSelectedItemId(R.id.nav_books);
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
            getSupportFragmentManager().beginTransaction().replace(R.id.owner_fragment_container, selectedFragment).commit(); //displays fragment

            return true; //clicked item marked as selected. not selected = false
        }
    };
}