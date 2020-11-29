package com.example.glassesgang;

import android.widget.EditText;

import androidx.test.ext.junit.runners.AndroidJUnit4;
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
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertTrue;

@RunWith(AndroidJUnit4.class)
public class OwnerHomeActivityTest {
    private static FirebaseAuth mAuth;
    private Solo solo;
    private String mockUserEmail = "mockuser2@gmail.com";
    private String password = "password";
    private int timeout = 2000;


    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, true, true);

    /**
     * initialize FireBaseAuth and DatabaseManager
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
        mAuth.signOut();
    }

    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());

        // if  no one is signed, sign the mock user
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            signInMockUser();
        }
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

    @Test
    public void checkOwnerHomeActivity() {
        solo.assertCurrentActivity("Wrong Activity", OwnerHomeActivity.class);

        // switch to Library fragment
        solo.clickOnText("Books");

        String bookIsbn = "1234567891011";
        String mockAuthor = "Mock Author";

        // check filter buttons
        assertTrue(solo.waitForView(R.id.availableToggleButton, 1, timeout));
        assertTrue(solo.waitForView(R.id.requestedToggleButton));
        assertTrue(solo.waitForView(R.id.acceptedToggleButton));
        assertTrue(solo.waitForView(R.id.borrowedToggleButton));

        // click on information about a book
        solo.clickOnText("Book for testing");

        // check ISBN
        assertTrue(solo.waitForText(bookIsbn, 1, timeout));

        // check Owner
        assertTrue(solo.waitForText(mockAuthor,1, timeout));
    }

    @Test
    public void checkUserActivity() {
        solo.assertCurrentActivity("Wrong Activity", OwnerHomeActivity.class);

        // switch to User fragment
        solo.clickOnText("User");
        assertTrue(solo.waitForText("Email:", 1, 3000));
        assertTrue(solo.isSpinnerTextSelected("Owner"));
    }

    // check if switching from borrower to owner works
    @Test
    public void checkSwitch() {
        solo.assertCurrentActivity("Wrong Activity", OwnerHomeActivity.class);
        solo.clickOnText("User");
        assertTrue(solo.waitForText("Email:", 1, 3000));
        solo.pressSpinnerItem(0, 1);
        solo.assertCurrentActivity("Wrong Activity", BorrowerHomeActivity.class);
    }
}
