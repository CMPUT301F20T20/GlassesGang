package com.example.glassesgang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class GoogleSignInActivity extends AppCompatActivity {

    private GoogleSignInClient mGoogleSignInClient;
    private String TAG = "GoogleSignInActivity";
    private int RC_SIGN_IN = 1;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        int darkGreyBackground = 282828;
        getWindow().getDecorView().setBackgroundColor(darkGreyBackground);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // sign ins when button clicked
        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                signIn();
            }
        });
    }

    private void signIn() {
        // prompts the user to choose google account to sign in with
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

        // onActivityResult launches automatically after signing in
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        // creating credentials
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        // signing in user with credentials
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        // successful sign in
        if (user != null) {
            // save email to sharedPreferences
            String filename = getResources().getString(R.string.email_account);
            SharedPreferences sharedPref = getSharedPreferences(filename, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("email", user.getEmail());
            editor.apply();
            // create user - need to fix bug, therefore commented out
            //createUser();
            // redirect to user home page
            Intent homeIntent = new Intent(this, OwnerHomeActivity.class);
            startActivity(homeIntent);
        } else {
            System.out.println("Error Signing In");
            //display error to user
        }
    }

    private void createUser(){
        // creates the user if user not in database
        CollectionReference usersDatabase = FirebaseFirestore.getInstance().collection("users");
        String filename = getResources().getString(R.string.email_account);
        SharedPreferences sharedPref = getSharedPreferences(filename, Context.MODE_PRIVATE);
        String email = sharedPref.getString("email", "False");
        if (!email.equals("False")){
            usersDatabase.document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Owner owner = (Owner) document.get("owner");
                            Borrower borrower = (Borrower) document.get("borrower");
                            //User user = document.toObject(User.class);
                            Log.d(TAG, "User is: " + document.getData());
                        } else {
                            Owner owner = new Owner(getApplicationContext());
                            Borrower borrower = new Borrower(getApplicationContext());
                            Log.d(TAG, "creating user");
                        }
                    } else {
                        Log.d(TAG, "Error get failed with ", task.getException());
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });


        };

    }

}