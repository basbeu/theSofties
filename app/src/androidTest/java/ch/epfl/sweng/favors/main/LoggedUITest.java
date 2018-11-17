package ch.epfl.sweng.favors.main;


import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;

import ch.epfl.sweng.favors.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LoggedUITest {

    @Rule
    public ActivityTestRule<SplashScreenActivity> mActivityTestRule = new ActivityTestRule<>(SplashScreenActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.ACCESS_FINE_LOCATION",
"android.permission.ACCESS_COARSE_LOCATION");

    @Test
    public void loggedUITest() {
         // Added a sleep statement to match the app's execution delay.
 // The recommended way to handle such scenarios is to use Espresso idling resources:
  // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
try {
 Thread.sleep(7000);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
        
         // Added a sleep statement to match the app's execution delay.
 // The recommended way to handle such scenarios is to use Espresso idling resources:
  // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
try {
 Thread.sleep(7000);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
        
        ViewInteraction textView = onView(
allOf(withId(R.id.welcomeTitle), withText("Welcome back Fake !"),
childAtPosition(
childAtPosition(
IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
0),
0),
isDisplayed()));
        textView.check(matches(withText("Welcome back Fake !")));
        
        ViewInteraction textView2 = onView(
allOf(withId(R.id.lastFavorsTitle), withText("Favors near you..."),
childAtPosition(
childAtPosition(
IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
1),
0),
isDisplayed()));
        textView2.check(matches(withText("Favors near you...")));
        
        ViewInteraction button = onView(
allOf(withId(R.id.button),
childAtPosition(
childAtPosition(
withId(R.id.fragment_container),
0),
3),
isDisplayed()));
        button.check(matches(isDisplayed()));
        
        ViewInteraction appCompatButton = onView(
allOf(withId(R.id.button), withText("More favors"),
childAtPosition(
childAtPosition(
withId(R.id.fragment_container),
0),
3),
isDisplayed()));
        appCompatButton.perform(click());
        
         // Added a sleep statement to match the app's execution delay.
 // The recommended way to handle such scenarios is to use Espresso idling resources:
  // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
try {
 Thread.sleep(500);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
        
        ViewInteraction appCompatButton2 = onView(
allOf(withId(R.id.modeSwitch), withText("Map view"),
childAtPosition(
allOf(withId(R.id.linLayout1),
childAtPosition(
withClassName(is("android.support.constraint.ConstraintLayout")),
0)),
0),
isDisplayed()));
        appCompatButton2.perform(click());
        
         // Added a sleep statement to match the app's execution delay.
 // The recommended way to handle such scenarios is to use Espresso idling resources:
  // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
try {
 Thread.sleep(5000);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
        
        ViewInteraction button2 = onView(
allOf(withId(R.id.modeSwitch),
childAtPosition(
allOf(withId(R.id.linLayout1),
childAtPosition(
IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
0)),
0),
isDisplayed()));
        button2.check(matches(isDisplayed()));
        
        ViewInteraction textView3 = onView(
allOf(withId(R.id.tokens), withText("0 Tokens "),
childAtPosition(
allOf(withId(R.id.linLayout1),
childAtPosition(
IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
0)),
1),
isDisplayed()));
        textView3.check(matches(withText("0 Tokens ")));
        
        ViewInteraction appCompatButton3 = onView(
allOf(withId(R.id.modeSwitch), withText("List view"),
childAtPosition(
allOf(withId(R.id.linLayout1),
childAtPosition(
withClassName(is("android.support.constraint.ConstraintLayout")),
0)),
0),
isDisplayed()));
        appCompatButton3.perform(click());
        
         // Added a sleep statement to match the app's execution delay.
 // The recommended way to handle such scenarios is to use Espresso idling resources:
  // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
try {
 Thread.sleep(500);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
        
        ViewInteraction textView4 = onView(
allOf(withId(R.id.title), withText("Stop cesar surviving the assasinaton."),
childAtPosition(
childAtPosition(
IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
0),
1),
isDisplayed()));
        textView4.check(matches(withText("Stop cesar surviving the assasinaton.")));
        
        ViewInteraction textView5 = onView(
allOf(withId(R.id.title), withText("KILL THE BATMAN"),
childAtPosition(
childAtPosition(
IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
0),
1),
isDisplayed()));
        textView5.check(matches(withText("KILL THE BATMAN")));
        
        ViewInteraction textView6 = onView(
allOf(withId(R.id.title), withText("KILL THE BATMAN"),
childAtPosition(
childAtPosition(
IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
0),
1),
isDisplayed()));
        textView6.check(matches(withText("KILL THE BATMAN")));
        
        ViewInteraction textView7 = onView(
allOf(withId(R.id.title), withText("I am hungry pls hurry"),
childAtPosition(
childAtPosition(
IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
0),
1),
isDisplayed()));
        textView7.check(matches(withText("I am hungry pls hurry")));
        
        ViewInteraction recyclerView = onView(
allOf(withId(R.id.favorsList),
childAtPosition(
withClassName(is("android.widget.LinearLayout")),
1)));
        recyclerView.perform(actionOnItemAtPosition(0, click()));
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
                        && view.equals(((ViewGroup)parent).getChildAt(position));
            }
        };
    }
    }
