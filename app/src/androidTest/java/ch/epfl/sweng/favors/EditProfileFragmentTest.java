package ch.epfl.sweng.favors;

import android.support.test.runner.AndroidJUnit4;

import com.google.common.util.concurrent.FakeTimeLimiter;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

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

    @Rule public FragmentTestRule<EditProfileFragment> mFragmentTestRule = new FragmentTestRule<>(EditProfileFragment.class);
    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock private FirebaseUser fbFakeUser;
    @Mock private FirebaseAuth fakeAuth;
    private final String FAKEEMAIL = "thisisatestemail@email.com";
    private final String FAKEFIRSTNAME = "Bastien";
    private final String FAKELASTNAME = "Beuchat";
    private final String FAKECITY = "Lausanne";
    private final String FAKENEWFIRSTNAME = "Charline";
    private final String FAKENEWLASTNAME = "Montial";
    private final String FAKENEWCITY = "Corseaux";


    @Before
    public void Before(){
        ExecutionMode.getInstance().setTest(true);
        User fakeUser = new User(fakeAuth);
        User.setMain(fakeUser);
        User.UserGender.setGender(User.getMain(), User.UserGender.M);
        User.getMain().set(User.StringFields.firstName, FAKEFIRSTNAME);
        User.getMain().set(User.StringFields.lastName, FAKELASTNAME);
        User.getMain().set(User.StringFields.city, FAKECITY);
        when(fakeAuth.getCurrentUser()).thenReturn(fbFakeUser);
        when(fbFakeUser.getEmail()).thenReturn(FAKEEMAIL);
    }
    @Test
    public void fragmentCanBeInstantiated() {
        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.editProfileTitle)).check(matches(isDisplayed()));
    }

    @Test
    public void userCanEditFirstName() {
        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.profFirstNameEdit)).perform(replaceText(FAKENEWFIRSTNAME)).perform(closeSoftKeyboard());
        onView(withId(R.id.commitChanges)).perform(scrollTo(), click());
        assertEquals(FAKENEWFIRSTNAME, User.getMain().get(User.StringFields.firstName));

    }

    @Test
    public void userHasCorrectFirstName() {
        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.profFirstNameEdit)).check(matches(withText(FAKEFIRSTNAME)));

    }

    @Test
    public void userCanEditLastName() {
        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.profLastNameEdit)).perform(replaceText(FAKENEWLASTNAME)).perform(closeSoftKeyboard());
        onView(withId(R.id.commitChanges)).perform(scrollTo(), click());
        assertEquals(FAKENEWLASTNAME, User.getMain().get(User.StringFields.lastName));

    }

    @Test
    public void userHasCorrectLastName() {
        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.profLastNameEdit)).check(matches(withText(FAKELASTNAME)));

    }

    @Test
    public void userCanEditCity() {
        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.profCityEdit)).perform(replaceText(FAKENEWCITY)).perform(closeSoftKeyboard());
        onView(withId(R.id.commitChanges)).perform(scrollTo(), click());
        assertEquals(FAKENEWCITY, User.getMain().get(User.StringFields.city));

    }

    @Test
    public void userHasCorrectCity() {
        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.profCityEdit)).check(matches(withText(FAKECITY)));

    }


    @Test
    public void userCanEditGender() {
        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.profGenderFEdit)).perform(scrollTo(), click());
        onView(withId(R.id.commitChanges)).perform(scrollTo(), click());
        assertEquals("F", User.getMain().get(User.StringFields.sex));

    }

    @Test
    public void userHasCorrectGender() {
        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.profGenderMEdit)).check(matches(withText(User.getMain().get(User.StringFields.sex))));
    }
}
