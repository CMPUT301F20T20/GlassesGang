package com.example.glassesgang.browse;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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
import android.widget.SearchView;

import com.example.glassesgang.Book;
import com.example.glassesgang.BorrowerBookProfileActivity;
import com.example.glassesgang.CustomBookList;
import com.example.glassesgang.OwnerBookProfileActivity;
import com.example.glassesgang.R;
import com.example.glassesgang.ResultsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Fragment for browsing books that are available or requested
 * does not show books that are owned by the user
 */
public class BrowseFragment extends Fragment {
    private ListView browseBookList;
    private ArrayAdapter<Book> browseBookAdapter;
    private ArrayList<Book> bookDataList;
    private HashMap<String, String> borrowerCatalogue;
    private String user;
    private FirebaseFirestore db;
    private CollectionReference borrowerCatalogueRef;
    private final String TAG = "Database";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // connect to the database
        db = FirebaseFirestore.getInstance();

        // get the user email
        user = getUserEmail();

        // set up the arrayAdapter
        bookDataList = new ArrayList<>();
        browseBookAdapter = new CustomBookList(getActivity(), bookDataList, "b");

        // get a reference to the borrowerCatalogue collection
        borrowerCatalogueRef = db.collection("users").document(user).collection("borrowerCatalogue");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.browse_fragment, container, false);

        browseBookList = v.findViewById(R.id.browse_list_view);
        browseBookList.setAdapter(browseBookAdapter);

        // setting up search view
        SearchView searchView = v.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //firebaseSearch(query.toLowerCase());
                getResult(query.toLowerCase());
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                //System.out.println(newText);
                return true;
            }
        });

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
                    // Store the contents of borrowerCatalogue from db in borrowerCatalogue hashmap<bid, requestRefStatus>
                    borrowerCatalogue.put(doc.getId(), doc.get("requestRefStatus").toString());
                }
                updateListView();      // updating ListView and overwriting status as needed using borrowerCatalogue
            }
        });

        browseBookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // get the book id of the book that was pressed
                Book book = (Book) adapterView.getItemAtPosition(i);
                String bid = book.getBID();

                // open up the appropriate book profile
                Intent bookProfileIntent;
                bookProfileIntent = new Intent(getActivity(), BorrowerBookProfileActivity.class);
                bookProfileIntent.putExtra("bid", bid);
                startActivity(bookProfileIntent);
            }
        });

        return v;
    }

    private void getResult(final String query) {
        Intent resultIntent = new Intent(getContext(), ResultsActivity.class);
        resultIntent.putExtra("query", query);
        startActivity(resultIntent);
    };

    /**
     * Get current user email they used to log in
     * @return user's email
     */
    private String getUserEmail() {
        String filename = getResources().getString(R.string.email_account);
        SharedPreferences sharedPref = getActivity().getSharedPreferences(filename, Context.MODE_PRIVATE);
        String user = sharedPref.getString("email", null);

        if (user == null) {
            Log.e("Email","No user email recorded");
        }
        return user;
    }

    /**
     * Updates the List View for the Browse Fragment
     * fetching book with status requested or available
     */
    private void updateListView() {
        final CollectionReference collectionReference = db.collection("books");
        // fetch books
        collectionReference.whereNotEqualTo("owner", user).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                bookDataList.clear();

                for (QueryDocumentSnapshot document: value) {
                    Map<String, Object> bookData = document.getData();

                    // show books that have status available or requested
                    if (bookData.get("status").equals("REQUESTED") || bookData.get("status").equals("AVAILABLE")) {
                        Book book = document.toObject(Book.class);
                        if (borrowerCatalogue.containsKey(document.getId())) {  // if book is the borrower catalogue, overwrite the book status
                            book.setStatus(borrowerCatalogue.get(document.getId()));
                        } else {
                            book.setStatus("AVAILABLE");   // if book is not in the borrower catalogue, then borrower hasn't interacted with book yet, so set it to available
                        }
                        bookDataList.add(book);
                    }
                }

                browseBookAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched from the cloud
            }
        });
    }
}