//package ca.ualberta.boost;
//
//import android.app.Activity;
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//import androidx.test.platform.app.InstrumentationRegistry;
//import androidx.test.rule.ActivityTestRule;
//import android.widget.EditText;
//import android.widget.ListView;
//import com.robotium.solo.Solo;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import static junit.framework.TestCase.assertTrue;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertFalse;
///**
// * Test class for MainActivity. All the UI tests are written here. Robotium test framework is
// used
// */
//@RunWith(AndroidJUnit4.class)
//public class FirebaseControllerTest {
//
//    private Solo solo;
//    @Rule
//    public ActivityTestRule<FirebaseTestActivity> rule =
//            new ActivityTestRule<>(FirebaseTestActivity.class, true, true);
//
//
//    /**
//     * Runs before all tests and creates solo instance.
//     *
//     * @throws Exception
//     */
//    @Before
//    public void setUp() throws Exception {
//        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
//    }
//
//    /**
//     * Gets the Activity
//     *
//     * @throws Exception
//     */
//    @Test
//    public void start() throws Exception {
//        Activity activity = rule.getActivity();
//    }
//
//    /**
//     * Test to make sure the home activity is displayed
//     */
//    @Test
//    public void testHomeActivity(){
//        solo.assertCurrentActivity("Correct Activity", FirebaseTestActivity.class);
//    }
//
//    /**
//     * Test to make sure that signing in with the correct email and password takes
//     * the user to the correct page
//     */
//    @Test
//    public void testDriverLogIn(){
//        solo.enterText((EditText) solo.getView(R.id.sign_in_email), "driver2@gmail.com");
//        solo.enterText((EditText) solo.getView(R.id.sign_in_password),"stilltestingdriver");
//        solo.clickOnButton("sign in");
//        solo.sleep(2000);
//        solo.assertCurrentActivity("Correct Activity",DriverMainPage.class);
//    }
//
//
//    /**
//     * Test to make sure the sign up activity is displayed
//     */
////    @Test
////    public void testSignUpActivity(){
////        solo.clickOnButton("sign up");
////        solo.sleep(2000);
////        solo.assertCurrentActivity("Correct Activity",SignUpActivity.class);
////    }
//}
