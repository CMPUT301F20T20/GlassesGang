package com.example.glassesgang;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ListView;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.glassesgang.browse.BrowseFragment;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for BorrowerHomeActivity. All the UI tests are written here. Robotium test framework is
 used
 */
public class BorrowerHomeActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<BorrowerHomeActivity> rule = new ActivityTestRule<>(BorrowerHomeActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    /**
     * Close activity after each test
     * @throws Exception
     */
    @After
    public void tearDown () {
        solo.finishOpenedActivities();
    }

    /**
     * Gets the Activity
     * @throws Exception
     */
    @Test
    public void start() {
        Activity activity = rule.getActivity();
    }

    @Test
    public void checkBrowseFragment() {
        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity( "Wrong Activity" , BorrowerHomeActivity.class);

        // switch to Library fragment
        solo.clickOnText("Home");

        // Test book
        assertTrue(solo.waitForText("This is a book",1, 2000));
    }

    @Test
    public void checkLibraryFragment() {
        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity( "Wrong Activity" , BorrowerHomeActivity.class);

        // switch to Library fragment
        solo.clickOnText("Books");

        // must be empty for now since no user is logged in, if there are any books, the test will fail
        assertFalse(solo.waitForText("accepted", 1, 2000));
        assertFalse(solo.waitForText("available", 1, 2000));
        assertFalse(solo.waitForText("requested", 1, 2000));
        assertFalse(solo.waitForText("borrowed", 1, 2000));
    }

    @Test
    public void checkNotificationFragment() {
        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity( "Wrong Activity" , BorrowerHomeActivity.class);

        // switch to Notification fragment
        solo.clickOnText("Notifications");

        // Asserts if default Value for boilerplate notification shows up
        assertTrue(solo.waitForText("test message", 1, 2000));
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
