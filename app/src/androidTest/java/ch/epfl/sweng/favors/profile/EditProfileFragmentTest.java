package ch.epfl.sweng.favors.profile;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import ch.epfl.sweng.favors.authentication.FakeAuthentication;
import ch.epfl.sweng.favors.database.FakeDatabase;
import ch.epfl.sweng.favors.utils.ExecutionMode;
import ch.epfl.sweng.favors.utils.FragmentTestRule;
import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.database.User;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.when;


@RunWith(AndroidJUnit4.class)

public class EditProfileFragmentTest {

    @Rule public FragmentTestRule<EditProfileFragment> mFragmentTestRule = new FragmentTestRule<EditProfileFragment>(EditProfileFragment.class);

    private final String FAKENEWFIRSTNAME = "Charline";
    private final String FAKENEWLASTNAME = "Montial";
    private final String FAKENEWCITY = "Corseaux";


    @Before
    public void Before(){
        ExecutionMode.getInstance().setTest(true);
        FakeDatabase.getInstance().createBasicDatabase();
    }
    @Test
    public void fragmentCanBeInstantiated() {
        mFragmentTestRule.launchActivity(null);
        onView(ViewMatchers.withId(R.id.editProfileTitle)).check(matches(isDisplayed()));
    }

    @Test
    public void userCanEditFirstName() {
        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.profFirstNameEdit)).perform(replaceText(FAKENEWFIRSTNAME)).perform(closeSoftKeyboard());
        onView(withId(R.id.commitChanges)).perform(scrollTo(), click());
        User user = new User(FakeAuthentication.UID);
        assertEquals(FAKENEWFIRSTNAME, user.get(User.StringFields.firstName));

    }

    @Test
    public void userHasCorrectFirstName() {
        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.profFirstNameEdit)).check(matches(withText(FakeAuthentication.FIRST_NAME)));

    }

    @Test
    public void userCanEditLastName() {
        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.profLastNameEdit)).perform(replaceText(FAKENEWLASTNAME)).perform(closeSoftKeyboard());
        onView(withId(R.id.commitChanges)).perform(scrollTo(), click());
        User user = new User(FakeAuthentication.UID);
        assertEquals(FAKENEWLASTNAME, user.get(User.StringFields.lastName));

    }

    @Test
    public void userHasCorrectLastName() {
        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.profLastNameEdit)).check(matches(withText(FakeAuthentication.LAST_NAME)));

    }

    @Test
    public void userCanEditCity() {
        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.profCityEdit)).perform(replaceText(FAKENEWCITY)).perform(closeSoftKeyboard());
        onView(withId(R.id.commitChanges)).perform(scrollTo(), click());
        User user = new User(FakeAuthentication.UID);
        assertEquals(FAKENEWCITY, user.get(User.StringFields.city));

    }

    @Test
    public void userHasCorrectCity() {
        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.profCityEdit)).check(matches(withText(FakeAuthentication.CITY)));

    }


    @Test
    public void userCanEditGender() {
        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.profGenderFEdit)).perform(scrollTo(), click());
        onView(withId(R.id.commitChanges)).perform(scrollTo(), click());
        User user = new User(FakeAuthentication.UID);
        assertEquals("F", user.get(User.StringFields.sex));

    }

    @Test
    public void userHasCorrectGender() {
        mFragmentTestRule.launchActivity(null);
        User user = new User(FakeAuthentication.UID);
        onView(withId(R.id.profGenderFEdit)).check(matches(withText(user.get(User.StringFields.sex))));
    }
}
