package ch.epfl.sweng.favors.settings;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.database.internal_db.InternalSqliteDb;
import ch.epfl.sweng.favors.database.internal_db.LocalPreferences;
import ch.epfl.sweng.favors.utils.FragmentTestRule;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class SetingsFragmentTest {

    @Rule
    public FragmentTestRule<SettingsFragment> mFragmentTestRule = new FragmentTestRule<>(SettingsFragment.class);

    @Before
    public void setUp(){
        mFragmentTestRule.launchActivity(null);
        InternalSqliteDb.openDb(mFragmentTestRule.getActivity().getApplicationContext());
    }


    @Test
    public void fragment_can_be_instantiated() {
        onView(ViewMatchers.withId(R.id.settingsTitle)).check(matches(isDisplayed()));
    }

    @Test
    public void setEmailNotificationsTest(){
        //twice is done at purpose to not disturb other tests
        onView(ViewMatchers.withId(R.id.email_notif_toggle)).perform(click());
        onView(ViewMatchers.withId(R.id.email_notif_toggle)).perform(click());
    }

    @Test
    public void setNotificationsTest(){
        //twice is done at purpose to not disturb other tests
        onView(ViewMatchers.withId(R.id.app_notif_toggle)).perform(click());
        onView(ViewMatchers.withId(R.id.app_notif_toggle)).perform(click());
    }

    @Test
    public void setColorsTest(){
        onView(ViewMatchers.withId(R.id.colors)).perform(click());
        String selectionText = "Blue";
        onData(allOf(is(instanceOf(String.class)), is(selectionText))).perform(click());
        onView(ViewMatchers.withId(R.id.colors)).check(matches(withSpinnerText(containsString(selectionText))));
    }

    @After
    public void after(){
        LocalPreferences.closeInstance();
    }
}


