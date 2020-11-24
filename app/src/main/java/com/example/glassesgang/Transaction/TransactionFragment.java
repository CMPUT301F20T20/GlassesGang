package com.example.glassesgang.Transaction;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.glassesgang.Book;
import com.example.glassesgang.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A fragment representing a list of Items.
 */
public class TransactionFragment extends Fragment implements MapFragment.OnMapInteractionListener {

    private OnTransactionInteractionListener listener;
    private TextView emailTextView;
    private Button transactionButton;
    private Button showLocationButton;
    private String requestId;
    private String userEmail;
    private String userType;
    private Request request;
    final String TAG = "TransactionFragment";
    private FirebaseFirestore db;
    private com.google.android.gms.maps.model.LatLng mapMarker;

    @Override
    public void onMarkerSelected(com.google.android.gms.maps.model.LatLng latLng) {
        transactionButton.setEnabled(true);
        mapMarker = latLng;
    }

    public interface OnTransactionInteractionListener {
        void onTransactionPressed();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnTransactionInteractionListener) {
            listener = (OnTransactionInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "must implement OnTransactionInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // connect to the database
        db = FirebaseFirestore.getInstance();

        //get requestId, borrower or owner email, and userType from bundle
        requestId = getArguments().getString("requestId");
        userEmail = getArguments().getString("userEmail");
        userType = getArguments().getString("userType");

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_transaction, container, false);
        return v;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //get request object
        DocumentReference reqRef = db.collection("requests").document(requestId);
        reqRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
              @Override
              public void onSuccess(DocumentSnapshot documentSnapshot) {
                  request = documentSnapshot.toObject(Request.class);   // convert the book document to Book Object
              }
        });

        //get email displayed based on userType
        emailTextView = view.findViewById(R.id.email_textview);
        emailTextView.setText(userEmail);

        //location button, default gone. if owner, enable. if borrower, disabled permanently
        showLocationButton = view.findViewById(R.id.show_location_button);
        showLocationButton.setVisibility(View.GONE); //disabled by default
        transactionButton = view.findViewById(R.id.transaction_button);

        //if owner, then transaction is offering a requested book. involves map and scan
        if (userType.equals("o")) {
            //enable location button and enable scanning
            transactionButton.setText("OFFER");
            //offer button for scanning only after location is chosen
            transactionButton.setEnabled(false);
            showLocationButton.setVisibility(View.VISIBLE);
            showLocationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //display map fragment
                    Bundle bundle = new Bundle();
                    bundle.putString("userType", userType); //store bin for later use in request handling
                    Fragment mapFragment = new MapFragment(); //initialized as an owner map fragment (no params)
                    mapFragment.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.transaction_fragment_container, mapFragment).commit();

                    //destroy button
                    showLocationButton.setVisibility(View.GONE);
                }
            });
            transactionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //offer book functionality, TODO: open scan activity
                }
            });
        }

        //if borrower, then transaction is accepting owners offer. load map fragment
        else if (userType.equals("b")) {
            transactionButton.setText("ACCEPT"); //transaction button enabled by default, location button uninteractable
            //display map fragment
            Bundle bundle = new Bundle();
            bundle.putString("userType", userType); //store bin for later use in request handling
            Fragment mapFragment = new MapFragment(request.getLocation()); //initialized as a borrower map fragment
            mapFragment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.transaction_fragment_container, mapFragment).commit();
        }


        //show map location, set offer button to accept
        showLocationButton = view.findViewById(R.id.show_location_button);
        showLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //display map fragment
                Fragment mapFragment = new MapFragment();
                getFragmentManager().beginTransaction().replace(R.id.transaction_fragment_container, mapFragment).commit();

                //destroy button
                showLocationButton.setVisibility(View.GONE);

                //TODO: scanning for borrower
            }
        });

    }

}