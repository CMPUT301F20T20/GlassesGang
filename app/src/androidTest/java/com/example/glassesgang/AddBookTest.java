package com.example.glassesgang;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AddBookTest {
    private static FirebaseAuth mAuth;
    private static DatabaseManager databaseManager;
    private String email = "mockuser@gmail.com";
    private String password = "12345678";
    private Solo solo;
    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    /**
     * intialize FireBaseAuth and DatabaseManager
     * sign out any logged in user
     */
    @BeforeClass
    public static void initialize() {
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        databaseManager = new DatabaseManager();
        mAuth.signOut();
    }

    /**
     * Sign out the mock user
     */
    @AfterClass
    public static void signOut() {
        mAuth.signOut();
    }

    /**
     * Runs before all tests and creates solo instance.
     * Signs in the mock user if not signed in already
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());

        // if  no one is signed, sign the mock user
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            signInMockUser();
        }
    }

    /**
     * removes all the books of the mock user from the database
     */
    @After
    public void clearCollection() {
        databaseManager.clearOwnerCatalogue(email);
    }


    /**
     * Checks if pressing the plus button directs user to add book activity
     */
    @Test
    public void checkAddButton() {
        // check if pressing add button goes to add book activity
        solo.clickOnView(solo.getView(R.id.add_button));
        solo.assertCurrentActivity("Wrong activity after pressing add button", AddBookActivity.class);
    }

    /**
     * Checks if added book goes into the list view
     */
    @Test
    public void checkListViewAfterAdding() {
        String title = "Title";
        String author = "AuthorFirst AuthorLast";
        String isbn = "1234567890123";

        // click add button
        solo.clickOnView(solo.getView(R.id.add_button));
        solo.assertCurrentActivity("Wrong activity after pressing add button", AddBookActivity.class);

        // enter book description
        solo.enterText((EditText) solo.getView(R.id.book_title_bar), title);
        solo.enterText((EditText) solo.getView(R.id.author_name_bar), author);
        solo.enterText((EditText) solo.getView(R.id.isbn_bar), isbn);

        solo.clickOnButton("SAVE");

        // check if the book is added in the list view
        solo.assertCurrentActivity("Wrong activity after pressing SAVE button", OwnerHomeActivity.class);

        assertTrue(solo.waitForText(title, 1, 2000));
        assertTrue(solo.waitForText(author, 1, 2000));
        assertTrue(solo.waitForText(isbn, 1, 2000));
    }

    /**
     * Checks if pressing back button returns to owner home activity
     * and that it does not add any book
     */
    @Test
    public void testBackButton() {
        String title = "Title";
        String author = "AuthorFirst AuthorLast";
        String isbn = "1234567890123";

        // verify that the book is not in the list view in the first place
        assertFalse(solo.waitForText(title, 1, 2000));
        assertFalse(solo.waitForText(author, 1, 2000));
        assertFalse(solo.waitForText(isbn, 1, 2000));

        // click add button
        solo.clickOnView(solo.getView(R.id.add_button));
        solo.assertCurrentActivity("Wrong activity after pressing add button", AddBookActivity.class);

        // enter book description
        solo.enterText((EditText) solo.getView(R.id.book_title_bar), title);
        solo.enterText((EditText) solo.getView(R.id.author_name_bar), author);
        solo.enterText((EditText) solo.getView(R.id.isbn_bar), isbn);

        solo.clickOnButton("BACK");

        // make sure the book is not added in the list view
        solo.assertCurrentActivity("Wrong activity after pressing BACK button", OwnerHomeActivity.class);
        assertFalse(solo.waitForText(title, 1, 2000));
        assertFalse(solo.waitForText(author, 1, 2000));
        assertFalse(solo.waitForText(isbn, 1, 2000));
    }

    /**
     * Check whether the app prevents the user from adding books
     * when they don't provide any of the author, isbn or title
     */
    @Test
    public void checkEmptyFieldVerification() {
        String title = "Title";
        String author = "AuthorFirst AuthorLast";
        String isbn = "1234567890123";

        // click add button
        solo.clickOnView(solo.getView(R.id.add_button));
        solo.assertCurrentActivity("Wrong activity after pressing add button", AddBookActivity.class);

        EditText titleField = (EditText) solo.getView(R.id.book_title_bar);
        EditText authorField = (EditText) solo.getView(R.id.author_name_bar);
        EditText isbnField = (EditText) solo.getView(R.id.isbn_bar);


        // leaving isbn field blank
        solo.enterText(titleField, title);
        solo.enterText(authorField, author);
        solo.clickOnButton("SAVE");
        solo.assertCurrentActivity("Wrong activity", AddBookActivity.class);

        // leaving author field blank
        solo.clearEditText(authorField);
        solo.enterText(isbnField, isbn);
        solo.clickOnButton("SAVE");
        solo.assertCurrentActivity("Wrong activity", AddBookActivity.class);

        // leaving the title field blank
        solo.clearEditText(titleField);
        solo.enterText(authorField, author);
        solo.clickOnButton("SAVE");
        solo.assertCurrentActivity("Wrong activity", AddBookActivity.class);

        // leaving all fields blank
        solo.clearEditText(authorField);
        solo.clearEditText(isbnField);
        solo.clickOnButton("SAVE");
        solo.assertCurrentActivity("Wrong activity", AddBookActivity.class);
    }

    /**
     * Checks whether the app prevents user to add books with invalid isbn
     */
    @Test
    public void testISBNFieldVerification() {
        String title = "Title";
        String author = "AuthorFirst AuthorLast";
        String longISBN = "12345678901231234567890123";
        String shortISBN = "123";

        // click add button
        solo.clickOnView(solo.getView(R.id.add_button));
        solo.assertCurrentActivity("Wrong activity after pressing add button", AddBookActivity.class);

        EditText isbnField = (EditText) solo.getView(R.id.isbn_bar);

        // enter book description
        solo.enterText((EditText) solo.getView(R.id.book_title_bar), title);
        solo.enterText((EditText) solo.getView(R.id.author_name_bar), author);

        // enter invalid long isbn
        solo.enterText(isbnField, longISBN);
        solo.clickOnButton("SAVE");
        solo.assertCurrentActivity("Wrong activity", AddBookActivity.class);

        // enter invalid short isbn
        solo.clearEditText(isbnField);
        solo.enterText(isbnField, shortISBN);
        solo.clickOnButton("SAVE");
        solo.assertCurrentActivity("Wrong activity", AddBookActivity.class);



    }


    public void signInMockUser() {
        solo.enterText((EditText) solo.getView(R.id.email), email);
        solo.enterText((EditText) solo.getView(R.id.password), password);
        solo.clickOnButton("SIGN IN");
    }





}
