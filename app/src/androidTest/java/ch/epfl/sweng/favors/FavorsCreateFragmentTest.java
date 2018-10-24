package ch.epfl.sweng.favors;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class FavorsCreateFragmentTest {

    @Rule
    public FragmentTestRule<FavorCreateFragment> mFragmentTestRule = new FragmentTestRule<>(FavorCreateFragment.class);

    @Test
    public void fragment_can_be_instantiated() {

        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.createFavorTitle)).check(matches(isDisplayed()));
        onView(withId(R.id.imageOfFavor)).check(matches(isDisplayed()));
        onView(withId(R.id.imageView3)).check(matches(isDisplayed()));
        onView(withId(R.id.floatingActionButton)).check(matches(isDisplayed()));
        onView(withId(R.id.titleFavor)).check(matches(isDisplayed()));
        onView(withId(R.id.deadlineFavor)).check(matches(isDisplayed()));
        onView(withId(R.id.locationFavor)).check(matches(isDisplayed()));
        onView(withId(R.id.descriptionFavor)).check(matches(isDisplayed()));
        onView(withId(R.id.categoryFavor)).check(matches(isDisplayed()));
        onView(withId(R.id.addFavor)).check(matches(isDisplayed()));
    }

    @Test
    public void changesTitle() {
        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.titleFavor)).perform(typeText("title")).check(matches(withText("title")));
    }

    @Test
    public void changesDescription() {
        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.descriptionFavor)).perform(typeText("description")).check(matches(withText("description")));
    }

    @Test
    public void changesLocation() {
        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.locationFavor)).perform(typeText("location")).check(matches(withText("location")));
    }
}
