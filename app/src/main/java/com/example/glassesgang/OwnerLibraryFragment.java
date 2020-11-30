package com.example.glassesgang;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * Fragment for showing books that user is currently borrowing,
 * requesting, or have an accepted request from the owner
 */
public class OwnerLibraryFragment extends Fragment {
    private ListView bookListView;
    private ArrayAdapter<Book> bookArrayAdapter;
    private ArrayList<Book> bookArrayList;
    private ArrayList<String> bidList; // list containing the bids of books that the owner owns
    private ToggleButton availableTogButton;
    private ToggleButton requestedTogButton;
    private ToggleButton acceptedTogButton;
    private ToggleButton borrowedTogButton;
    private String user;
    final String TAG = "OwnerLibraryFragment";
    private FirebaseFirestore db;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get user email
        String filename = getResources().getString(R.string.email_account);
        SharedPreferences sharedPref = getActivity().getSharedPreferences(filename, Context.MODE_PRIVATE);
        user = sharedPref.getString("email", "null");
        if (user == "null") {
            Log.e("Email","No user email recorded");
        }

        // connect to the database
        db = FirebaseFirestore.getInstance();

        // setting up the array adapter
        bookArrayList = new ArrayList<Book>();
        bookArrayAdapter = new CustomBookList(getActivity(), bookArrayList, "o");

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v =  inflater.inflate(R.layout.owner_library_fragment, container, false);

        return v;
    }


    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpFilter(view, (CustomBookList) bookArrayAdapter);

        // setting up the bookList view
        bookListView = view.findViewById(R.id.library_list_view);
        bookListView.setAdapter(bookArrayAdapter);

        DocumentReference userRef = db.collection("users").document(user);   // get user reference from db

        // display owner catalogue using a listview
        userRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data 1: " + snapshot.getData());
                    bidList = new ArrayList<>();
                    bidList = (ArrayList<String>) snapshot.get("ownerCatalogue");  // set the bidList
                    updateListView();  // update ListView to display books whose bid are in the bidList
                } else {
                    Log.d(TAG, "Current data 2: null");
                }
            }
        });

        // Setting an onItemClick listener for the listview of books
        // When owner taps on a book in the list view, they are sent to that book's profile
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // get the book id of the book that was pressed
                Book book = (Book) adapterView.getItemAtPosition(i);
                String bid = book.getBID();

                // get book reference from db
                DocumentReference bookRef = db.collection("books").document(bid);

                // open up the appropriate book profile
                Intent bookProfileIntent = new Intent(getActivity(), OwnerBookProfileActivity.class);
                bookProfileIntent.putExtra("bid", bid);
                startActivity(bookProfileIntent);
            }
        });
        

    }

    /**
     * Updates the list view with real-time updates
     */
    private void updateListView() {
        // get a reference for the books collection
        CollectionReference booksRef = db.collection("books");

        booksRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                bookArrayList.clear();
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                for (QueryDocumentSnapshot doc : value) {
                    // if a book's bid is in bidList, add it to the bookArrayList to be displayed
                    if (bidList.contains(doc.getId())) {
                        Book book = doc.toObject(Book.class);
                        bookArrayList.add(book);
                    }
                }
                //TODO: fix bug where books you own only appear for a split second
                bookArrayAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched from the cloud
                bookArrayAdapter.getFilter().filter("");  // refilter the new list of books
            }
        });
    }

    /**
     * Set up filter for book statuses
     * @param view corresponding view where widget elements are located
     * @param adapter object to updates the fragment
     */
    private void setUpFilter(View view, CustomBookList adapter) {
        // setting up the buttons
        availableTogButton = view.findViewById(R.id.availableToggleButton);
        requestedTogButton = view.findViewById(R.id.requestedToggleButton);
        acceptedTogButton = view.findViewById(R.id.acceptedToggleButton);
        borrowedTogButton = view.findViewById(R.id.borrowedToggleButton);

        // setting up listeners
        availableTogButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    adapter.updateFilter("AVAILABLE", 1);
                    adapter.getFilter().filter("");
                } else {
                    adapter.updateFilter("AVAILABLE", 0);
                    adapter.getFilter().filter("");
                }
            }
        });

        requestedTogButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    adapter.updateFilter("REQUESTED", 1);
                    adapter.getFilter().filter("");
                } else {
                    adapter.updateFilter("REQUESTED", 0);
                    adapter.getFilter().filter("");
                }
            }
        });

        acceptedTogButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    adapter.updateFilter("ACCEPTED", 1);
                    adapter.getFilter().filter("");
                } else {
                    adapter.updateFilter("ACCEPTED", 0);
                    adapter.getFilter().filter("");
                }
            }
        });

        borrowedTogButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    adapter.updateFilter("BORROWED", 1);
                    adapter.getFilter().filter("");
                } else {
                    adapter.updateFilter("BORROWED", 0);
                    adapter.getFilter().filter("");
                }
            }
        });


    }
}
