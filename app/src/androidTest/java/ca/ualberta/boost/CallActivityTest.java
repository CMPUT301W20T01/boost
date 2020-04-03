package ca.ualberta.boost;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.firebase.ui.auth.ui.phone.PhoneActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import java.util.Random;

/**
 * Tests for making sure the user profile can call and email
 */

public class CallActivityTest {

    private Solo solo;
    private FirebaseFirestore fb;
    private FirebaseAuth auth;
    private String TAG = "SignUpIntentTest";

    @Rule
    public ActivityTestRule<SignUpActivity> rule =
            new ActivityTestRule<>(SignUpActivity.class, true, true);


    /**
     * Runs before all tests and creates solo instance.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        fb = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
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
     * test to make sure that the phone activity opens with correct phone number
     */
    @Test
    public void testCallPhoneNumber(){
        Activity activity = rule.getActivity();
        Random rand = new Random();
        int randomNum = rand.nextInt(1000000);
        solo.enterText((EditText) solo.getView(R.id.sign_up_first_name), "Test Name");
        // riderMichelle is already taken
        solo.enterText((EditText) solo.getView(R.id.sign_up_username), "testRider"+randomNum);
        solo.enterText((EditText) solo.getView(R.id.sign_up_email), "testRider"+randomNum+"@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.sign_up_phone_number), "7809999999");
        solo.enterText((EditText) solo.getView(R.id.sign_up_password), "testPassword");
        solo.clickOnButton("Sign Up");
//        solo.searchText("Username is taken");
        // signup should fail and remain on signup activity
        solo.sleep(5000);
        solo.assertCurrentActivity("Correct Activity", RiderMainPage.class);
        solo.clickOnButton("Profile");
        solo.sleep(5000);
//        ImageView image = (ImageView) solo.getView(R.id.call_icon_private);
        solo.clickOnText("7809999999");
        solo.sleep(4000);
        solo.assertCurrentActivity("correct", CallActivity.class);
    }

    /**
     * test to make sure that the email activity opens with correct information
     */
    @Test
    public void testUserProfileEmail(){
        Activity activity = rule.getActivity();
        Random rand = new Random();
        int randomNum = rand.nextInt(1000000);
        solo.enterText((EditText) solo.getView(R.id.sign_up_first_name), "Test Name");
        // riderMichelle is already taken
        solo.enterText((EditText) solo.getView(R.id.sign_up_username), "testRider"+randomNum);
        solo.enterText((EditText) solo.getView(R.id.sign_up_email), "testRider"+randomNum+"@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.sign_up_phone_number), "7809999999");
        solo.enterText((EditText) solo.getView(R.id.sign_up_password), "testPassword");
        solo.clickOnButton("Sign Up");
//        solo.searchText("Username is taken");
        // signup should fail and remain on signup activity
        solo.sleep(5000);
        solo.assertCurrentActivity("Correct Activity", RiderMainPage.class);
        solo.clickOnButton("Profile");
        solo.sleep(5000);
//        ImageView image = (ImageView) solo.getView(R.id.call_icon_private);
        solo.clickOnText("testRider"+randomNum+"@gmail.com");
        solo.sleep(4000);
        solo.assertCurrentActivity("correct",EmailActivity.class);
    }
}
