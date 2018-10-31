package ch.epfl.sweng.favors.favors;

import android.support.test.espresso.matcher.ViewMatchers;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.database.FakeDatabase;
import ch.epfl.sweng.favors.utils.ExecutionMode;
import ch.epfl.sweng.favors.utils.FragmentTestRule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.fail;

public class FavorDetailViewTest {

    //TODO: HANDLE NULL POINTER EXCEPTION SETUSERINFO.HAVA LINE 51 AND 56 TO MAKE TESTS PASS

    @Rule public FragmentTestRule<FavorDetailView> mFragmentTestRule = new FragmentTestRule<FavorDetailView>(FavorDetailView.class);


    @Before
    public void Before(){
        ExecutionMode.getInstance().setTest(true);
        FakeDatabase.getInstance().createBasicDatabase();
    }


    @Test
    public void imInterestedToast(){
        mFragmentTestRule.launchActivity(null);
        onView(ViewMatchers.withId(R.id.favIntrestedButton)).perform(scrollTo(), click());
        onView(withText("We will inform the poster of the add that you are intrested to help!")).inRoot(withDecorView(not(is(mFragmentTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void imageDisplayed(){
        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.imageView)).check(matches(isDisplayed()));
    }



    @Test
    public void image2Displayed(){

        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.imageView2)).check(matches(isDisplayed()));
    }

   @Test
    public void tokensAmountDisplayed(){
        mFragmentTestRule.launchActivity(null);

        onView(withId(R.id.favTokAmmount)).check(matches(isDisplayed()));
    }

    @Test
    public void reportAbusiveAddToast(){
        mFragmentTestRule.launchActivity(null);

        try{
            Thread.sleep(2000);
            //This line tests if a toast is displayed
            onView(withId(R.id.favReportAbusiveAdd)).perform(scrollTo(), click());
            Thread.sleep(2000);
            onView(withText("issue has been reported! Sorry for the inconvenience")).inRoot(withDecorView(not(is(mFragmentTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
        } catch(Exception e){
            fail("Can't sleep");
        }
    }

}
