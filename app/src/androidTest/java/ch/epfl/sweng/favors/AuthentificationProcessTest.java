package ch.epfl.sweng.favors;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import ch.epfl.sweng.favors.database.User;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class AuthentificationProcessTest {
    //@Rule public ActivityTestRule<AuthentificationProcess> activityActivityTestRule = new ActivityTestRule<>(AuthentificationProcess.class);
    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private FirebaseUser fbFakeUser;
    @Mock private FirebaseAuth fakeAuth;
    private final String FAKEEMAIL = "thisisatestemail@email.com";

    @Before
    public void Before(){
        ExecutionMode.getInstance().setTest(true);
        User fakeUser = new User(fakeAuth);
        User.setMain(fakeUser);
        User.UserGender.setGender(User.getMain(), User.UserGender.M);
        when(fakeAuth.getCurrentUser()).thenReturn(fbFakeUser);
        when(fbFakeUser.getEmail()).thenReturn(FAKEEMAIL);

    }

    @Test
    public void login(){
        ActivityTestRule<AuthentificationProcess> activityActivityTestRule = new ActivityTestRule<>(AuthentificationProcess.class);
        activityActivityTestRule.launchActivity(null);

        onView(withId(R.id.loginMessageText)).check(matches(isDisplayed()));
        onView(withId(R.id.emailTextField)).perform(replaceText("toto@email.com"));
        onView(withId(R.id.passwordTextField)).perform(replaceText("abcd1234"));
    }

}
