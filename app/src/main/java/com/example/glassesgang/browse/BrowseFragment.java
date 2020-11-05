package com.example.glassesgang.browse;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.glassesgang.Book;
import com.example.glassesgang.CustomBookList;
import com.example.glassesgang.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

        // get user
        String filename = getResources().getString(R.string.email_account);
        SharedPreferences sharedPref = getActivity().getSharedPreferences(filename, Context.MODE_PRIVATE);
        String user = sharedPref.getString("email", null);

        if (user == null) {
            Log.e("Email","No user email recorded");
        }

        final CollectionReference collectionReference = db.collection("books");
        collectionReference.whereNotEqualTo("owner", user).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                bookDataList.clear();

                for (QueryDocumentSnapshot document: value) {
                    Map<String, Object> bookData = document.getData();

                    // TODO: ADD COMMENTS
                    if (bookData.get("status").equals("requested") || bookData.get("status").equals("available")) {
                        Book book = new Book((String)document.get("title"), (String)bookData.get("author"), (String)bookData.get("isbn"), (String)document.getId(), (String)document.get("owner"));
                        bookDataList.add(book);
                    }
                }

                browseBookAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched from the cloud
            }
        });

        return v;
    }
}