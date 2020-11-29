package com.example.glassesgang;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.robotium.solo.Solo;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;

public class BorrowerHomeActivityTest {
    private static FirebaseAuth mAuth;
    private Solo solo;
    private String mockUserEmail = "mockuser@gmail.com";
    private String password = "password";
    private int timeout = 3000;


    @Rule
    public ActivityTestRule<OwnerHomeActivity> rule = new ActivityTestRule<>(OwnerHomeActivity.class, true, true);

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
        solo.clickOnText("Books");
    }


}
