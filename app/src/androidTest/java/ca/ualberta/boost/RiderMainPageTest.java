package ca.ualberta.boost;

import android.app.Activity;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ca.ualberta.boost.controllers.ActiveUser;
import ca.ualberta.boost.models.Promise;
import ca.ualberta.boost.models.Ride;
import ca.ualberta.boost.models.Rider;
import ca.ualberta.boost.stores.RideStore;

import static org.junit.Assert.assertEquals;

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
     * Tests that a rider can request a ride between two locations
     * US 01.01.01
     */
    @Test
    public void testRequestRide(){
        final CollectionReference rideCollection = FirebaseFirestore.getInstance().collection("rides");
        Rider rider = new Rider("Test Rider", "IntentTestRider", "password", "IntentTestRider@gmail.com", "7801234567");
        ActiveUser.login(rider);
        solo.clickOnButton("Request Ride");
        solo.typeText((EditText) solo.getView(R.id.searchPickupEditText), "beercade");
        solo.pressSoftKeyboardSearchButton();
        solo.typeText((EditText) solo.getView(R.id.searchDestinationEditText), "cafe mosaics");
        solo.pressSoftKeyboardSearchButton();
        solo.sleep(1000);
        solo.clickOnButton("Confirm");
        solo.clickOnText("Accept");
        // have to wait to get actualRide
        solo.sleep(5000);
        final Ride actualRide = ActiveUser.getCurrentRide();
        Promise<Ride> ridePromise = RideStore.getRide(actualRide.id());
        // check that ride is in db
        ridePromise.addOnSuccessListener(new OnSuccessListener<Ride>() {
            @Override
            public void onSuccess(Ride ride) {
                assertEquals(actualRide.id(), ride.id());
                // remove ride from firebase
                rideCollection.document(ride.id()).delete();

            }
        });
        ridePromise.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // test failed
            }
        });
        ActiveUser.logout();
    }

//    idk how to move map markers using solo
//    @Test
//    public void testGeoLocations() {
//        solo.clickOnButton("Request Ride");
//        solo.typeText((EditText) solo.getView(R.id.searchPickupEditText), "beercade");
//        solo.pressSoftKeyboardSearchButton();
//        solo.typeText((EditText) solo.getView(R.id.searchDestinationEditText), "cafe mosaics");
//    }

    /**
     * Tests that an estimate of a fair fare is shown to riders
     * US 01.06.01
     */
    @Test
    public void testFairFare() {
        Rider rider = new Rider("Test Rider", "IntentTestRider", "password", "IntentTestRider@gmail.com", "7801234567");
        ActiveUser.login(rider);
        solo.clickOnButton("Request Ride");
        solo.typeText((EditText) solo.getView(R.id.searchPickupEditText), "beercade");
        solo.pressSoftKeyboardSearchButton();
        solo.typeText((EditText) solo.getView(R.id.searchDestinationEditText), "cafe mosaics");
        solo.pressSoftKeyboardSearchButton();
        solo.sleep(1000);
        solo.clickOnButton("Confirm");
        // get the cost text
        // assert that its nonzero or something
        // change end location to wem
        // assert that fare is bigger than old one
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
