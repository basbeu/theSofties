package ch.epfl.sweng.favors;

import android.support.test.runner.AndroidJUnit4;

import com.google.firebase.auth.FirebaseAuth;

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
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class EditProfileFragmentTest {
    @Rule
    public FragmentTestRule<EditProfileFragment> mFragmentTestRule = new FragmentTestRule<>(EditProfileFragment.class);

   /* @Mock
    User fakeUser;
    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();*/


    @Test
    public void fragment_can_be_instantiated() {
        //when(fakeUser.getMain().set(User.StringFields.email, FirebaseAuth.getInstance().getCurrentUser().getEmail())).thenReturn("Toto");
       // mFragmentTestRule.launchActivity(null);
        //onView(withId(R.id.profileTitle)).check(matches(isDisplayed()));
    }
    
}
