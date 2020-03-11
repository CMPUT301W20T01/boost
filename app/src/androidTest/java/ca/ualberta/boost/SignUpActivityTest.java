package ca.ualberta.boost;

import android.app.Activity;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class SignUpActivityTest {
    private Solo solo;
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

    @Test
    public void checkUniqueUsername() {
        Activity activity = rule.getActivity();
        solo.enterText((EditText) solo.getView(R.id.sign_up_first_name), "Test Name");
        // "michelle11" username is already taken
        solo.enterText((EditText) solo.getView(R.id.sign_up_username), "michelle11");
        solo.enterText((EditText) solo.getView(R.id.sign_up_email), "test111@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.sign_up_phone_number), "1111111111");
        solo.enterText((EditText) solo.getView(R.id.sign_up_password), "password1");
        solo.clickOnButton("Sign Up");
        solo.sleep(8000);
        // signup should fail and remain on signup activity
        solo.assertCurrentActivity("Right Activity", SignUpActivity.class);
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
