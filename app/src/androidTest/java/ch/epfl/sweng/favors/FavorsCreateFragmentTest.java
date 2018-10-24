package ch.epfl.sweng.favors;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import ch.epfl.sweng.favors.database.Favor;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class FavorsCreateFragmentTest {

    @Rule
    public FragmentTestRule<FavorCreateFragment> mFragmentTestRule = new FragmentTestRule<>(FavorCreateFragment.class);
    @Mock Favor favor;

    @Before
    public void Before(){

    }

    @Test
    public void fragment_can_be_instantiated() {
        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.createFavorTitle)).check(matches(isDisplayed()));
    }

    @Test
    public void isStringValidTest(){

        assertEquals(FavorCreateFragment.isStringValid("blablabla"), true);

    }

    @Test
    public void allFavorsValidisFalse(){
        mFragmentTestRule.launchActivity(null);
        assertEquals(mFragmentTestRule.getFragment().allFavorFieldsValid(), false);
    }

    @Test
    public void createFavorIfValidTest(){
        mFragmentTestRule.launchActivity(null);
        mFragmentTestRule.getFragment().createFavorIfValid(favor);
    }



    
}
