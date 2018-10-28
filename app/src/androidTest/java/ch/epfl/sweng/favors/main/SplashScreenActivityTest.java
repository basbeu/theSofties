package ch.epfl.sweng.favors.main;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.database.FakeDatabase;
import ch.epfl.sweng.favors.utils.ExecutionMode;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.fail;


@RunWith(AndroidJUnit4.class)

public class SplashScreenActivityTest {
    public final ActivityTestRule<SplashScreenActivity> mActivityRule = new ActivityTestRule<>(SplashScreenActivity.class);

    @Before
    public void Before(){
        ExecutionMode.getInstance().setTest(true);
        FakeDatabase.getInstance().createBasicDatabase();
    }

    @Test
    public void testSplashScreen() {
        mActivityRule.launchActivity(null);
        onView(withId(R.id.logoFavors)).check(matches(isDisplayed()));
    }

    @Test
    public void splashScreenDoesNotLastForever(){
        mActivityRule.launchActivity(null);
        try{
            Thread.sleep(6000);
            onView(withId(R.id.profileTitle)).check(matches(isDisplayed()));
        }catch(Exception e){
            fail("Can't sleep");
        }
    }
}
