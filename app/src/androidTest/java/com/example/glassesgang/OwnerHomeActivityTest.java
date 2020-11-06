package com.example.glassesgang;
import android.app.Activity;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import android.app.Instrumentation;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.robotium.solo.Solo;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
/**
 * Test class for NotificationActivity. All the UI tests are written here. Robotium test framework is
 used
 */
@RunWith(AndroidJUnit4.class)
public class OwnerHomeActivityTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<OwnerHomeActivity> rule =
            new ActivityTestRule<>(OwnerHomeActivity.class, true, true);
    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }
    /**
     * Gets the Activity
     * @throws Exception
     */
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }


    // check if pressing the add button starts the add book activity
    @Test
    public void checkAddButton(){
        solo.assertCurrentActivity("Wrong Activity", OwnerHomeActivity.class);
        solo.clickOnImageButton(0);
        solo.assertCurrentActivity("Wrong Activity", AddBookActivity.class);
    }

    // check if pressing the back button returns to the owner home activity
    @Test
    public void checkBackButton() {
        solo.assertCurrentActivity("Wrong Activity", OwnerHomeActivity.class);
        solo.clickOnImageButton(0);
        solo.assertCurrentActivity("Wrong Activity", AddBookActivity.class);
        solo.clickOnButton("BACK");
        solo.assertCurrentActivity("Wrong Activity", OwnerHomeActivity.class);
    }

    // check if pressing the book icon opens up the library
    @Test
    public void checkLibraryFragment() {
        solo.assertCurrentActivity("Wrong Activity", OwnerHomeActivity.class);
        solo.clickOnText("Books");
        // must be empty for now since no user is logged in, if there are any books, the test will fail
        assertFalse(solo.waitForText("accepted", 1, 2000));
        assertFalse(solo.waitForText("available", 1, 2000));
        assertFalse(solo.waitForText("requested", 1, 2000));
        assertFalse(solo.waitForText("borrowed", 1, 2000));
    }

    // check if pressing the notification icon shows notification
    @Test
    public void checkNotificationFragment() {
        solo.assertCurrentActivity("Wrong Activity", OwnerHomeActivity.class);
        solo.clickOnText("Notifications");
        // change this when notifications is implemented
        assertTrue(solo.waitForText("test message", 1, 3000));
    }

    // check if pressing the profile icon shows profile
    // test in the future
    @Test
    public void checkProfileFragment() {
        solo.assertCurrentActivity("Wrong Activity", OwnerHomeActivity.class);
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
//    /**
//     * Close activity after each test
//     * @throws Exception
//     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}