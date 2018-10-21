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
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.assertEquals;

@RunWith(AndroidJUnit4.class)



public class FavorsMainTest {
    @Rule public ActivityTestRule<FavorsMain> activityActivityTestRule = new ActivityTestRule<>(FavorsMain.class, true, false);


    @Before
    public void Before(){
        ActivityCompat.setPermissionCompatDelegate(new LocationDelegate());
    }

    @Test
    public void registerView(){
        Intent intent = new Intent();
        intent.putExtra("test", "test");
        activityActivityTestRule.launchActivity(intent);
        onView(withId(R.id.registerButton)).perform(click());
        onView(withId(R.id.authentificationButton)).check(matches(isDisplayed()));
        onView(withId(R.id.loginMessageText)).check(matches(withText("Welcome here! Just some small steps...")));
    }

    @Test
    public void loginView(){
        Intent intent = new Intent();
        intent.putExtra("test", "test");
        activityActivityTestRule.launchActivity(intent);
        onView(withId(R.id.loginButton)).perform(click());
        onView(withId(R.id.authentificationButton)).check(matches(isDisplayed()));
        onView(withId(R.id.loginMessageText)).check(matches(withText("Please enter your login informations:")));
    }

    @After
    public void After(){
        ActivityCompat.setPermissionCompatDelegate(null);
    }
    
}
