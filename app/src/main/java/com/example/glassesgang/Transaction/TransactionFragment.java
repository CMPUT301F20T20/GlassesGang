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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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
public class TransactionFragment extends Fragment {

    private OnFragmentInteractionListener listener;
    private TextView borrowerEmailTextView;
    private Button showLocationButton;
    private String requestId;
    private String borrowerEmail;
    final String TAG = "TransactionFragment";
    private FirebaseFirestore db;

    public interface OnFragmentInteractionListener {
        void onOfferPressed();
        void onRetrievePressed();
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

        //get requestId and borrowerEmail from bundle
        requestId = getArguments().getString("requestId");
        borrowerEmail = getArguments().getString("borrowerEmail");

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_transaction, container, false);
        return v;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //get borrower email displayed
        borrowerEmailTextView = view.findViewById(R.id.borrower_email_textview);
        borrowerEmailTextView.setText(borrowerEmail);

        //show location button
        showLocationButton = view.findViewById(R.id.show_location_button);
        showLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //display map fragment
                Fragment mapFragment = new MapFragment();
                getFragmentManager().beginTransaction().replace(R.id.transaction_fragment_container, mapFragment).commit();

                //destroy button
                showLocationButton.setVisibility(View.GONE);
            }
        });

    }

}