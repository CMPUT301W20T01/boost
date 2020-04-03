package ca.ualberta.boost;

import android.app.Activity;
import android.service.autofill.AutofillService;
import android.util.Log;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ca.ualberta.boost.models.ActiveUser;
import ca.ualberta.boost.models.Rider;

import static org.junit.Assert.assertEquals;


/**
 * Test for CallActivity. All UI tests are written here. Robotium test framework is used
 */

public class ProfileTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<RiderMainPage> rule =
            new ActivityTestRule<>(RiderMainPage.class, true, true);

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
     * Tests that a rider can request a ride between two locations
     * US 01.01.01
     */
    @Test
    public void testProfileButton() {
        Rider rider = new Rider("Test Rider", "IntentTestRider", "password", "IntentTestRider@gmail.com", "7801234567");
        ActiveUser.login(rider);

        solo.clickOnButton("Profile");
        solo.assertCurrentActivity("Wrong Activity", UserProfileActivity.class);
        

    }

}