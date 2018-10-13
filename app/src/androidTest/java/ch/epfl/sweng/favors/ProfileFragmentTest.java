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
public class ProfileFragmentTest {

    @Rule
    public FragmentTestRule<ProfileFragment> mFragmentTestRule = new FragmentTestRule<>(ProfileFragment.class);

    @Test
    public void fragment_can_be_instantiated() {

        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.profileTitle)).check(matches(isDisplayed()));
    }

}
