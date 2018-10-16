package ch.epfl.sweng.favors;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;


@RunWith(AndroidJUnit4.class)


public class EditProfileFragmentTest {

    @Rule
    public FragmentTestRule<EditProfileFragment> mFragmentTestRule = new FragmentTestRule<>(EditProfileFragment.class);
    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

 
    @Test
    public void fragment_can_be_instantiated() {

        //mFragmentTestRule.launchActivity(null);
        //onView(withId(R.id.profileTitle)).check(matches(isDisplayed()));
    }
    
}
