package ch.epfl.sweng.favors;

import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import ch.epfl.sweng.favors.database.DatabaseHandler;
import ch.epfl.sweng.favors.database.User;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


@RunWith(AndroidJUnit4.class)
public class FavorsCreateFragmentTest {

    @Rule
    public FragmentTestRule<FavorCreateFragment> mFragmentTestRule = new FragmentTestRule<>(FavorCreateFragment.class);

    @Test
    public void fragment_can_be_instantiated() {

        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.createFavorTitle)).check(matches(isDisplayed()));
    }

    @Test
    public void isStringValide_fails_on_null(){
        FavorCreateFragment fc = new FavorCreateFragment();
        try {
            Method stringvalide = FavorCreateFragment.class.getDeclaredMethod("isStringValid", String.class);
            stringvalide.setAccessible(true);
            boolean b = (boolean)stringvalide.invoke(fc, "");
            assertThat(b,is(false));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            assertThat("Error",true,is(false));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            assertThat("Error",true,is(false));
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            assertThat("Error",true,is(false));
        }
    }

    @Test
    public void isStringValide_succeds_on_HelloWorld(){
        FavorCreateFragment fc = new FavorCreateFragment();
        try {
            Method stringvalide = FavorCreateFragment.class.getDeclaredMethod("isStringValid", String.class);
            stringvalide.setAccessible(true);
            boolean b = (boolean)stringvalide.invoke(fc, "HelloWorld");
            assertThat(b,is(true));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            assertThat("Error",true,is(false));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            assertThat("Error",true,is(false));
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            assertThat("Error",true,is(false));
        }
    }

    
}
