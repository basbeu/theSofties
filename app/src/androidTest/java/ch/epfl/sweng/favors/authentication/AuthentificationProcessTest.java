package ch.epfl.sweng.favors.authentication;

import android.content.Intent;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiSelector;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import ch.epfl.sweng.favors.database.Database;
import ch.epfl.sweng.favors.database.FakeDatabase;
import ch.epfl.sweng.favors.utils.ExecutionMode;
import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.database.User;
import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class AuthentificationProcessTest {
    //@Rule public ActivityTestRule<AuthenticationProcess> activityActivityTestRule = new ActivityTestRule<>(AuthenticationProcess.class);

    private UiDevice device;

    /*private final String FAKE_EMAIL = "toto@email.com";
    private final String FAKE_PASSWORD = "abcd1234";
    private final String FAKE_WRONG_EMAIL= "tato@email.com";*/

    /*@Mock private FirebaseUser fbFakeUser;
    @Mock private FirebaseAuth fakeAuth;
    private final String FAKEEMAIL = "thisisatestemail@email.com";*/

    @Before
    public void Before(){
        device = UiDevice.getInstance(getInstrumentation());
        ExecutionMode.getInstance().setTest(true);
        FakeDatabase db = (FakeDatabase)Database.getInstance();
        db.createBasicDatabase();
    }

    @Test
    public void loginSucceed() throws Exception{

        ExecutionMode.getInstance().setInvalidAuthTest(false);
        ActivityTestRule<AuthenticationProcess> activityActivityTestRule = new ActivityTestRule<>(AuthenticationProcess.class);
        Intent intent = new Intent();
        intent.putExtra(AuthenticationProcess.AUTHENTIFICATION_ACTION, AuthenticationProcess.Action.Login);

        activityActivityTestRule.launchActivity(intent);
        // Check if the title correspond to a login title
        onView(ViewMatchers.withId(R.id.loginMessageText)).check(matches(isDisplayed()));
        //login process


        onView(withId(R.id.emailTextField)).perform(replaceText("bruce.wayne@waynecorp.com"));
        onView(withId(R.id.passwordTextField)).perform(replaceText("tatata666")).perform(closeSoftKeyboard());
        onView(withId(R.id.authentificationButton)).perform(scrollTo(), click());


        /*UiObject loginButton = device.findObject(new UiSelector().text("LOGIN"));
        loginButton.click();*/

        //Check that we are login in the app
        onView(withId(R.id.profileTitle)).check(matches(isDisplayed()));
    }

    @Test
    public void loginFailed() throws Exception{
        ExecutionMode.getInstance().setInvalidAuthTest(true);
        ActivityTestRule<AuthenticationProcess> activityActivityTestRule = new ActivityTestRule<>(AuthenticationProcess.class);
        Intent intent = new Intent();
        intent.putExtra(AuthenticationProcess.AUTHENTIFICATION_ACTION, AuthenticationProcess.Action.Login);
        activityActivityTestRule.launchActivity(intent);
        // Check if the title correspond to a login title
        onView(withId(R.id.loginMessageText)).check(matches(isDisplayed()));
        //wrong login process
        onView(withId(R.id.emailTextField)).perform(replaceText("thisisnotauser@mail.com"));
        onView(withId(R.id.passwordTextField)).perform(replaceText("hsh837dh3")).perform(closeSoftKeyboard());
        onView(withId(R.id.authentificationButton)).perform(scrollTo(), click());

        /*UiObject loginButton = device.findObject(new UiSelector().text("LOGIN"));
        loginButton.click();*/
        //Check that the login is failed
       assertEquals("Wrong email or password or email not verified\nPlease try again",activityActivityTestRule.getActivity().requirementsText.get());
    }

    @Test
    public void registerSucceed() throws Exception{
        ExecutionMode.getInstance().setInvalidAuthTest(false);
        ActivityTestRule<AuthenticationProcess> activityActivityTestRule = new ActivityTestRule<>(AuthenticationProcess.class);
        Intent intent = new Intent();
        intent.putExtra(AuthenticationProcess.AUTHENTIFICATION_ACTION, AuthenticationProcess.Action.Register);
        activityActivityTestRule.launchActivity(intent);
        // Check if the title correspond to a register title
        onView(withId(R.id.loginMessageText)).check(matches(isDisplayed()));
        //register process
        onView(withId(R.id.emailTextField)).perform(replaceText("newuser@mail.com"));
        onView(withId(R.id.passwordTextField)).perform(replaceText("hhs883jj")).perform(closeSoftKeyboard());
        onView(withId(R.id.authentificationButton)).perform(scrollTo(), click());
        /*UiObject registerButton = device.findObject(new UiSelector().text("REGISTER"));
        registerButton.click();*/
        //Check that we load setUserInfo
       //onView(withId(R.id.submit)).check(matches(isDisplayed()));

    }

    @Test
    public void registerFail() throws Exception{
        ExecutionMode.getInstance().setInvalidAuthTest(true);
        ActivityTestRule<AuthenticationProcess> activityActivityTestRule = new ActivityTestRule<>(AuthenticationProcess.class);
        Intent intent = new Intent();
        intent.putExtra(AuthenticationProcess.AUTHENTIFICATION_ACTION, AuthenticationProcess.Action.Register);
        activityActivityTestRule.launchActivity(intent);
        // Check if the title correspond to a register title
        onView(withId(R.id.loginMessageText)).check(matches(isDisplayed()));
        //register process
        onView(withId(R.id.emailTextField)).perform(replaceText("notanewuser@mail.com"));
        onView(withId(R.id.passwordTextField)).perform(replaceText("jnd38jd9")).perform(closeSoftKeyboard());
        onView(withId(R.id.authentificationButton)).perform(scrollTo(), click());
        /*UiObject registerButton = device.findObject(new UiSelector().text("REGISTER"));
        registerButton.click();*/
        //Check that the register process failed
       assertEquals("Register failed, please try again",activityActivityTestRule.getActivity().requirementsText.get());

    }

}
