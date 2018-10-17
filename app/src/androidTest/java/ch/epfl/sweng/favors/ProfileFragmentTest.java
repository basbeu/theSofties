package ch.epfl.sweng.favors;

import android.support.test.runner.AndroidJUnit4;

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
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class ProfileFragmentTest {

    @Rule public FragmentTestRule<ProfileFragment> mFragmentTestRule = new FragmentTestRule<>(ProfileFragment.class);
    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    // @Mock User fakeUser;
    @Mock private FirebaseUser fbFakeUser;
    @Mock private FirebaseFirestore fstore;
    @Mock private FirebaseAuth fakeAuth;
    private final String FAKEEMAIL = "thisisatestemail@email.com";
    private final String FAKEFIRSTNAME = "Toto";
    private final String FAKELASTNAME = "Tutu";

    @Before
    public void Before(){
        User fakeUser = new User(fakeAuth);
        User.setMain(fakeUser);
        when(fakeAuth.getCurrentUser()).thenReturn(fbFakeUser);
        when(fbFakeUser.getEmail()).thenReturn(FAKEEMAIL);
        fakeUser.set(User.StringFields.firstName, FAKEFIRSTNAME);
        fakeUser.set(User.StringFields.lastName, FAKELASTNAME);
        fakeUser.set(User.StringFields.email, FAKEEMAIL);

    }

    @Test
    public void fragment_can_be_instantiated() {
        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.profileTitle)).check(matches(isDisplayed()));
    }


    @Test
    public void firstName_is_displayed(){
        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.profFirstName)).check(matches(withText(FAKEFIRSTNAME)));
    }

    @Test
    public void lasttName_is_displayed(){
        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.profLastName)).check(matches(withText(FAKELASTNAME)));
    }

    @Test
    public void email_is_displayed(){
        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.profEmail)).check(matches(withText(FAKEEMAIL)));
    }

}
