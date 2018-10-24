package ch.epfl.sweng.favors;

import android.support.test.espresso.Espresso;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static java.util.EnumSet.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.fail;
//import android.support.test.espresso.contrib.PickerActions;


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

    /*@Test
    public void changesDeadline() {
        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.deadlineFavor)).perform(PickerActions.setDate(2017, 6, 30));
    }
   /* @Test
    public void changesCategory() {
        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.categoryFavor)).perform(click());
        onData(instanceOf(String.class)).inAdapterView(withId(R.id.categoryFavor)).atPosition(0)
                .perform(scrollTo(), click());
        Espresso.pressBack();

        onView(withId(R.id.categoryFavor)).check(matches(withSpinnerText(containsString("Cooking"))));
    }*/
    /*
    @Test
    public void createFavorToastDisplayed(){
        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.titleFavor)).perform(typeText("title"));
        onView(withId(R.id.descriptionFavor)).perform(typeText("description"));
        onView(withId(R.id.locationFavor)).perform(typeText("location"));
        onView(withId(R.id.deadlineFavor)).perform(typeText("24-10-2019"));
        onView(withId(R.id.addFavor)).perform(scrollTo(), click());
        try{
            Thread.sleep(2000);
            //This line tests if a toast is displayed
            onView(withText("Favor created successfully")).inRoot(withDecorView(not(is(mFragmentTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
        } catch(Exception e){
            fail("Can't sleep");
        }
    }*/
}
