package ch.epfl.sweng.favors;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.favors.SplashScreenActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)

public class SplashScreenActivityTest {
    @Rule
    public final ActivityTestRule<SplashScreenActivity> mActivityRule =
            new ActivityTestRule<>(SplashScreenActivity.class);
    @Test
    public void testSplashScreen() {
        onView(withId(R.id.logoFavors)).check(matches(withId(R.id.logoFavors)));
    }
}
