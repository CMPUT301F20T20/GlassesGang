package com.example.glassesgang;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class UserProfileFragment extends Fragment {
    private Spinner usertypeSpinner;

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

        // get currentUser passed from HomeActivity
        Bundle args = getArguments();
        int currentUserTypeSelection;
        if (args != null) {
            currentUserTypeSelection = args.getInt("currentUser");
        } else {
            currentUserTypeSelection = 0;
        }
        usertypeSpinner.setSelection(currentUserTypeSelection);

        final String currentRole = usertypeSpinner.getSelectedItem().toString();
        usertypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String newRole = adapterView.getItemAtPosition(i).toString();

                if ((newRole.equals("Owner")) && (!currentRole.equals("Owner"))) {
                    getActivity().finish();
                    Intent ownerIntent = new Intent(getActivity(), OwnerHomeActivity.class);
                    startActivity(ownerIntent);
                } else if ((newRole.equals("Borrower")) && (!currentRole.equals("Borrower"))){
                    Intent borrowerIntent = new Intent(getActivity(), HomeActivity.class);
                    startActivity(borrowerIntent);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return view;
    }
}