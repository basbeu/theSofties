package ch.epfl.sweng.favors.favors;

import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.matcher.ViewMatchers;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.favors.database.FakeDatabase;
import ch.epfl.sweng.favors.utils.ExecutionMode;
import ch.epfl.sweng.favors.utils.FragmentTestRule;


import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;


public class FavorListTest {

    @Rule
    public FragmentTestRule<FavorsFragment> mFragmentTestRule = new FragmentTestRule<FavorsFragment>(FavorsFragment.class);



    @Before
    public void Before(){
        ExecutionMode.getInstance().setTest(true);
        FakeDatabase.getInstance().createBasicDatabase();
    }

    @Test
    public void start_fragment() throws InterruptedException {
        mFragmentTestRule.launchActivity(null);
        Thread.sleep(2000);
        onView(ViewMatchers.withText(FakeDatabase.LAST_FAVOR_TITLE)).check(doesNotExist());
    }

    private static DataInteraction onEntry(String string) {
        return onData(hasEntry(equalTo(FakeDatabase.LAST_FAVOR_TITLE),is(string)));
    }

    @After
    public void tearDown(){
        FakeDatabase.getInstance().removeExtraFromDB();
    }
}
