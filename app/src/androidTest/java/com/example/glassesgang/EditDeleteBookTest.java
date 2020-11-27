package com.example.glassesgang;

import android.view.View;
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

public class EditDeleteBookTest {
    private static FirebaseAuth mAuth;
    private static DatabaseManager databaseManager;
    private String email = "mockuser@gmail.com";
    private String password = "12345678";
    private Solo solo;
    private int timeout = 3000;
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

    public void signInMockUser() {
        solo.enterText((EditText) solo.getView(R.id.email), email);
        solo.enterText((EditText) solo.getView(R.id.password), password);
        solo.clickOnButton("SIGN IN");
        solo.waitForActivity(OwnerHomeActivity.class, timeout);
    }

    /**
     * adding a test Book
     * @param title title of the book to add
     * @param author author of the book to add
     * @param isbn isbn of the book to add
     */
    public void addBook(String title, String author, String isbn) {
        solo.clickOnView(solo.getView(R.id.add_button));
        solo.assertCurrentActivity("Wrong activity after pressing add button", AddBookActivity.class);
        solo.enterText((EditText) solo.getView(R.id.book_title_bar), title);
        solo.enterText((EditText) solo.getView(R.id.author_name_bar), author);
        solo.enterText((EditText) solo.getView(R.id.isbn_bar), isbn);
        solo.clickOnButton("SAVE");

        solo.waitForText(title, 1, timeout);
    }

    /**
     * Checks if clicking a book from list view opens up the correct book profile
     */
    @Test
    public void checkBookClick() {
        String title = "Original Title";
        String author = "Original Author";
        String isbn = "1234567890123";
        addBook(title, author, isbn);
        solo.clickOnText(title);
        solo.assertCurrentActivity("Wrong Activity after clicking book", OwnerBookProfileActivity.class);

        // check if info is correct
        assertTrue(solo.waitForText(title, 1, timeout));
        assertTrue(solo.waitForText(author, 1, timeout));
        assertTrue(solo.waitForText(isbn, 1, timeout));
    }

    /**
     * Check if edits made on the book are reflected in profile
     */
    @Test
    public void checkEditShowsInProfile() {
        String origTitle = "Original Title";
        String origAuthor = "Original Author";
        String origISBN = "1234567890123";
        String editedTitle = "Edited Title";
        String editedAuthor = "Edited Author";
        String editedISBN = "1231231231231";

        // add a book and open up its profile
        addBook(origTitle, origAuthor, origISBN);
        solo.clickOnText(origTitle);
        solo.assertCurrentActivity("Wrong Activity after clicking book", OwnerBookProfileActivity.class);

        // click edit button
        solo.clickOnButton("EDIT");
        solo.assertCurrentActivity("Wrong Activity after clicking book", OwnerBookProfileActivity.class);

        // edit the book and save changes
        EditText titleField = (EditText) solo.getView(R.id.book_title_bar);
        EditText authorField = (EditText) solo.getView(R.id.author_name_bar);
        EditText isbnField = (EditText) solo.getView(R.id.isbn_bar);
        solo.waitForText(origTitle, 1, timeout);
        solo.clearEditText(titleField);
        solo.clearEditText(authorField);
        solo.clearEditText(isbnField);
        solo.waitForText("", 3, timeout);
        solo.enterText(titleField, editedTitle);
        solo.enterText(authorField, editedAuthor);
        solo.enterText(isbnField, editedISBN);
        solo.clickOnButton("SAVE");

        // check if changes are reflected in the book profile
        solo.assertCurrentActivity("Wrong Activity after clicking book", OwnerBookProfileActivity.class);
        assertTrue(solo.waitForText(editedTitle, 1, timeout));
        assertTrue(solo.waitForText(editedAuthor, 1, timeout));
        assertTrue(solo.waitForText(editedISBN, 1, timeout));
    }

    /**
     * Check if edits made in the book is reflected on the listview
     */
    @Test
    public void checkEditShowsInListView() {
        String origTitle = "Original Title";
        String origAuthor = "Original Author";
        String origISBN = "1234567890123";
        String editedTitle = "Edited Title";
        String editedAuthor = "Edited Author";
        String editedISBN = "1231231231231";

        // add a book and open up its profile
        addBook(origTitle, origAuthor, origISBN);
        solo.clickOnText(origTitle);
        solo.assertCurrentActivity("Wrong Activity after clicking book", OwnerBookProfileActivity.class);

        // click edit button
        solo.clickOnButton("EDIT");
        solo.assertCurrentActivity("Wrong Activity after clicking book", OwnerBookProfileActivity.class);

        // edit the book and save changes
        EditText titleField = (EditText) solo.getView(R.id.book_title_bar);
        EditText authorField = (EditText) solo.getView(R.id.author_name_bar);
        EditText isbnField = (EditText) solo.getView(R.id.isbn_bar);
        solo.waitForText(origTitle, 1, timeout);
        solo.clearEditText(titleField);
        solo.clearEditText(authorField);
        solo.clearEditText(isbnField);
        solo.waitForText("", 3, timeout);
        solo.enterText(titleField, editedTitle);
        solo.enterText(authorField, editedAuthor);
        solo.enterText(isbnField, editedISBN);
        solo.waitForText(editedTitle, 1, timeout);
        solo.waitForText(editedAuthor, 1, timeout);
        solo.waitForText(editedISBN, 1, timeout);
        solo.clickOnButton("SAVE");

        // return to the list view
        solo.assertCurrentActivity("Wrong Activity after clicking book", OwnerBookProfileActivity.class);
        solo.clickOnActionBarHomeButton();
        solo.assertCurrentActivity("Wrong Activity", OwnerHomeActivity.class);

        // check if changes were reflected in the list view
        assertTrue(solo.waitForText(editedTitle, 1, timeout));
        assertTrue(solo.waitForText(editedAuthor, 1, timeout));
        assertTrue(solo.waitForText(editedISBN, 1, timeout));
    }

    /**
     * Checks if deleteing a book is revmoed from the list view
     */
    @Test
    public void testBookRemovedFromListView() {
        // add a book
        String title = "book to delete";
        String author = "Author";
        String isbn = "1234567890123";
        addBook(title, author, isbn);
        solo.waitForText(title, 1, timeout);

        // delete the added book
        solo.clickOnText(title);
        solo.assertCurrentActivity("Wrong Activity", OwnerBookProfileActivity.class);
        solo.clickOnButton("DELETE");
        solo.clickOnText("Confirm");

        // check if the book is removed from the list view
        assertTrue(solo.waitForActivity(OwnerBookProfileActivity.class, timeout));
        assertFalse(solo.waitForText(title, 1, timeout));
        assertFalse(solo.waitForText(author, 1, timeout));
        assertFalse(solo.waitForText(isbn, 1, timeout));
    }

    /**
     * Test if the cancel button works for deletion
     */
    @Test
    public void testDeleteCancelButton() {
        // add a book
        String title = "book to delete";
        String author = "Author";
        String isbn = "1234567890123";
        addBook(title, author, isbn);
        solo.waitForText(title, 1, timeout);

        // attempt to delete the added book, but cancel
        solo.clickOnText(title);
        solo.assertCurrentActivity("Wrong Activity", OwnerBookProfileActivity.class);
        solo.clickOnButton("DELETE");
        solo.clickOnText("Cancel");

        // check that book is not deleted
        solo.assertCurrentActivity("Wrong Activity", OwnerBookProfileActivity.class);
        solo.clickOnActionBarHomeButton();
        assertTrue(solo.waitForText(title, 1, timeout));
        assertTrue(solo.waitForText(author, 1, timeout));
        assertTrue(solo.waitForText(isbn, 1, timeout));
    }


}
