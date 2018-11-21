package ch.epfl.sweng.favors.favors;

import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.matcher.ViewMatchers;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.database.FakeDatabase;
import ch.epfl.sweng.favors.utils.ExecutionMode;
import ch.epfl.sweng.favors.utils.FragmentTestRule;


import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;


public class FavorListTest {

    @Rule
    public FragmentTestRule<FavorsList> mFragmentTestRule = new FragmentTestRule<FavorsList>(FavorsList.class);


    @Before
    public void Before(){
        ExecutionMode.getInstance().setTest(true);
        FakeDatabase.getInstance().createBasicDatabase();
    }

    @Test
    public void canInsertSearchText() throws InterruptedException {
        mFragmentTestRule.launchActivity(null);
        onView(ViewMatchers.withId(R.id.searchFavor)).perform(scrollTo(), click(), typeText("KILL"));
    }

    @Test
    public void canSortByLocation() throws InterruptedException{
        mFragmentTestRule.launchActivity(null);
        Thread.sleep(2000);
        onView(ViewMatchers.withId(R.id.sortBySpinner)).perform(scrollTo(), click());
        onData(allOf(is(instanceOf(String.class)), is("location"))).perform(click());
        onView(ViewMatchers.withId(R.id.sortBySpinner)).check(matches(withSpinnerText(containsString("location"))));
    }

    @Test
    public void canSortByRecent() throws InterruptedException {
        mFragmentTestRule.launchActivity(null);
        Thread.sleep(2000);
        onView(ViewMatchers.withId(R.id.sortBySpinner)).perform(scrollTo(), click());
        onData(allOf(is(instanceOf(String.class)), is("recent"))).perform(click());
        onView(ViewMatchers.withId(R.id.sortBySpinner)).check(matches(withSpinnerText(containsString("recent"))));
    }

    @Test
    public void canSortByCategory() {
        mFragmentTestRule.launchActivity(null);

        onView(ViewMatchers.withId(R.id.sortBySpinner)).perform(scrollTo(), click());
        onData(allOf(is(instanceOf(String.class)), is("category"))).perform(click());
        onView(ViewMatchers.withId(R.id.sortBySpinner)).check(matches(withSpinnerText(containsString("category"))));
    }

    private static DataInteraction onEntry(String string) {
        return onData(hasEntry(equalTo(FakeDatabase.LAST_FAVOR_TITLE),is(string)));
    }

    @After
    public void tearDown(){
        FakeDatabase.getInstance().removeExtraFromDB();
    }
}
