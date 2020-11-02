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
import android.widget.ExpandableListView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class LibraryFragment extends Fragment {
    private ListView bookListView;
    private ArrayAdapter<Book> bookArrayAdapter;
    private ArrayList<Book> bookArrayList;
    private String user;
    private String userType; // "o" = owner ; "b" = borrower
    final String TAG = "LibraryFragment";
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set the user type
        if (getActivity() instanceof OwnerHomeActivity) {
            userType = "o";
        } else if (getActivity() instanceof BorrowerHomeActivity) {
            userType = "b";
        } else {
            // clean this up later
            try {
                throw new Exception("library fragment can only be launched by Owner or Borrower HomeActivity");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // get user
        String filename = getResources().getString(R.string.email_account);
        SharedPreferences sharedPref = getActivity().getSharedPreferences(filename, Context.MODE_PRIVATE);
        user = sharedPref.getString("email", "default value");
        Log.d("user-email", user);

        // connect to the database
        db = FirebaseFirestore.getInstance();

        // setting up the array adapter
        bookArrayList = new ArrayList<Book>();
        bookArrayAdapter = new CustomBookList(getActivity(), bookArrayList, userType);

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v =  inflater.inflate(R.layout.library_fragment, container, false);

        return v;
    }


    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // setting up the bookList view
        bookListView = (ListView) view.findViewById(R.id.library_list_view);
        bookListView.setAdapter(bookArrayAdapter);

        DocumentReference userRef = db.collection("users").document(user);   // get user reference from db

        // display either owner or borrower catalogue
        if (userType.equals("o")) {
            // display owner catalogue
            userRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot,
                                    @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }

                    if (snapshot != null && snapshot.exists()) {
                        Log.d(TAG, "Current data: " + snapshot.getData());
                        ArrayList<String> catalogue = (ArrayList<String>) snapshot.get("ownerCatalogue");
                        updateListView(catalogue, view);
                    } else {
                        Log.d(TAG, "Current data: null");
                    }
                }
            });
        }
        if (userType.equals("b")) {
            // display borrower catalogue
            userRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot,
                                    @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }

                    if (snapshot != null && snapshot.exists()) {
                        Log.d(TAG, "Current data: " + snapshot.getData());
                        ArrayList<String> catalogue = (ArrayList<String>) snapshot.get("borrowerCatalogue");
                        updateListView(catalogue, view);
                    } else {
                        Log.d(TAG, "Current data: null");
                    }
                }
            });
        }

        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // get the book id of the book that was pressed
                Book book = (Book) adapterView.getItemAtPosition(i);
                String bid = book.getBID();

                // get book reference from db
                DocumentReference bookRef = db.collection("books").document(bid);

                // open up the appropriate book profile
                Intent bookProfileIntent;
                if (userType.equals("o")) {
                    bookProfileIntent = new Intent(getActivity(), OwnerBookProfileActivity.class);
                    bookProfileIntent.putExtra("bid", bid);
                    startActivity(bookProfileIntent);
                }
                if (userType.equals("b")) {
                    bookProfileIntent = new Intent(getActivity(), BorrowerBookProfileActivity.class);
                    bookProfileIntent.putExtra("bid", bid);
                    startActivity(bookProfileIntent);
                }
            }
        });
        

    }

    private void updateListView(final ArrayList<String> catalogue, View v) {
        CollectionReference booksRef = db.collection("books");

        booksRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                bookArrayList.clear();

                for (QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                    // if a book is in the catalogue, add it to the bookArrayList for the bookListView to be displayed.
                    if (catalogue.contains(doc.getId())) {
                        Book book = doc.toObject(Book.class);
                        book.setBID(doc.getId());
                        bookArrayList.add(book);
                    }
                }

                bookArrayAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched from the cloud
            }
        });
    }
}
