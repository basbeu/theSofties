package ch.epfl.sweng.favors;

import android.support.test.runner.AndroidJUnit4;

import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
@RunWith(AndroidJUnit4.class)
public class FavorsFragmentTest {

    @Rule
    public FragmentTestRule<FavorsFragment> mFragmentTestRule = new FragmentTestRule<FavorsFragment>(FavorsFragment.class);
    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Test
    public void fragment_can_be_instantiated() {
        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.searchFavor)).check(matches(isDisplayed()));
        onView(withId(R.id.sortBySpinner)).check(matches(isDisplayed()));
        onView(withId(R.id.addNewFavor)).check(matches(isDisplayed()));
        onView(withId(R.id.favorsList)).check(matches(isDisplayed()));
    }

////WHY DOES THIS NOT WORK????
//    @Test
//    public void TestCreateNewButton() throws Exception{
//        mFragmentTestRule.launchActivity(null);
//        onView(withId(R.id.addNewFavor)).perform(click());
//    }

}

