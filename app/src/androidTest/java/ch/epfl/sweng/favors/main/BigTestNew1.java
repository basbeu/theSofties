package ch.epfl.sweng.favors.main;


import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiSelector;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.database.FakeDatabase;
import ch.epfl.sweng.favors.profile.ProfileFragment;
import ch.epfl.sweng.favors.utils.ExecutionMode;
import ch.epfl.sweng.favors.utils.FragmentTestRule;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class BigTestNew1 {

    private UiDevice device;

    @Before
    public void setUp(){
        device = UiDevice.getInstance(getInstrumentation());
        ExecutionMode.getInstance().setTest(true);
        ExecutionMode.getInstance().setInvalidAuthTest(false);
        FakeDatabase.getInstance().createBasicDatabase();
    }

    @Rule
    public ActivityTestRule<SplashScreenActivity> mActivityTestRule = new ActivityTestRule<>(SplashScreenActivity.class);
//    public FragmentTestRule<ProfileFragment> mFragmentTestRule = new FragmentTestRule<>(ProfileFragment.class);

    @Test
    public void bigTestNew1() throws Exception{
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        UiObject allowButton = device.findObject(new UiSelector().text("ALLOW"));
        UiObject denyButton = device.findObject(new UiSelector().text("DENY"));

        if(allowButton.exists() && denyButton.exists()){
            denyButton.click();
        }

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
                        2),
                        isDisplayed()));
        navigationMenuItemView.perform(click());




        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.editProfileButton), withText("Edit profile"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                1),
                        isDisplayed()));
        appCompatButton.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.profFirstNameEdit),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                1)));
        appCompatEditText.perform(scrollTo(), replaceText("Jean"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.profLastNameEdit),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        appCompatEditText2.perform(scrollTo(), replaceText("Eudes"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.profCityEdit),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                5)));
        appCompatEditText3.perform(scrollTo(), replaceText("Lausanne"), closeSoftKeyboard());

        ViewInteraction appCompatRadioButton2 = onView(
                allOf(withId(R.id.profGenderFEdit), withText("F"),
                        childAtPosition(
                                allOf(withId(R.id.prof_gender_edit),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                0)),
                                1)));
        appCompatRadioButton2.perform(scrollTo(), click());

        ViewInteraction appCompatRadioButton = onView(
                allOf(withId(R.id.profGenderMEdit), withText("M"),
                        childAtPosition(
                                allOf(withId(R.id.prof_gender_edit),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                0)),
                                0)));
        appCompatRadioButton.perform(scrollTo(), click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.commitChanges), withText("Update"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                8)));
        appCompatButton2.perform(scrollTo(), click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        onView(withId(R.id.profLastName)).perform(scrollTo());
//        ViewInteraction textView = onView(
//                allOf(withId(R.id.profLastName), withText("Eudes"),
//                        childAtPosition(
//                                childAtPosition(
//                                        IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class),
//                                        0),
//                                3),
//                        isDisplayed()));
//        textView.check(matches(withText("Eudes")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.profileTitle), withText("Jean's Profile"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        0),
                                0),
                        isDisplayed()));
        textView4.check(matches(withText("Jean's Profile")));
//        ViewInteraction textView6 = onView(
//                allOf(withId(R.id.profileTitle), withText("Jean's Profile"),
//                        childAtPosition(
//                                childAtPosition(
//                                        IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class),
//                                        0),
//                                1),
//                        isDisplayed()));
//        textView6.check(matches(withText("Jean's Profile")));

        ViewInteraction button = onView(
                allOf(withId(R.id.editProfileButton),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        0),
                                1),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

//        onView(withId(R.id.profCity)).perform(scrollTo());
//        try {
//            Thread.sleep(500);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        ViewInteraction textView2 = onView(
//                allOf(withId(R.id.profCity), withText("Lausanne"),
//                        childAtPosition(
//                                childAtPosition(
//                                        IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class),
//                                        0),
//                                7),
//                        isDisplayed()));
//        textView2.check(matches(withText("Lausanne")));

//        onView(withId(R.id.profSex)).perform(scrollTo());
//        try {
//            Thread.sleep(500);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        ViewInteraction textView3 = onView(
//                allOf(withId(R.id.profSex), withText("M"),
//                        childAtPosition(
//                                childAtPosition(
//                                        IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class),
//                                        0),
//                                9),
//                        isDisplayed()));
//        textView3.check(matches(withText("M")));

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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
