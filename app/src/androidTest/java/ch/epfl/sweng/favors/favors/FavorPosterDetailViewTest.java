package ch.epfl.sweng.favors.favors;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import com.google.android.gms.tasks.Tasks;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.authentication.FakeAuthentication;
import ch.epfl.sweng.favors.database.Database;
import ch.epfl.sweng.favors.database.FakeDatabase;
import ch.epfl.sweng.favors.database.Favor;
import ch.epfl.sweng.favors.main.FavorsMain;
import ch.epfl.sweng.favors.utils.ExecutionMode;
import ch.epfl.sweng.favors.utils.FragmentTestRule;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ch.epfl.sweng.favors.favors.FavorPosterDetailView.OWNER_EMAIL;
import static java.lang.Thread.sleep;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.fail;

//TOTEST: OK BUTTON (acts like Backbutton) AND CHECK WHAT ARE INSIDE FIELDS; SEE PROFILE AND EDIT PROFILE FOR MORE TESTS
//TEST WHO IS THE POSTER BUTTON IN FAVOR DETAIL VIEW TEST CLASS

public class FavorPosterDetailViewTest {

    private UiDevice device;

    //@Rule public FragmentTestRule<FavorPosterDetailView> mFragmentTestRule = new FragmentTestRule<FavorPosterDetailView>(FavorPosterDetailView.class);
    @Rule public FragmentTestRule<FavorPosterDetailView> mFragmentTestRule = new FragmentTestRule<FavorPosterDetailView>(FavorPosterDetailView.class);
    public static final String fakePosterEmail = "bruce.wayne@waynecorp.com";

    @Before
    public void Before(){
        ExecutionMode.getInstance().setTest(true);
       FakeDatabase.getInstance().createBasicDatabase();
       device = UiDevice.getInstance(getInstrumentation());
      // Mockito.when(mFragmentTestRule.getArguments().getString(OWNER_EMAIL)).thenReturn(fakePosterEmail);

    }


    //@Ignore("Testing interface not available")

    @Test
    public void titleIsCorrectlyDisplayed() throws UiObjectNotFoundException {
        mFragmentTestRule.launchActivity(null);

        onView(ViewMatchers.withId(R.id.posterTitle)).perform(scrollTo()).check(matches(isDisplayed()));
    }
    @Test
    public void profilePictureDisplayed(){
        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.profilePic)).check(matches(isDisplayed()));
    }

    @Test
    public void isOk(){
        mFragmentTestRule.launchActivity(new Intent().putExtra(OWNER_EMAIL, fakePosterEmail));
        try {
            sleep(1000);
        } catch (InterruptedException e) {
        }
        onView(withId(R.id.posterFirstName)).check(matches(withText("Bruce")));
    }

   @Test
    public void testBackButton(){
       mFragmentTestRule.launchActivity(null);
        try {
            sleep(1000);
        } catch (InterruptedException e) {
        }
        Espresso.pressBackUnconditionally();
        intended(hasComponent(new ComponentName(getTargetContext(), FavorDetailView.class)));
    }

}
