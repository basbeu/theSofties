package ch.epfl.sweng.favors.profile;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;

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

import ch.epfl.sweng.favors.authentication.FakeAuthentication;
import ch.epfl.sweng.favors.database.FakeDatabase;
import ch.epfl.sweng.favors.profile.ProfileFragment;
import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.main.LoggedInScreen;
import ch.epfl.sweng.favors.database.User;
import ch.epfl.sweng.favors.utils.ExecutionMode;
import ch.epfl.sweng.favors.utils.FragmentTestRule;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class ProfileFragmentTest {

    @Rule public FragmentTestRule<ProfileFragment> mFragmentTestRule = new FragmentTestRule<>(ProfileFragment.class);

    @Before
    public void Before(){
        ExecutionMode.getInstance().setTest(true);
        FakeDatabase.getInstance().createBasicDatabase();
    }

    @Test
    public void fragment_can_be_instantiated() {

        mFragmentTestRule.launchActivity(null);
        onView(ViewMatchers.withId(R.id.profileTitle)).check(matches(isDisplayed()));
    }


    @Test
    public void firstName_is_displayed(){
        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.profFirstName)).check(matches(withText(FakeAuthentication.FIRST_NAME)));
    }

    @Test
    public void lastName_is_displayed(){
        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.profLastName)).check(matches(withText(FakeAuthentication.LAST_NAME)));
    }

    @Test
    public void email_is_displayed(){
        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.profEmail)).check(matches(withText(FakeAuthentication.EMAIL)));
    }

    @Test
    public void editProfile(){
        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.editProfileButton)).check(matches(isDisplayed()));
  }

}
