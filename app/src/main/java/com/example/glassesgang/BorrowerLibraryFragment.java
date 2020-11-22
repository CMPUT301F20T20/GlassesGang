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
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class BorrowerLibraryFragment extends Fragment {
    private ListView bookListView;
    private ArrayAdapter<Book> bookArrayAdapter;
    private ArrayList<Book> bookArrayList;
    private HashMap<String, String> borrowerCatalogue;   // key is bid and value is status of that book under the borrower
    private String user;
    final String TAG = "BorrowerLibraryFragment";
    private CollectionReference borrowerCatalogueRef;
    private FirebaseFirestore db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // connect to the database
        db = FirebaseFirestore.getInstance();

        // get the user email
        String filename = getResources().getString(R.string.email_account);
        SharedPreferences sharedPref = getActivity().getSharedPreferences(filename, Context.MODE_PRIVATE);
        user = sharedPref.getString("email", "null");
        if (user == "null") {
            Log.e("Email","No user email recorded");
        }


        // setting up the array adapter
        bookArrayList = new ArrayList<Book>();
        bookArrayAdapter = new CustomBookList(getActivity(), bookArrayList, "b");

        // get a reference to the borrowerCatalogue collection
        borrowerCatalogueRef = db.collection("users").document(user).collection("borrowerCatalogue");


    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v =  inflater.inflate(R.layout.borrower_library_fragment, container, false);

        return v;
    }


    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // setting up the bookList view
        bookListView = view.findViewById(R.id.library_list_view);
        bookListView.setAdapter(bookArrayAdapter);

        // Store the contents of borrowerCatalogue from db in borrowerCatalogue hashmap<bid, requestRefStatus>,
        // display the books whose bids are keys in the borrowerCatalogue hashmap
        borrowerCatalogueRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                borrowerCatalogue = new HashMap<>();
                for (QueryDocumentSnapshot doc : value) {
                    borrowerCatalogue.put(doc.getId(), doc.get("requestRefStatus").toString());   // putting each bid and status in the hashmap
                    updateListView();      // updating ListView to display books in the borrowerCatalogue
                }
            }
        });

        // Setting an onItemClick listener for the list view of books
        // When borrower taps on a book in the list view, they are sent to that book's profile
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // get the book id of the book that was pressed
                Book book = (Book) adapterView.getItemAtPosition(i);
                String bid = book.getBID();

                // open up the book profile
                Intent bookProfileIntent = new Intent(getActivity(), BorrowerBookProfileActivity.class);
                bookProfileIntent.putExtra("bid", bid);
                startActivity(bookProfileIntent);
            }
        });


    }

    private void updateListView() {
        // get a reference to books collection
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
                    // if a book's bid is a key in borrowerCatalogue hashmap, add it to the bookArrayList to be displayed
                    if (borrowerCatalogue.containsKey(doc.getId())) {
                        Book book = doc.toObject(Book.class);
                        book.setStatus(borrowerCatalogue.get(doc.getId()));    // setting the status of the book to match its status under the borrower
                        bookArrayList.add(book);
                    }
                }
                bookArrayAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched from the cloud
            }
        });
    }

}
