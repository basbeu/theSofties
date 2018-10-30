package ch.epfl.sweng.favors.settings;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.utils.FragmentTestRule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;

@RunWith(AndroidJUnit4.class)
public class SettingsFragmentTest {

    @Rule
    public FragmentTestRule<SettingsFragment> mFragmentTestRule = new FragmentTestRule<>(SettingsFragment.class);

    @Test
    public void fragment_can_be_instantiated() {

        mFragmentTestRule.launchActivity(null);
        onView(ViewMatchers.withId(R.id.settingsTitle)).check(matches(isDisplayed()));
    }
}


