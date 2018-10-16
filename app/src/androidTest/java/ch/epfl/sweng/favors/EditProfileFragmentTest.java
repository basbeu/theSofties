package ch.epfl.sweng.favors;

import android.support.test.runner.AndroidJUnit4;

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
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.when;


@RunWith(AndroidJUnit4.class)


public class EditProfileFragmentTest {

    @Rule public FragmentTestRule<EditProfileFragment> mFragmentTestRule = new FragmentTestRule<>(EditProfileFragment.class);
    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

   // @Mock User fakeUser;
    @Mock FirebaseUser fbFakeUser;
    @Mock FirebaseFirestore fstore;
    @Mock FirebaseAuth fakeAuth;

    @Before
    public void Before(){

        User fakeUser = new User(fakeAuth);
        User.setMain(fakeUser);
        when(fakeAuth.getCurrentUser()).thenReturn(fbFakeUser);
        when(fbFakeUser.getEmail()).thenReturn("thisisatestemail@email.com");
    }
    @Test
    public void fragment_can_be_instantiated() {


        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.profileTitle)).check(matches(isDisplayed()));
    }
    
}
