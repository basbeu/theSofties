package ch.epfl.sweng.favors;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiSelector;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.security.KeyStore;

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
    //@Rule public ActivityTestRule<AuthentificationProcess> activityActivityTestRule = new ActivityTestRule<>(AuthentificationProcess.class);
    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    private UiDevice device;

    private final String FAKE_EMAIL = "toto@email.com";
    private final String FAKE_PASSWORD = "abcd1234";
    private final String FAKE_WRONG_EMAIL= "tato@email.com";

    @Mock private FirebaseUser fbFakeUser;
    @Mock private FirebaseAuth fakeAuth;
    private final String FAKEEMAIL = "thisisatestemail@email.com";

    @Before
    public void Before(){
        device = UiDevice.getInstance(getInstrumentation());
        ExecutionMode.getInstance().setTest(true);
        User fakeUser = new User(fakeAuth);
        User.setMain(fakeUser);
        User.UserGender.setGender(User.getMain(), User.UserGender.M);
        when(fakeAuth.getCurrentUser()).thenReturn(fbFakeUser);
        when(fakeAuth.signInWithEmailAndPassword(FAKE_EMAIL,FAKE_PASSWORD)).thenReturn(Tasks.forResult(null));
        when(fakeAuth.signInWithEmailAndPassword(FAKE_WRONG_EMAIL,FAKE_PASSWORD)).thenReturn(Tasks.forCanceled());
        when(fakeAuth.createUserWithEmailAndPassword(FAKE_EMAIL,FAKE_PASSWORD)).thenReturn(Tasks.forResult(null));
        when(fakeAuth.createUserWithEmailAndPassword(FAKE_WRONG_EMAIL,FAKE_PASSWORD)).thenReturn(Tasks.forCanceled());
        when(fbFakeUser.getEmail()).thenReturn(FAKEEMAIL);
        when(fbFakeUser.isEmailVerified()).thenReturn(true);
        when(fbFakeUser.sendEmailVerification()).thenReturn(Tasks.forResult((Void)null));
    }

    @Test
    public void loginSucceed() throws Exception{
        ActivityTestRule<AuthentificationProcess> activityActivityTestRule = new ActivityTestRule<>(AuthentificationProcess.class);
        Intent intent = new Intent();
        intent.putExtra(AuthentificationProcess.AUTHENTIFICATION_ACTION, AuthentificationProcess.Action.Login);
        activityActivityTestRule.launchActivity(intent);
        // Check if the title correspond to a login title
        onView(withId(R.id.loginMessageText)).check(matches(isDisplayed()));
        //login process
        onView(withId(R.id.emailTextField)).perform(replaceText(FAKE_EMAIL));
        onView(withId(R.id.passwordTextField)).perform(replaceText(FAKE_PASSWORD)).perform(closeSoftKeyboard());
        onView(withId(R.id.authentificationButton)).perform(scrollTo(), click());
        /*UiObject loginButton = device.findObject(new UiSelector().text("LOGIN"));
        loginButton.click();*/

        //Check that we are login in the app
        onView(withId(R.id.profileTitle)).check(matches(isDisplayed()));
    }

    @Test
    public void loginFailed() throws Exception{
        ActivityTestRule<AuthentificationProcess> activityActivityTestRule = new ActivityTestRule<>(AuthentificationProcess.class);
        Intent intent = new Intent();
        intent.putExtra(AuthentificationProcess.AUTHENTIFICATION_ACTION, AuthentificationProcess.Action.Login);
        activityActivityTestRule.launchActivity(intent);
        // Check if the title correspond to a login title
        onView(withId(R.id.loginMessageText)).check(matches(isDisplayed()));
        //wrong login process
        onView(withId(R.id.emailTextField)).perform(replaceText(FAKE_WRONG_EMAIL));
        onView(withId(R.id.passwordTextField)).perform(replaceText(FAKE_PASSWORD)).perform(closeSoftKeyboard());
        onView(withId(R.id.authentificationButton)).perform(scrollTo(), click());

        /*UiObject loginButton = device.findObject(new UiSelector().text("LOGIN"));
        loginButton.click();*/
        //Check that the login is failed
        assertEquals("Wrong email or password or email not verified\nPlease try again",activityActivityTestRule.getActivity().requirementsText.get());
    }

    @Test
    public void registerSucceed() throws Exception{
        ActivityTestRule<AuthentificationProcess> activityActivityTestRule = new ActivityTestRule<>(AuthentificationProcess.class);
        Intent intent = new Intent();
        intent.putExtra(AuthentificationProcess.AUTHENTIFICATION_ACTION, AuthentificationProcess.Action.Register);
        activityActivityTestRule.launchActivity(intent);
        // Check if the title correspond to a register title
        onView(withId(R.id.loginMessageText)).check(matches(isDisplayed()));
        //register process
        onView(withId(R.id.emailTextField)).perform(replaceText(FAKE_EMAIL));
        onView(withId(R.id.passwordTextField)).perform(replaceText(FAKE_PASSWORD)).perform(closeSoftKeyboard());
        onView(withId(R.id.authentificationButton)).perform(scrollTo(), click());
        /*UiObject registerButton = device.findObject(new UiSelector().text("REGISTER"));
        registerButton.click();*/
        //Check that we load setUserInfo
        onView(withId(R.id.submit)).check(matches(isDisplayed()));

    }

    @Test
    public void registerFail() throws Exception{
        ActivityTestRule<AuthentificationProcess> activityActivityTestRule = new ActivityTestRule<>(AuthentificationProcess.class);
        Intent intent = new Intent();
        intent.putExtra(AuthentificationProcess.AUTHENTIFICATION_ACTION, AuthentificationProcess.Action.Register);
        activityActivityTestRule.launchActivity(intent);
        // Check if the title correspond to a register title
        onView(withId(R.id.loginMessageText)).check(matches(isDisplayed()));
        //register process
        onView(withId(R.id.emailTextField)).perform(replaceText(FAKE_WRONG_EMAIL));
        onView(withId(R.id.passwordTextField)).perform(replaceText(FAKE_PASSWORD)).perform(closeSoftKeyboard());
        onView(withId(R.id.authentificationButton)).perform(scrollTo(), click());
        /*UiObject registerButton = device.findObject(new UiSelector().text("REGISTER"));
        registerButton.click();*/
        //Check that the register process failed
        assertEquals("Register failed, please try again",activityActivityTestRule.getActivity().requirementsText.get());

    }

}
