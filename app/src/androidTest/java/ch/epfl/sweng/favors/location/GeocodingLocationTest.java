package ch.epfl.sweng.favors.location;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.favors.FavorCreateFragment;
import ch.epfl.sweng.favors.favors.FavorsMap;
import ch.epfl.sweng.favors.utils.FragmentTestRule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;

@RunWith(AndroidJUnit4.class)
public class GeocodingLocationTest {

    @Rule
    public FragmentTestRule<FavorsMap> mFragmentTestRule = new FragmentTestRule<>(FavorsMap.class);


    @Test
    public void getAddressFromLocationTest(){
        mFragmentTestRule.launchActivity(null);
        onView(ViewMatchers.withId(R.id.mapView)).check(matches(isDisplayed()));
        if (Looper.myLooper() == null) {
            Looper.prepare();
        }

        GeocodingLocation locationAddress = new GeocodingLocation();
        locationAddress.getAddressFromLocation("Lausanne", mFragmentTestRule.getFragment().getContext(), new Handler());
    }
}
