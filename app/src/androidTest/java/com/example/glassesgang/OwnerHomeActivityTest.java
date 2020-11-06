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
    /**
     * Add a city to the listview and check the city name using assertTrue
     * Clear all the cities from the listview and check again with assertFalse
     */
    @Test
    public void checkList(){
//Asserts that the current activity is the OwnerHomeActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", OwnerHomeActivity.class);
        assertTrue(solo.waitForText("test message", 1, 2000));
    }
    /**
     * Check item taken from the listview
     */
    @Test
    public void checkCiyListItem(){
        solo.assertCurrentActivity("Wrong Activity", OwnerHomeActivity.class);
        OwnerHomeActivity activity = (OwnerHomeActivity) solo.getCurrentActivity();
        final ListView cityList = activity.cityList; // Get the listview
        String city = (String) cityList.getItemAtPosition(0); // Get item from first position
        assertEquals("Edmonton", city);
    }

    /**
     * Check whether activity correctly switches when item is clicked
     */
    @Test
    public void checkActivitySwitch(){
        solo.assertCurrentActivity("Wrong Activity", OwnerHomeActivity.class);
        solo.clickOnButton("ADD CITY");
        solo.enterText((EditText) solo.getView(R.id.editText_name), "Edmonton");
        solo.clickOnButton("CONFIRM");
        solo.waitForText("Edmonton", 1, 2000);
        solo.clickInList(0);
        solo.waitForActivity("Can't Switch Activity", 2000);
        solo.assertCurrentActivity("Wrong Activity", ShowActivity.class);
    }

    /**
     * Check if city name is consistent in new activity
     */
    @Test
    public void checkDisplayName(){
        solo.assertCurrentActivity("Wrong Activity", OwnerHomeActivity.class);
        solo.clickOnButton("ADD CITY");
        solo.enterText((EditText) solo.getView(R.id.editText_name), "Edmonton");
        solo.clickOnButton("CONFIRM");
        solo.waitForText("Edmonton", 1, 2000);
        solo.clickInList(0);
        solo.waitForActivity("Can't Switch Activity", 2000);
        ShowActivity showActivity = (ShowActivity) solo.getCurrentActivity();
        final String displayNameString = showActivity.displayName.getText().toString();
        assertEquals("Edmonton", displayNameString);
    }

    /**
     * Check back button exists activity, should return to OwnerHomeActivity
     */
    @Test
    public void checkBackButton(){
        solo.assertCurrentActivity("Wrong Activity", OwnerHomeActivity.class);
        solo.clickOnButton("ADD CITY");
        solo.enterText((EditText) solo.getView(R.id.editText_name), "Edmonton");
        solo.clickOnButton("CONFIRM");
        solo.waitForText("Edmonton", 1, 2000);
        solo.clickInList(0);
        solo.waitForActivity("Can't Switch Activity", 2000);
        solo.assertCurrentActivity("Wrong Activity", ShowActivity.class);
        solo.clickOnButton("BACK");
        solo.waitForActivity("Can't Switch Activity", 2000);
        solo.assertCurrentActivity("Wrong Activity", OwnerHomeActivity.class);
    }

    /**
     * Close activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}