package ch.epfl.sweng.favors.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Looper;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.database.FakeDatabase;
import ch.epfl.sweng.favors.database.User;
import ch.epfl.sweng.favors.database.internal_db.InternalSqliteDb;
import ch.epfl.sweng.favors.database.storage.FirebaseStorageDispatcher;
import ch.epfl.sweng.favors.utils.ExecutionMode;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class LoggedInScreenTest {

    ViewInteraction appCompatImageButton;
    @Rule public ActivityTestRule<LoggedInScreen> activityActivityTestRule = new ActivityTestRule<>(LoggedInScreen.class, true, false);
    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock private Intent data;

    @Before
    public void setUp(){
        ExecutionMode.getInstance().setTest(true);
        ExecutionMode.getInstance().setInvalidAuthTest(false);
        InternalSqliteDb.openDb(activityActivityTestRule.getActivity().getApplicationContext());
        when(data.getData()).thenReturn(Uri.parse("fakeUri"));
    }

    @Test
    public void menu() {
        ExecutionMode.getInstance().setTest(true);
        if(Looper.myLooper() == null){
            Looper.prepare();
        }
        activityActivityTestRule.launchActivity(null);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(
                                Matchers.allOf(ViewMatchers.withId(R.id.toolbar),
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
                        2),
                        isDisplayed()));
        navigationMenuItemView.perform(click());

        appCompatImageButton.perform(click());

        ViewInteraction navigationMenuItemView2 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.design_navigation_view),
                                childAtPosition(
                                        withId(R.id.nav_view),
                                        0)),
                        3),
                        isDisplayed()));
        navigationMenuItemView2.perform(click());

        appCompatImageButton.perform(click());

        ViewInteraction navigationMenuItemView3 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.design_navigation_view),
                                childAtPosition(
                                        withId(R.id.nav_view),
                                        0)),
                        1),
                        isDisplayed()));
        navigationMenuItemView3.perform(click());


        appCompatImageButton.perform(click());

        pressBack();

        Looper.myLooper().quitSafely();
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

    @Ignore
    public void testReimbursment(){
        activityActivityTestRule.getActivity().reimburseExpiredFavors();
        // We need to set up request to fake db to get truly expired favors
        long tok = User.getMain().get(User.LongFields.tokens);
        assertThat(tok,is(19L));
    }

    @Test
    public void onActivityResultTest(){
        if(Looper.myLooper() == null){
            Looper.prepare();
        }
        activityActivityTestRule.launchActivity(null);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        activityActivityTestRule.getActivity().onActivityResult(FirebaseStorageDispatcher.GET_FROM_GALLERY, -1, data);
        Looper.myLooper().quitSafely();
    }

    @After
    public void cleanUp(){
        FakeDatabase.cleanUpAll();
    }
}

