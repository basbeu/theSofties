package ch.epfl.sweng.favors.favors;

import android.content.ComponentName;
import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.database.Database;
import ch.epfl.sweng.favors.database.FakeDatabase;
import ch.epfl.sweng.favors.database.Favor;
import ch.epfl.sweng.favors.database.User;
import ch.epfl.sweng.favors.database.UserRequest;
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


public class FavorPosterDetailViewTest {

    private UiDevice device;

    @Rule public FragmentTestRule<FavorPosterDetailView> mFragmentTestRule = new FragmentTestRule<FavorPosterDetailView>(FavorPosterDetailView.class);
    public static final String fakePosterEmail = "ownerEMAIL";
    ObservableList<User> usersList = new ObservableArrayList<>();

    @Before
    public void Before(){
        ExecutionMode.getInstance().setTest(true);
       FakeDatabase.getInstance().createBasicDatabase();
       device = UiDevice.getInstance(getInstrumentation());

    }

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

    @Ignore
    @Test
    public void firstNameDisplayed(){
        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.posterFirstName)).check(matches(isDisplayed()));
    }


}
