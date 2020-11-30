package com.example.glassesgang;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.app.Activity;
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
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

/**
 * Fragment for showing user's profile
 * handles switching from owner to borrower, and vice versa
 */
public class UserProfileFragment extends Fragment{
    private Spinner usertypeSpinner;
    private Button signOutButton;
    private String TAG = "USER FRAGMENT";
    private TextView emailTexView;
    private TextView contactInfoTextView;
    private Button editContactInfoButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_profile_fragment, container, false);
        usertypeSpinner = view.findViewById(R.id.user_type);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getActivity().getBaseContext(),
                R.array.user_type,
                android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        usertypeSpinner.setAdapter(adapter);
        Log.d(TAG, "inside on create");
        // get currentUser passed from HomeActivity
        Bundle args = getArguments();
        int currentUserTypeSelection;
        if (args != null) {
            currentUserTypeSelection = args.getInt("currentUser");
        } else {
            currentUserTypeSelection = 0;
        }

        signOutButton = view.findViewById(R.id.signoutbutton);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                Log.d(TAG, "please help me");
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(getContext(), SignInActivity.class);
                startActivity(i);

            }
        });

        emailTexView = view.findViewById(R.id.userEmail);
        emailTexView.setText(getEmail());

        setContactInfo(view);

        editContactInfoButton = view.findViewById(R.id.editContactInfo_button);
        editContactInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editContactIntent = new Intent(getActivity(), editContactInfoActivity.class);
                editContactIntent.putExtra("phoneNumber", contactInfoTextView.getText().toString());
                startActivityForResult(editContactIntent, 1);
            }
        });

        usertypeSpinner.setSelection(currentUserTypeSelection);

        // gets current role of the user
        final String currentRole = usertypeSpinner.getSelectedItem().toString();
        usertypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // new role user selected
                String newRole = adapterView.getItemAtPosition(i).toString();
                // redirecting user to proper activity depending on chosen role
                if ((newRole.equals("Owner")) && (!currentRole.equals("Owner"))) {
                    Log.d(TAG, "before activity finishes");
                    getActivity().finish();
                    Log.d(TAG, "after activity finishes");
                    Intent ownerIntent = new Intent(getActivity(), OwnerHomeActivity.class);
                    startActivity(ownerIntent);
                } else if ((newRole.equals("Borrower")) && (!currentRole.equals("Borrower"))){
                    getActivity().finish();
                    Intent borrowerIntent = new Intent(getActivity(), BorrowerHomeActivity.class);
                    startActivity(borrowerIntent);
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }


        });

        return view;

    } // on create class ends

    /**
     * Get current user email they used to log in
     * @return user's email
     */
    public String getEmail(){
        Context context = getContext();
        String filename = context.getResources().getString(R.string.email_account);
        SharedPreferences sharedPref = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        return sharedPref.getString("email", "False");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // If user chose to save changes made in editContactInfoActivity
        // update the contact info upon returning
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){

                String newContactInfo =  data.getStringExtra("newPhoneNumber");
                DatabaseManager dbm = new DatabaseManager();

                // update the profile
                contactInfoTextView.setText(newContactInfo);

                // update the db
                dbm.editPhoneNumber(getEmail(), newContactInfo);
            }
        }
    }

    public void setContactInfo(View view) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(getEmail());

        contactInfoTextView = view.findViewById(R.id.contactInfoEditText);
        editContactInfoButton = view.findViewById(R.id.editContactInfo_button);

        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                            String contactInfo = document.get("phoneNumber").toString();
                            if (contactInfo != "") {
                                contactInfoTextView.setText(contactInfo);
                            }
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



}