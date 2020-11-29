package com.example.glassesgang;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Activity for signing in using Firebase authentication
 */
public class SignInActivity extends AppCompatActivity {

    private String TAG = "SignInActivity";
    private FirebaseAuth mAuth;
    private EditText user_email;
    private EditText user_password;
    private Button signInButton;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // connecting components
        user_email = findViewById(R.id.email);
        user_password = findViewById(R.id.password);
        signInButton = findViewById(R.id.sign_in);
        signUpButton = findViewById(R.id.sign_up);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // listeners
        signInButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Sign In");
                signIn();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Sign Up");
                final Integer password_length = user_password.getText().toString().length();
                if (password_length < 6) {
                    Toast.makeText(SignInActivity.this, "Password needs to be at least 6 " +
                                    "characters long.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    signUp();
                }
            }
        });

    }

    private void signIn() {
        // grab text
        final String email = user_email.getText().toString();
        final String password = user_password.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(SignInActivity.this, "Email or password is empty",
                    Toast.LENGTH_SHORT).show();
            return;
        }


        // sign in user
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(SignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void signUp() {
        // grab text
        final String email = user_email.getText().toString();
        final String password = user_password.getText().toString();

        // sign up user
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void updateUI(FirebaseUser user) {
        // save email to sharedPreferences
        String filename = getResources().getString(R.string.email_account);
        SharedPreferences sharedPref = getSharedPreferences(filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("email", user.getEmail());
        editor.apply();
        User userobj = new User(user.getEmail());
        DatabaseManager.createUser(userobj);
        // redirect to user home page
        Intent homeIntent = new Intent(this, OwnerHomeActivity.class);
        startActivity(homeIntent);
    }
}
