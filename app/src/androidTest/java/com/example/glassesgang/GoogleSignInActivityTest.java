package com.example.glassesgang;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.By;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)

public class GoogleSignInActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<GoogleSignInActivity> rule =
            new ActivityTestRule<GoogleSignInActivity>(GoogleSignInActivity.class, true, true);

    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void checkActivity() {
        // checks if you are at right activity
        solo.assertCurrentActivity("WRONG ACTIVITY", GoogleSignInActivity.class);
        solo.clickOnButton("Sign in");
    }

    @After
    public void teardown() {
        solo.finishOpenedActivities();
    }
}