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

import com.example.glassesgang.Book;
import com.example.glassesgang.BorrowerBookProfileActivity;
import com.example.glassesgang.CustomBookList;
import com.example.glassesgang.OwnerBookProfileActivity;
import com.example.glassesgang.R;
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
    private FirebaseFirestore db;
    private final String TAG = "Database";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // connect to the database
        db = FirebaseFirestore.getInstance();

        // set up the arrayAdapter
        bookDataList = new ArrayList<>();
        browseBookAdapter = new CustomBookList(getActivity(), bookDataList, "b");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.browse_fragment, container, false);

        browseBookList = v.findViewById(R.id.browse_list_view);
        browseBookList.setAdapter(browseBookAdapter);

        updateListView();

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
        String user = getUserEmail();
        // fetch books
        collectionReference.whereNotEqualTo("owner", user).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                bookDataList.clear();

                for (QueryDocumentSnapshot document: value) {
                    Map<String, Object> bookData = document.getData();

                    // show books that have status available or requested
                    if (bookData.get("status").equals("requested") || bookData.get("status").equals("available")) {
                        Book book = document.toObject(Book.class);
                        bookDataList.add(book);
                    }
                }

                browseBookAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched from the cloud
            }
        });
    }
}