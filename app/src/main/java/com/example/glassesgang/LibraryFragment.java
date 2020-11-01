package com.example.glassesgang;

import android.content.Intent;
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

public class LibraryFragment extends Fragment {
    private ListView bookListView;
    private ArrayAdapter<Book> bookArrayAdapter;
    private ArrayList<Book> bookArrayList;
    private FirebaseFirestore db;
    final String TAG = "Sample";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v =  inflater.inflate(R.layout.library_fragment, container, false);
//        ListView bookListView;
//        final ArrayAdapter<Book> bookArrayAdapter;
//        final ArrayList<Book> bookArrayList;
//
        bookListView = v.findViewById(R.id.library_list_view);
        bookArrayList = new ArrayList<Book>();
        bookArrayAdapter = new CustomBookList(getActivity(), bookArrayList);
        bookListView.setAdapter(bookArrayAdapter);


        db = FirebaseFirestore.getInstance();

//        String user = getActivity().getIntent().getStringExtra("user");
        String user = "someone@ualberta.ca";
        // if fragment started from OwnerHomeActivity, get owner catalogue
        if (getActivity() instanceof OwnerHomeActivity) {
            DocumentReference ownerRef = db.collection("users").document(user);
            ownerRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        ArrayList<String> a = (ArrayList<String>) document.get("ownerCatalogue");
                        updateListView(a, v);
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

        else if (getActivity() instanceof BorrowerHomeActivity) {
            // get caltalogue of borrower instead
        }

        else {
            try {
                throw new Exception("Libray Fragment can only be started by OwnerHomeActivity or BorrowerHomeActivity");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Book book = (Book) adapterView.getItemAtPosition(i);
                String bookID = book.getBID();
                DocumentReference bookRef= db.collection("books").document(bookID);
                String path = bookRef.getPath();
                Intent bookProfileIntent;
                if (getActivity() instanceof OwnerHomeActivity) {
                    bookProfileIntent = new Intent(getActivity(), OwnerBookProfileActivity.class);
                    bookProfileIntent.putExtra("path", path);
                    startActivity(bookProfileIntent);
                }
                else if (getActivity() instanceof BorrowerHomeActivity) {
                    bookProfileIntent = new Intent(getActivity(), BorrowerBookProfileActivity.class);
                    bookProfileIntent.putExtra("path", path);
                    startActivity(bookProfileIntent);
                }
            }
        });




//        ArrayAdapter<String> testAdapter;
//        ArrayList<String> testArrayList;
//
//        ListView testListView = (ListView) v.findViewById(R.id.library_list_view);
//
//        testArrayList = new ArrayList<String>();
//        testArrayList.add("Element1");
//        testArrayList.add("Element2");
//        testArrayList.add("Element3");
//        testArrayList.add("Element3");
//        testArrayList.add("Element3");
//        testArrayList.add("Element3");
//        testArrayList.add("Element3");
//        testArrayList.add("Element3");
//        testArrayList.add("Element3");
//        testArrayList.add("Element3");
//        testArrayList.add("Element3");
//        testArrayList.add("Element3");
//
//        ArrayAdapter<String> allItemsAdapter = new ArrayAdapter<String>(getActivity().getBaseContext(), R.layout.content, testArrayList);
//
//        testListView.setAdapter(allItemsAdapter);

        return v;
    }

    void updateListView(final ArrayList<String> a, View v) {
        CollectionReference booksRef = db.collection("books");


        booksRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                bookArrayList.clear();

                for (QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                    if (a.contains(doc.getId())) {
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
