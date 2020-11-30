package com.example.glassesgang.Transaction;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.glassesgang.Book;
import com.example.glassesgang.DatabaseManager;
import com.example.glassesgang.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A fragment representing a list of Items.
 */
public class TransactionFragment extends Fragment{

    private OnTransactionInteractionListener listener;
    private TextView emailTextView;
    private Button transactionButton;
    private Button showLocationButton;
    private String requestId;
    private String userEmail;
    private String userType;
    private String ownerEmail;
    private String bookID;
    private LatLng givenLocation;
    final String TAG = "TransactionFragment";
    private FirebaseFirestore db;
    private LatLng mapMarker;



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
        getParentFragmentManager().setFragmentResultListener("location", this, (FragmentResultListener) (locationString, bundle) -> {
            //convert to latlng coordinates
            mapMarker = bundle.getParcelable("location");
            if (mapMarker != null)
            {
                transactionButton.setText("CONFIRM LOCATION");
                transactionButton.setEnabled(true);
            }
            else transactionButton.setEnabled(false);
        });

        // connect to the database
        db = FirebaseFirestore.getInstance();

        //get requestId, borrower or owner email, and userType from bundle
        requestId = getArguments().getString("requestId");
        userEmail = getArguments().getString("userEmail");
        userType = getArguments().getString("userType");
        ownerEmail = getArguments().getString("ownerEmail");
        bookID = getArguments().getString("bid");
        if (getArguments().getParcelable("givenLocation") != null)
            givenLocation = getArguments().getParcelable("givenLocation");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_transaction, container, false);
        return v;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
            transactionButton.setText("CONFIRM LOCATION");
            //offer button for scanning only after location is chosen
            transactionButton.setEnabled(false);
            showLocationButton.setVisibility(View.VISIBLE);
            showLocationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //display map fragment
                    Bundle bundle = new Bundle();
                    bundle.putString("userType", userType);
                    Fragment mapFragment = new MapFragment(); //initialized as an owner map fragment (no params)
                    mapFragment.setArguments(bundle);
                    getParentFragmentManager().beginTransaction().replace(R.id.transaction_fragment_container, mapFragment).commit();
                    //delete all other requests
                    DatabaseManager dbm = new DatabaseManager();
                    dbm.acceptRequest(bookID, userEmail, ownerEmail, requestId);
                    //destroy button
                    showLocationButton.setVisibility(View.GONE);
                }
            });
            transactionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //offer book functionality, TODO: open scan activity
                    if (transactionButton.getText().toString().equals("CONFIRM LOCATION") && mapMarker != null) {
                        //write location to request
                        db.collection("requests").document(requestId).update("location", mapMarker);
                        transactionButton.setText("OFFER");
                    }
                    else if (transactionButton.getText().toString().equals("OFFER")){
                        //TODO: open scanner and initiate transaction

                        listener.onTransactionPressed();
                    }
                }
            });
        }

        //if borrower, then transaction is accepting owners offer. load map fragment
        else if (userType.equals("b")) {
            transactionButton.setText("ACCEPT"); //transaction button enabled by default, location button uninteractable
            //display map fragment
            Bundle bundle = new Bundle();
            bundle.putString("userType", userType); //store bin for later use in request handling
            Fragment mapFragment = new MapFragment(givenLocation); //initialized as a borrower map fragment
            mapFragment.setArguments(bundle);
            getParentFragmentManager().beginTransaction().replace(R.id.transaction_fragment_container, mapFragment).commit();
            transactionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onTransactionPressed();
                }
            });
        }

    }

}