package ch.epfl.sweng.favors.authentication;

import android.content.Intent;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.database.Database;
import ch.epfl.sweng.favors.database.User;
import ch.epfl.sweng.favors.main.FavorsMain;
import ch.epfl.sweng.favors.utils.ExecutionMode;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static java.lang.Thread.sleep;
import static junit.framework.TestCase.assertEquals;

/**
 * Used as container to test fragments in isolation with Espresso
 */


@RunWith(AndroidJUnit4.class)

//TODO: HANDLE NULL POINTER EXCEPTION SETUSERINFO.HAVA LINE 51 AND 56 TO MAKE TESTS PASS
public class SetUserInfoTest {

    @Rule public ActivityTestRule<SetUserInfo> activityActivityTestRule = new ActivityTestRule<SetUserInfo>(SetUserInfo.class, true,  false);

    private final String FAKEFIRSTNAME = "Bastien";
    private final String FAKELASTNAME = "Beuchat";
    private final String FAKECITY = "Lausanne";

    private User u;

    @Before
    public void Before(){
        ExecutionMode.getInstance().setTest(true);
        activityActivityTestRule.launchActivity(null);
        try {
            sleep(300);
        } catch (InterruptedException e) {

        }
        onView(withId(R.id.userFirstNameEdit)).perform(scrollTo(),replaceText(FAKEFIRSTNAME)).perform(closeSoftKeyboard());
        onView(withId(R.id.userLastNameEdit)).perform(scrollTo(),replaceText(FAKELASTNAME)).perform(closeSoftKeyboard());
        onView(withId(R.id.userCityEdit)).perform(scrollTo(),replaceText(FAKECITY)).perform(closeSoftKeyboard());
        onView(withId(R.id.profGenderFEdit)).perform(scrollTo(),click());
        onView(withId(R.id.submit)).perform(scrollTo(),click());
        u = new User(Authentication.getInstance().getUid());
        try {
            sleep(300);
        } catch (InterruptedException e) {

        }
    }

    @Test
    public void userHasCorrectFirstName() {

        assertEquals(FAKEFIRSTNAME, u.get(User.StringFields.firstName));

    }
    @Test
    public void userHasCorrectLastName() {
        assertEquals(FAKELASTNAME, u.get(User.StringFields.lastName));
    }
    @Test
    public void userHasCorrectCity() {
        assertEquals(FAKECITY, u.get(User.StringFields.city));

    }
    @Test
    public void userHasCorrectGender() {
        assertEquals("F", u.get(User.StringFields.sex));
    }

}
