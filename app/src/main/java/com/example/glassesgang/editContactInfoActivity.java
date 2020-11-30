package com.example.glassesgang;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class editContactInfoActivity extends AppCompatActivity {
    private EditText phoneNumEditText;
    private Button saveButton;
    private Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact_info);

        Intent intent = getIntent();
        String phoneNumber = intent.getStringExtra("phoneNumber");


        phoneNumEditText = findViewById(R.id.contactInfoEditText);
        phoneNumEditText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        if (phoneNumber != null && !phoneNumber.equals("")) {
            phoneNumEditText.setText(phoneNumber);
        }

        saveButton = findViewById(R.id.save);
        cancelButton = findViewById(R.id.cancel);
        
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPhoneNumber = phoneNumEditText.getText().toString();
                if (newPhoneNumber.length() == 14) { // format must be (XXX)-XXX-XXXX
                    getIntent().putExtra("newPhoneNumber", newPhoneNumber);
                    setResult(Activity.RESULT_OK, getIntent());
                    finish();
                }
                else {
                    phoneNumEditText.setError("Invalid phone number");
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}