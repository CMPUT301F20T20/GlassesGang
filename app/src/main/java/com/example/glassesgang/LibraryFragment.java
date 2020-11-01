package com.example.glassesgang;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class LibraryFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.library_fragment, container, false);
        ListView bookListView;
        final ArrayAdapter<Book> bookArrayAdapter;
        final ArrayList<Book> bookArrayList;
        final String TAG = "Sample";

        bookListView = v.findViewById(R.id.library_list_view);
        bookArrayList = new ArrayList<Book>();
        bookArrayAdapter = new CustomBookList(getActivity(), bookArrayList);
        bookListView.setAdapter(bookArrayAdapter);


        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference booksRef = db.collection("books");
        booksRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                bookArrayList.clear();

                for (QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                    Book book = doc.toObject(Book.class);
                    book.setBID(doc.getId());
                    bookArrayList.add(book);
                }

                bookArrayAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched from the cloud
            }
        });


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
}
