package com.example.glassesgang.Transaction;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.glassesgang.R;
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
 * A fragment representing a list of Items.
 */
public class RequestHandlingFragment extends Fragment {

    private OnFragmentInteractionListener listener;
    private ListView requestListView;
    private ArrayAdapter<Request> requestAdapter;
    private ArrayList<Request> requestArrayList;
    final String TAG = "RequestFragment";
    private FirebaseFirestore db;

    public interface OnFragmentInteractionListener {
        void onDeclineRequest(Request request);
        void onAcceptRequest(Request request);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // connect to the database
        db = FirebaseFirestore.getInstance();

        // setting up the array adapter
        requestArrayList = new ArrayList<Request>();
        requestAdapter = new RequestListAdapter(getActivity(), requestArrayList);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_request_list, container, false);
        return v;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // setting up the requestList view
        requestListView = view.findViewById(R.id.request_listview);
        requestListView.setAdapter(requestAdapter);

        //get bid from bundle
        String bid = getArguments().getString("bid");

        DocumentReference reqRef = db.collection("books").document(bid);   // get request reference from db for that book

        // display requests for this book
        reqRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data 1: " + snapshot.getData());
                    ArrayList<String> requestList = (ArrayList<String>) snapshot.get("requestList");
                    updateListView(requestList, view);
                } else {
                    Log.d(TAG, "Current data 2: null");
                }
            }
        });

        requestListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // get the request id of the book that was pressed
                Request request = (Request) adapterView.getItemAtPosition(i);
                String requestId = request.getRequestId();

                // get request reference from db
                DocumentReference reqRef = db.collection("requests").document(requestId);

                // transition to the transaction fragment in parent
                //TODO: transaction fragment, sending switch command to parent through interface
                listener.onAcceptRequest(request);
            }
        });


    }

    private void updateListView(final ArrayList<String> requestList, View v) {
        CollectionReference reqRef = db.collection("requests");

        reqRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                requestArrayList.clear();

                for (QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                    // if a request is in the requestList, add it to the requestArrayList for the requestListView to be displayed.
                    if (requestList.contains(doc.getId())) {
                        Request req = doc.toObject(Request.class);
                        req.setRequestId(doc.getId());
                        requestArrayList.add(req);
                    }
                }
                requestAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched from the cloud
            }
        });
    }

}