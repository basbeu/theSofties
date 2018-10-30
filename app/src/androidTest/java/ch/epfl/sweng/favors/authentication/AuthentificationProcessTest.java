package ch.epfl.sweng.favors.authentication;

import android.content.Intent;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.database.Database;
import ch.epfl.sweng.favors.database.FakeDatabase;
import ch.epfl.sweng.favors.utils.ExecutionMode;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sweng.favors.main.LoggedInScreenTest.childAtPosition;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.is;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class AuthentificationProcessTest {

    private UiDevice device;


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


        onView(withId(R.id.emailTextField)).perform(scrollTo(),replaceText("bruce.wayne@waynecorp.com")).perform(closeSoftKeyboard());
        onView(withId(R.id.passwordTextField)).perform(scrollTo(),replaceText("tatata666")).perform(closeSoftKeyboard());
        onView(withId(R.id.authentificationButton)).perform(scrollTo(), click());

        //Check that we are login in the app
        onView(withId(R.id.profileTitle)).check(matches(isDisplayed()));
    }

   /* @Test
    public void logOutToast() {
        ExecutionMode.getInstance().setInvalidAuthTest(false);
        ActivityTestRule<AuthenticationProcess> activityActivityTestRule = new ActivityTestRule<>(AuthenticationProcess.class);
        Intent intent = new Intent();
        intent.putExtra(AuthenticationProcess.AUTHENTIFICATION_ACTION, AuthenticationProcess.Action.Login);

        activityActivityTestRule.launchActivity(intent);
        // Check if the title correspond to a login title
        onView(ViewMatchers.withId(R.id.loginMessageText)).check(matches(isDisplayed()));
        //login process

        onView(withId(R.id.emailTextField)).perform(scrollTo(),replaceText("bruce.wayne@waynecorp.com")).perform(closeSoftKeyboard());
        onView(withId(R.id.passwordTextField)).perform(scrollTo(),replaceText("tatata666")).perform(closeSoftKeyboard());
        onView(withId(R.id.authentificationButton)).perform(scrollTo(), click());
        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction navigationMenuItemView = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.design_navigation_view),
                                childAtPosition(
                                        withId(R.id.nav_view),
                                        0)),
                        4),
                        isDisplayed()));
        navigationMenuItemView.perform(click());
        onView(withText("See you soon !")).
                inRoot(withDecorView(not(is(activityActivityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }*/
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
        onView(withId(R.id.emailTextField)).perform(scrollTo(),replaceText("thisisnotauser@mail.com")).perform(closeSoftKeyboard());
        onView(withId(R.id.passwordTextField)).perform(scrollTo(),replaceText("hsh837dh3")).perform(closeSoftKeyboard());
        onView(withId(R.id.authentificationButton)).perform(scrollTo(), click());

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
        onView(withId(R.id.emailTextField)).perform(scrollTo(),replaceText("newuser@mail.com")).perform(closeSoftKeyboard());
        onView(withId(R.id.passwordTextField)).perform(scrollTo(),replaceText("hhs883jj")).perform(closeSoftKeyboard());
        onView(withId(R.id.authentificationButton)).perform(scrollTo(), click());
        //Check that we load setUserInfo

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
        onView(withId(R.id.emailTextField)).perform(scrollTo(),replaceText("notanewuser@mail.com")).perform(closeSoftKeyboard());
        onView(withId(R.id.passwordTextField)).perform(scrollTo(),replaceText("jnd38jd9")).perform(closeSoftKeyboard());
        onView(withId(R.id.authentificationButton)).perform(scrollTo(), click());
        //Check that the register process failed
        assertEquals("Register failed, please try again",activityActivityTestRule.getActivity().requirementsText.get());

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}

