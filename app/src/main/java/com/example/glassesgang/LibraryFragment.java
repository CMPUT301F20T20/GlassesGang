package com.example.glassesgang;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class LibraryFragment extends Fragment {
    private ListView bookListView;
    private ArrayAdapter<Book> bookArrayAdapter;
    private ArrayList<Book> bookArrayList;
    private String user;
    final String TAG = "LibraryFragment";
    private FirebaseFirestore db;


    // used for passing the user email from the calling activity
    static LibraryFragment newInstance(String user) {
        Bundle args = new Bundle();
        args.putString("user", user);

        LibraryFragment fragment = new LibraryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v =  inflater.inflate(R.layout.library_fragment, container, false);

        // get the user email
        Bundle args = getArguments();
        if (args != null) {
            user = args.getString("user");
        }

        // set up the array adapter for the listview
        bookListView = v.findViewById(R.id.library_list_view);
        bookArrayList = new ArrayList<Book>();
        bookArrayAdapter = new CustomBookList(getActivity(), bookArrayList);
        bookListView.setAdapter(bookArrayAdapter);

        // connect to the database
        db = FirebaseFirestore.getInstance();

        if (getActivity() instanceof OwnerHomeActivity) {
            // display owner catalogue
            DocumentReference ownerRef = db.collection("users").document(user);   // get owner reference from db
            ownerRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        // get owner catalogue and update list view using books from the catalogue
                        DocumentSnapshot document = task.getResult();
                        ArrayList<String> catalogue = (ArrayList<String>) document.get("ownerCatalogue");
                        updateListView(catalogue, v);
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }
        if (getActivity() instanceof BorrowerHomeActivity) {
            // display borrower catalogue
        }
        else {
            // shouldn't be launched
            try {
                throw new Exception("library fragment must be started by only Owner or Borrower HomeActivity");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return v;
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
