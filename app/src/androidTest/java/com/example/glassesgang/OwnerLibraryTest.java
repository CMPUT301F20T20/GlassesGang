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

/**
 * Contains test for the Owner Library
 * which contains all the books that a user owns.
 */
public class OwnerLibraryTest {
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
        if (mAuth.getCurrentUser() != null) {
            mAuth.signOut();
        }
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
     * manually signs in the mock user
     */
    public void signInMockUser() {
        solo.enterText((EditText) solo.getView(R.id.email), email);
        solo.enterText((EditText) solo.getView(R.id.password), password);
        solo.clickOnButton("SIGN IN");
        if (!solo.waitForActivity(OwnerHomeActivity.class)) {
            solo.waitForActivity(OwnerHomeActivity.class); // wait again for sign in
        }
        solo.clickOnText("Books");
    }

    /**
     * manually adds a dummy book
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

        solo.waitForText(title, 1, timeout);  // waiting for the book to be added on the list view
    }

    /**
     * Checks if pressing the plus button directs user to add book activity
     */
    @Test
    public void checkAddButton() {
        solo.clickOnView(solo.getView(R.id.add_button));
        solo.assertCurrentActivity("Wrong activity after pressing add button", AddBookActivity.class);
    }

    /**
     * Checks if added book goes into the list view
     */
    @Test
    public void checkListViewAfterAdding() {
        String title = "The Hunger Games";
        String author = "Suzanne Collins";
        String isbn = "9780439023481";

        addBook(title, author, isbn);

        // check if the book is added in the list view
        solo.assertCurrentActivity("Wrong activity", OwnerHomeActivity.class);
        assertTrue(solo.waitForText(title, 1, timeout));
        assertTrue(solo.waitForText(author, 1, timeout));
        assertTrue(solo.waitForText(isbn, 1, timeout));
    }

    /**
     * Checks if pressing back button returns to owner home activity
     * and that it does not add any book
     */
    @Test
    public void testBackButton() {
        String title = "The Hunger Games";
        String author = "Suzanne Collins";
        String isbn = "9780439023481";

        // verify that the book is not in the list view in the first place
        assertFalse(solo.waitForText(title, 1, 2000));
        assertFalse(solo.waitForText(author, 1, 2000));
        assertFalse(solo.waitForText(isbn, 1, 2000));

        solo.clickOnView(solo.getView(R.id.add_button));
        solo.assertCurrentActivity("Wrong activity after pressing add button", AddBookActivity.class);

        // enter book description, but don't save
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
        String title = "The Hunger Games";
        String author = "Suzanne Collins";
        String isbn = "9780439023481";

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
        String title = "The Hunger Games";
        String author = "Suzanne Collins";
        String longISBN = "978043902348212813912";
        String shortISBN = "97804390";

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

    /**
     * Checks if clicking a book from list view opens up the correct book profile
     */
    @Test
    public void checkBookClick() {
        String title = "The Hunger Games";
        String author = "Suzanne Collins";
        String isbn = "9780439023481";

        addBook(title, author, isbn);

        // click on the added book
        solo.clickOnText(title);
        solo.assertCurrentActivity("Wrong Activity after clicking book", OwnerBookProfileActivity.class);

        // check if info is correct
        assertTrue(solo.waitForText(title, 1, timeout));
        assertTrue(solo.waitForText(author, 1, timeout));
        assertTrue(solo.waitForText(isbn, 1, timeout));
    }

    @Test
    public void testEditFieldHasBookDesc() {
        String origTitle = "The Hunger Games";
        String origAuthor = "Suzanne Collins";
        String origISBN = "9780439023481";

        // add a book and open up its profile
        addBook(origTitle, origAuthor, origISBN);
        solo.clickOnText(origTitle);
        solo.assertCurrentActivity("Wrong Activity after clicking book", OwnerBookProfileActivity.class);

        // click edit button
        solo.clickOnButton("EDIT");
        solo.assertCurrentActivity("Wrong Activity after clicking book", OwnerBookProfileActivity.class);

        // check if the edit text fields contain the book info
        solo.assertCurrentActivity("Wrong Activity", EditBookActivity.class);
        assertTrue(solo.waitForText(origTitle, 1, timeout));
        assertTrue(solo.waitForText(origAuthor, 1, timeout));
        assertTrue(solo.waitForText(origISBN, 1, timeout));
    }

    /**
     * Check if edits made on the book are reflected in profile
     */
    @Test
    public void checkEditShowsInProfile() {
        String origTitle = "Hunger Games";
        String origAuthor = "Suzan Collins";
        String origISBN = "1231231231231";
        String editedTitle = "The Hunger Games";
        String editedAuthor = "Suzanne Collins";
        String editedISBN = "9780439023481";

        // add a book and open up its profile
        addBook(origTitle, origAuthor, origISBN);
        solo.clickOnText(origTitle);
        solo.assertCurrentActivity("Wrong Activity after clicking book", OwnerBookProfileActivity.class);

        // click edit button
        solo.clickOnButton("EDIT");
        solo.assertCurrentActivity("Wrong Activity after clicking book", EditBookActivity.class);
        solo.waitForText(origTitle, 1, 5000); // waiting for the orig description to show before clearing text fields

        // edit the book and save changes
        EditText titleField = (EditText) solo.getView(R.id.book_title_bar);
        EditText authorField = (EditText) solo.getView(R.id.author_name_bar);
        EditText isbnField = (EditText) solo.getView(R.id.isbn_bar);
        solo.clearEditText(titleField);
        solo.clearEditText(authorField);
        solo.clearEditText(isbnField);
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
        String origTitle = "Hunger Games";
        String origAuthor = "Suzan Collins";
        String origISBN = "1231231231231";
        String editedTitle = "The Hunger Games";
        String editedAuthor = "Suzanne Collins";
        String editedISBN = "9780439023481";

        // add a book and open up its profile
        addBook(origTitle, origAuthor, origISBN);
        solo.clickOnText(origTitle);
        solo.assertCurrentActivity("Wrong Activity after clicking book", OwnerBookProfileActivity.class);

        // click edit button
        solo.clickOnButton("EDIT");
        solo.assertCurrentActivity("Wrong Activity after clicking book", EditBookActivity.class);
        solo.waitForText(origTitle, 1, 5000); // waiting for the orig description to show before clearing text fields

        // edit the book and save changes
        EditText titleField = (EditText) solo.getView(R.id.book_title_bar);
        EditText authorField = (EditText) solo.getView(R.id.author_name_bar);
        EditText isbnField = (EditText) solo.getView(R.id.isbn_bar);
        solo.clearEditText(titleField);
        solo.clearEditText(authorField);
        solo.clearEditText(isbnField);
        solo.enterText(titleField, editedTitle);
        solo.enterText(authorField, editedAuthor);
        solo.enterText(isbnField, editedISBN);
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
     * Checks if deleting a book is removes it from the list view
     */
    @Test
    public void testBookRemovedFromListView() {
        // add a book
        String title = "The Hunger Games";
        String author = "Suzanne Collins";
        String isbn = "9780439023481";
        addBook(title, author, isbn);

        // delete the added book
        solo.clickOnText(title);
        solo.assertCurrentActivity("Wrong Activity", OwnerBookProfileActivity.class);
        solo.clickOnButton("DELETE");
        solo.clickOnText("Confirm");

        // check if the book is removed from the list view
        solo.assertCurrentActivity("Wrong Activity", OwnerHomeActivity.class);
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
        String title = "The Hunger Games";
        String author = "Suzanne Collins";
        String isbn = "9780439023481";
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
        solo.assertCurrentActivity("Wrong Activity", OwnerHomeActivity.class);
        assertTrue(solo.waitForText(title, 1, timeout));
        assertTrue(solo.waitForText(author, 1, timeout));
        assertTrue(solo.waitForText(isbn, 1, timeout));
    }




}
