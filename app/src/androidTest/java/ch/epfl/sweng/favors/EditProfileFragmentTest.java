package ch.epfl.sweng.favors;

import android.support.test.runner.AndroidJUnit4;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import ch.epfl.sweng.favors.database.User;

import static org.mockito.Mockito.when;


@RunWith(AndroidJUnit4.class)


public class EditProfileFragmentTest {

    @Rule public FragmentTestRule<EditProfileFragment> mFragmentTestRule = new FragmentTestRule<>(EditProfileFragment.class);
    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock User fakeUser;

    @Before
    public void Before(){
        User.setMain(fakeUser);
        //when(fakeUser.getInstance().getCurrentUser().getEmail()).thenReturn("email@test.toto");
    }
    @Test
    public void fragment_can_be_instantiated() {

        //mFragmentTestRule.launchActivity(null);
        //onView(withId(R.id.profileTitle)).check(matches(isDisplayed()));
    }
    
}
