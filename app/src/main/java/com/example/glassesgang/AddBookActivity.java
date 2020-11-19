package com.example.glassesgang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Activity to add a book in the database
 * title, author, isbn are fields required to add book
 */
public class AddBookActivity extends AppCompatActivity {
    private Button backButton;
    private Button scanButton;
    private Button saveButton;
    private EditText titleEditText;
    private EditText authorEditText;
    private EditText isbnEditText;
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_book);

        // get the user
        String filename = getResources().getString(R.string.email_account);
        SharedPreferences sharedPref = this.getSharedPreferences(filename, Context.MODE_PRIVATE);
        user = sharedPref.getString("email", null);

        if (user == null) {
            Log.e("Email","No user email recorded");
        }

        findViewsById();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // meant to return to the previous activity without adding a book
            }
        });

        // add information to database if valid upon saving
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isThereInvalidInput()) {
                    // get the values for title, author, and isbn from the EditTexts
                    final String title = titleEditText.getText().toString();
                    final String author = authorEditText.getText().toString();
                    final String isbn = isbnEditText.getText().toString();

                    // update the database
                    Book newBook = new Book(title, author, isbn, user);
                    DatabaseManager database = new DatabaseManager();
                    database.addBook(newBook, user);
                    // TODO: somehow add to the system and make sure photos are attached
                    finish();
                }
            }
        });

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: implement scanning for submission 4
            }
        });
    }

    /**
     * assigns each attribute to the proper textView.
     */
    private void findViewsById() {
        backButton = findViewById(R.id.back_button);
        scanButton = findViewById(R.id.scan_button);
        saveButton = findViewById(R.id.finish_button);
        titleEditText = findViewById(R.id.book_title_bar);
        authorEditText = findViewById(R.id.author_name_bar);
        isbnEditText = findViewById(R.id.isbn_bar);
    }

    /**
     * Checks for empty fields in the title, author and isbn fields and that isbn is only of length 13.
     * Displays error message for the invalid fields.
     * @return true if there is an invalid input, false otherwise.
     */
    public boolean isThereInvalidInput() {
        boolean invalidInput = false;
        if (titleEditText.getText().toString().trim().equalsIgnoreCase("")) {
            titleEditText.setError("This field can not be blank");
            invalidInput = true;
        }
        if (authorEditText.getText().toString().trim().equalsIgnoreCase("")) {
            authorEditText.setError("This field can not be blank");
            invalidInput = true;
        }
        if (isbnEditText.getText().toString().trim().equalsIgnoreCase("")) {
            isbnEditText.setError("This field can not be blank");
            invalidInput = true;
        } else if (isbnEditText.getText().toString().length() != 13) {   // ISBN can only be of length 13
            isbnEditText.setError("invalid ISBN");
            invalidInput = true;
        }
        return invalidInput;
    }

}
