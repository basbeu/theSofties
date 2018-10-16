package ch.epfl.sweng.favors;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class SetingsFragmentTest {

    @Rule
    public FragmentTestRule<SettingsFragment> mFragmentTestRule = new FragmentTestRule<>(SettingsFragment.class);

    @Test
    public void fragment_can_be_instantiated() {

        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.settingsTitle)).check(matches(isDisplayed()));
    }
}


