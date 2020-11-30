package com.example.glassesgang;

import android.view.WindowManager;
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

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static junit.framework.TestCase.assertTrue;


public class BorrowerHomeActivityTest {
    private static FirebaseAuth mAuth;
    private Solo solo;
    private String mockUserEmail = "mockuser2@gmail.com";
    private String password = "password";
    private int timeout = 2000;


    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, true, true);

    /**
     * intialize FireBaseAuth and DatabaseManager
     * sign out any logged in user
     */
    @BeforeClass
    public static void initialize() {
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            mAuth.signOut();
        }
    }

    /**
     * Sign out the mock user
     */
    @AfterClass
    public static void signOut() {
        signOut();
    }

    @Before
    public void setUp() {
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                rule.getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
            }
        });

        solo = new Solo(getInstrumentation(),rule.getActivity());

        // if  no one is signed, sign the mock user
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            signInMockUser();
        }
        switchToBorrower();
    }

    @After
    public void tearDown() {
        solo.finishOpenedActivities();
    }

    /**
     * manually signs in the mock user
     */
    public void signInMockUser() {
        solo.enterText((EditText) solo.getView(R.id.email), mockUserEmail);
        solo.enterText((EditText) solo.getView(R.id.password), password);
        solo.clickOnButton("SIGN IN");
        if (!solo.waitForActivity(OwnerHomeActivity.class)) {
            solo.waitForActivity(OwnerHomeActivity.class); // wait again for sign in
        }

    }

    public void switchToBorrower() {
        solo.clickOnText("User");
        solo.waitForText("Email:", 1, timeout);
        solo.pressSpinnerItem(0, 1);
    }

    @Test
    public void checkBorrowerHomeActivity() {
        solo.assertCurrentActivity("Wrong Activity", BorrowerHomeActivity.class);

        // switch to Home fragment
        solo.clickOnText("Home");

        String bookIsbn = "1234567890123";
        String mockAuthor = "testAuthor";
        String mockBookTest = "testBook";

        // check if search bar exists
        assertTrue(solo.waitForView(R.id.search_view, 1, timeout));

        // click on information about a book
        solo.clickOnText(mockBookTest);

        // check ISBN
        assertTrue(solo.waitForText(bookIsbn, 1, timeout));

        // check Owner
        assertTrue(solo.waitForText(mockAuthor,1, timeout));
    }

    @Test
    public void checkLibraryFragment() {
        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity( "Wrong Activity" , BorrowerHomeActivity.class);

        // switch to Library fragment
        solo.clickOnText("Books");

        assertTrue(solo.waitForView(R.id.requestedToggleButton));
        assertTrue(solo.waitForView(R.id.acceptedToggleButton));

    }

    @Test
    public void checkUserFragment() {
        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity( "Wrong Activity" , BorrowerHomeActivity.class);

        // switch to User fragment
        solo.clickOnText("User");

        // Assert if email shows up
        assertTrue(solo.searchText("Email:"));
        // Assert role for user is borrower
        assertTrue(solo.waitForText("Borrower"));

    }
}