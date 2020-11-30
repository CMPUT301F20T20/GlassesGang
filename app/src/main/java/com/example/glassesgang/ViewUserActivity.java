package com.example.glassesgang;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * This shows the user profile when card is clicked
 */
public class ViewUserActivity extends AppCompatActivity {

    TextView userInfo;
    TextView phoneNumber;
    private String TAG = "VIEW USER ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_user_activity);

        String user_info = getIntent().getStringExtra("user_info");
        userInfo = findViewById(R.id.user_info);
        userInfo.setText(user_info);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        phoneNumber = findViewById(R.id.phoneNumber);
        setUserPhoneNumber(user_info);
    }

    // redirects user to prev activity when back button clicked
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void setUserPhoneNumber(String email) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(email);

        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String contactInfo = document.get("phoneNumber").toString();
                        if (contactInfo != "") {
                            phoneNumber.setText(contactInfo);
                        } else {
                            phoneNumber.setText("None");
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
