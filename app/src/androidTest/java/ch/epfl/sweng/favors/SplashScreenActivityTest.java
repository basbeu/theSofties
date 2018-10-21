package ch.epfl.sweng.favors;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.ActivityCompat;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.fail;


@RunWith(AndroidJUnit4.class)

public class SplashScreenActivityTest {
    @Rule public final ActivityTestRule<SplashScreenActivity> mActivityRule = new ActivityTestRule<>(SplashScreenActivity.class,true, false);

    @Before
    public void Before(){
        ActivityCompat.setPermissionCompatDelegate(new LocationDelegate());
    }

    @Test
    public void testSplashScreen() {

        launchActivity();
        onView(withId(R.id.logoFavors)).check(matches(isDisplayed()));
    }

    @Test
    public void splashScreenDoesNotLastForever(){
        launchActivity();
       try{
            Thread.sleep(6000);
            onView(withId(R.id.welcomeMessageText)).check(matches(isDisplayed()));


        }catch(Exception e){
            fail("Can't sleep");
        }
    }

    @After
    public void After(){
        ActivityCompat.setPermissionCompatDelegate(null);
    }

    private void launchActivity(){
        Intent intent = new Intent();
        intent.putExtra("test", "test");
        mActivityRule.launchActivity(intent);
    }
}
