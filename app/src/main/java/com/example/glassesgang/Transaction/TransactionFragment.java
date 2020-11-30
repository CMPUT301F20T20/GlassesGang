package com.example.glassesgang.Transaction;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.glassesgang.BookStatus;
import com.example.glassesgang.Helpers.OverrideBackPressed;
import com.example.glassesgang.R;
import com.example.glassesgang.ViewUserActivity;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.util.concurrent.ExecutionException;

/**
 * A fragment representing a list of Items.
 */
public class TransactionFragment extends Fragment implements OverrideBackPressed {

    private OnTransactionInteractionListener listener;
    private TextView emailTextView;
    private TextView infoTextView;
    private Button transactionButton;
    private Button showLocationButton;
    private String requestId;
    private String userEmail;
    private String userType;
    private String bookStatus;
    private LatLng givenLocation;
    private int resultCode;
    final String TAG = "TransactionFragment";
    private FirebaseFirestore db;
    private LatLng mapMarker;

    public interface OnTransactionInteractionListener {
        void onTransactionPressed(String requestId, TransactionType transactionType);
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
        bookStatus = getArguments().getString("bookStatus");
        requestId = getArguments().getString("requestId");
        userEmail = getArguments().getString("userEmail");
        userType = getArguments().getString("userType");
        if (getArguments().getParcelable("givenLocation") != null){
            com.google.android.gms.maps.model.LatLng location = getArguments().getParcelable("givenLocation");
            givenLocation = new LatLng(location);
        }

        //generate a result code for the transaction
        try {
            getResultCode(requestId);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onBackPressed() {
        if (userType == "o" && mapMarker != null) return true;
        else if (userType == "o" && mapMarker == null) {
            Toast.makeText(getContext(), "Please specify a location to accept this request", Toast.LENGTH_SHORT).show();
            return false;
        }
        else return true;
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
        infoTextView = view.findViewById(R.id.info_textview);
        emailTextView.setText(userEmail);

        //location button, default gone. if owner, enable. if borrower, disabled permanently
        showLocationButton = view.findViewById(R.id.show_location_button);
        showLocationButton.setVisibility(View.GONE); //disabled by default
        transactionButton = view.findViewById(R.id.transaction_button);

        //if owner, then transaction is offering a requested book. involves map and scan
        if (userType.equals("o")) {
            if (resultCode < 4) {
                transactionButton.setText("CONFIRM LOCATION");
                transactionButton.setEnabled(false);
                if (resultCode == 2) {
                    infoTextView.setText("Scan to offer book to borrower");
                    transactionButton.setText("OFFER");
                    transactionButton.setEnabled(true);
                }
            }
            else {
                transactionButton.setText("RETRIEVE");
                if (resultCode == 4) {
                    infoTextView.setText("Waiting for borrower to scan");
                    transactionButton.setEnabled(false);
                }
                else if (resultCode == 5) {
                    infoTextView.setText("Scan to retrieve book");
                    transactionButton.setEnabled(true);
                }
            }

            if (givenLocation == null) {
                //owner has not specified a location yet
                showLocationButton.setVisibility(View.VISIBLE);
                showLocationButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //remove info message
                        infoTextView.setVisibility(View.GONE);

                        //display map fragment
                        Fragment mapFragment = new MapFragment(userType); //initialized as an owner map fragment (no params)
                        getParentFragmentManager().beginTransaction().replace(R.id.transaction_fragment_container, mapFragment).commit();

                        //destroy button
                        showLocationButton.setVisibility(View.GONE);
                    }
                });
            } else {
                //owner has already put in location. display this location
                infoTextView.setVisibility(View.GONE);
                Fragment mapFragment = new MapFragment(userType, givenLocation);
                getParentFragmentManager().beginTransaction().replace(R.id.transaction_fragment_container, mapFragment).commit();
            }

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
                        listener.onTransactionPressed(requestId, TransactionType.REQUEST);
                    }
                }
            });
        }

        //if borrower, then transaction is accepting owners offer or returning book. load map fragment
        else if (userType.equals("b")) {
            if (resultCode < 4) {
                infoTextView.setText("Waiting for owner to offer book");
                transactionButton.setText("ACCEPT");
                transactionButton.setEnabled(false);
                if (resultCode == 2) {
                    infoTextView.setText("Scan to accept book");
                    transactionButton.setEnabled(true);
                }
            }
            else {
                transactionButton.setText("RETURN");
                infoTextView.setText("Scan to return book to owner");
                transactionButton.setEnabled(true);
                if (resultCode == 5) {
                    infoTextView.setText("Waiting for owner to scan to complete return");
                    transactionButton.setEnabled(false);
                }
            }

            //display map  if owner has specified a location
            if (givenLocation != null)
            {
                infoTextView.setVisibility(View.GONE);
                Fragment mapFragment = new MapFragment(userType, givenLocation); //initialized as a borrower map fragment
                getParentFragmentManager().beginTransaction().replace(R.id.transaction_fragment_container, mapFragment).commit();
            } else {
                //owner has not given location. show debug label
                infoTextView.setText("Please wait for the owner to specify a pick-up location");
                infoTextView.setTextColor(Color.RED);
                transactionButton.setEnabled(false);
            }
            transactionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (transactionButton.getText().toString().equals("RETURN")) {
                        listener.onTransactionPressed(requestId, TransactionType.RETURN);
                    }
                    else if (transactionButton.getText().toString().equals("ACCEPT")) {
                        listener.onTransactionPressed(requestId, TransactionType.REQUEST);
                    }

                }
            });
        }

        emailTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_info = emailTextView.getText().toString();
                if (!user_info.equals("None")) {
                    Intent viewUserProf = new Intent(getContext(), ViewUserActivity.class);
                    viewUserProf.putExtra("user_info", user_info);   // pass in the bid of the book
                    startActivityForResult(viewUserProf, 1);
                }
            }
        });
    }
    private void getResultCode(String requestId) throws ExecutionException, InterruptedException {   //0 = both missing, 1 = borrower ok, owner missing, 2 = owner ok, borrower missing, 3 = both ok. these are resultCodes for request
        //4 = ", 5 = ", 6 = ", 7 = ", codes for returns
        DocumentReference reqRef = db.collection("requests").document(requestId);
        db.runTransaction(new Transaction.Function<Void>(){
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot req = transaction.get(reqRef);
                Object borrowerOkObj = req.get("borrowerAction");
                Object ownerOkObj =  req.get("ownerAction");
                boolean borrowerOk = false;
                boolean ownerOk = false;
                if (borrowerOkObj != null && (boolean) borrowerOkObj) borrowerOk = true;
                if (ownerOkObj != null && (boolean) ownerOkObj) ownerOk = true;
                if (borrowerOk && ownerOk) resultCode = 3;
                else if (borrowerOk && !ownerOk) resultCode = 1;
                else if (!borrowerOk && ownerOk) resultCode = 2;
                else resultCode = 0;

                if (bookStatus == BookStatus.Status.BORROWED.toString())  resultCode += 4;

                //success
                return null;
            }
        });
    }
}