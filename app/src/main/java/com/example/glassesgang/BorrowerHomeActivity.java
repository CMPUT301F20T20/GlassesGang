package com.example.glassesgang;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.os.health.SystemHealthManager;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.SearchView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class BorrowerHomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private static final String TAG = "HomeActivity";
    private ArrayAdapter<Book> bookArrayAdapter;
    private ArrayList<Book> bookArrayList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_borrower);

        // connect to the database
        db = FirebaseFirestore.getInstance();

        // setting up the array adapter
        bookArrayList = new ArrayList<Book>();
        //bookArrayAdapter = new CustomBookList(getActivity(), bookArrayList, userType);

        //setup bottom navigation
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationListener);

        // setting up search view
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
        });

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

    private void firebaseSearch(final String query) {
        System.out.println(query + " submit");
        CollectionReference booksRef = db.collection("books");

        booksRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                bookArrayList.clear();

                for (QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                    // turning doc into book object
                    Book book = doc.toObject(Book.class);
                    book.setBID(doc.getId());

                    // grabbing book description
                    String title = book.getTitle().toLowerCase();
                    String author = book.getAuthor().toLowerCase();
                    String ISBN = book.getISBN().toLowerCase();

                    // filtering through results and adding to list if match
                    if (title.contains(query)) {
                        bookArrayList.add(book);
                    } else if (author.contains(query)){
                        bookArrayList.add(book);
                    } else if (ISBN.contains(query)) {
                        bookArrayList.add(book);
                    }

                    System.out.println(bookArrayList);
                }

                System.out.println("hello " + bookArrayList);
            }
        });
    }

}