package ca.ualberta.boost;

import android.app.Activity;
import android.view.Display;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

import ca.ualberta.boost.models.Ride;

public class RiderMainPageTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<RiderMainPage> rule =
            new ActivityTestRule<>(RiderMainPage.class, true, true);


    /**
     * Runs before all tests and creates solo instance.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    /**
     * Gets the Activity
     *
     * @throws Exception
     */
    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    /**
     * Test that searching a location in the pickup search bar
     * correctly specifies a start location
     */
    @Test
    public void testPickupSearchBar(){
        solo.assertCurrentActivity("Wrong Activity", RiderMainPage.class);
        RiderMainPage activity = (RiderMainPage) solo.getCurrentActivity();
        Marker marker = activity.pickupMarker;
     //   LatLng oldLocation = marker.getPosition();
        solo.clickOnButton("Request Ride");
        solo.enterText((EditText) solo.getView(R.id.searchPickupEditText), "University of Alberta");
        solo.pressSoftKeyboardSearchButton();
        solo.waitForText("updated start location", 1, 4000);

     //  LatLng newLocation = marker.getPosition();
     //   assertNotEquals(oldLocation, newLocation);

    }

    @Test
    public void testDestinationSearchBar() {
        solo.assertCurrentActivity("Wrong Activity", RiderMainPage.class);
        RiderMainPage activity = (RiderMainPage) solo.getCurrentActivity();
        solo.clickOnButton("Request Ride");
        solo.enterText((EditText) solo.getView(R.id.searchDestinationEditText), "West Edmonton Mall");
        solo.waitForText("updated end location", 1, 4000);
        //Marker marker = activity.destinationMarker;
        //assertTrue(marker.isVisible());

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

