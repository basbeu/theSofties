package ch.epfl.sweng.favors.profile;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.authentication.FakeAuthentication;
import ch.epfl.sweng.favors.database.FakeDatabase;
import ch.epfl.sweng.favors.database.User;
import ch.epfl.sweng.favors.database.fields.DatabaseStringField;
import ch.epfl.sweng.favors.utils.ExecutionMode;
import ch.epfl.sweng.favors.utils.FragmentTestRule;

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


@RunWith(AndroidJUnit4.class)

public class EditProfileFragmentTest {

    @Rule public FragmentTestRule<EditProfileFragment> mFragmentTestRule = new FragmentTestRule<EditProfileFragment>(EditProfileFragment.class);


    private User u;

    /*private final String FAKE_UID = "sklfklalsdj";
    private final String FAKE_EMAIL = "thisisatestemail@email.com";
    private final String FAKE_FIRST_NAME = "toto";
    private final String FAKE_LAST_NAME = "foo";
    private final Integer FAKE_TIMESTAMP = 343354;
    private final String FAKE_CITY = "Corseaux";*/

    @Before
    public void Before(){
        ExecutionMode.getInstance().setTest(true);
        FakeDatabase.getInstance().createBasicDatabase();

        u = new User(FakeAuthentication.getInstance().getUid());
        /*u.set(User.StringFields.firstName, FAKE_FIRST_NAME);
        u.set(User.StringFields.lastName, FAKE_LAST_NAME);
        u.set(User.StringFields.email, FAKE_EMAIL);
        u.set(User.StringFields.city, FAKE_EMAIL);
        u.set(User.IntegerFields.creationTimeStamp, FAKE_TIMESTAMP);
        u.set(User.StringFields.city, FAKE_CITY);
        User.UserGender.setGender(u, User.UserGender.M);*/

        FakeDatabase.getInstance().updateOnDb(u);
    }
    @Test
    public void fragmentCanBeInstantiated() {
        mFragmentTestRule.launchActivity(null);
        onView(ViewMatchers.withId(R.id.editProfileTitle)).check(matches(isDisplayed()));
    }

//    @Test
//    public void userHasCorrectFirstName() {
//        mFragmentTestRule.launchActivity(null);
//        onView(withId(R.id.profFirstNameEdit)).check(matches(withText(FakeAuthentication.FIRST_NAME)));
//
//    }

    @Test
    public void userCanEditFirstName() {
        mFragmentTestRule.launchActivity(null);
        String newFirstName = "tata";
        onView(withId(R.id.profFirstNameEdit)).perform(scrollTo(), replaceText(newFirstName)).perform(closeSoftKeyboard());
        onView(withId(R.id.commitChanges)).perform(scrollTo(), click());

        FakeDatabase.getInstance().updateFromDb(u).addOnSuccessListener(t -> {
            assertEquals(newFirstName, u.get(User.StringFields.firstName));
            u.set(User.StringFields.firstName, FakeAuthentication.FIRST_NAME);
            FakeDatabase.getInstance().updateOnDb(u);
        });


    }

    @Test
    public void userHasCorrectLastName() {
        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.profLastNameEdit)).check(matches(withText(FakeAuthentication.LAST_NAME)));

    }

    @Test
    public void userCanEditLastName() {
        mFragmentTestRule.launchActivity(null);
        String newLastname = "fofofofo";
        onView(withId(R.id.profLastNameEdit)).perform(replaceText(newLastname)).perform(closeSoftKeyboard());
        onView(withId(R.id.commitChanges)).perform(scrollTo(), click());
        FakeDatabase.getInstance().updateFromDb(u).addOnSuccessListener(t -> {
            assertEquals(newLastname, u.get(User.StringFields.lastName));
            u.set(User.StringFields.lastName, FakeAuthentication.LAST_NAME);
            FakeDatabase.getInstance().updateOnDb(u);
        });
    }

    @Test
    public void userCanEditCity() {
        mFragmentTestRule.launchActivity(null);
        String newCity = "Geneva";
        onView(withId(R.id.profCityEdit)).perform(replaceText(newCity)).perform(closeSoftKeyboard());
        onView(withId(R.id.commitChanges)).perform(scrollTo(), click());
        FakeDatabase.getInstance().updateFromDb(u).addOnSuccessListener(t -> {
            assertEquals(newCity, u.get(User.StringFields.city));
            u.set(User.StringFields.city, FakeAuthentication.CITY);
            FakeDatabase.getInstance().updateOnDb(u);
        });
    }

    @Test
    public void userHasCorrectCity() {
        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.profCityEdit)).check(matches(withText(FakeAuthentication.CITY)));

    }

    @Test
    public void userHasCorrectGender() {
        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.profGenderFEdit)).check(matches(withText(u.get(User.StringFields.sex))));
    }

    /*@Test
    public void userCanEditGender() {
        User.UserGender.setGender(u,User.UserGender.M);
        FakeDatabase.getInstance().updateOnDb(u);
        mFragmentTestRule.launchActivity(null);

        onView(withId(R.id.profGenderFEdit)).perform(scrollTo(), click()).perform(closeSoftKeyboard());
        onView(withId(R.id.commitChanges)).perform(scrollTo(), click());


        FakeDatabase.getInstance().updateFromDb(u).addOnSuccessListener(t -> {
            assertEquals("F", u.get(User.StringFields.sex));
            User.UserGender.setGender(u, FakeAuthentication.GENDER);
            FakeDatabase.getInstance().updateOnDb(u);
        });

    }*/
}
