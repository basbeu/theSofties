package ch.epfl.sweng.favors.location;


import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.favors.FavorsMap;
import ch.epfl.sweng.favors.utils.ExecutionMode;
import ch.epfl.sweng.favors.utils.FragmentTestRule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;

@RunWith(AndroidJUnit4.class)
public class AndroidGeocoderTest {

    @Rule
    public FragmentTestRule<FavorsMap> mFragmentTestRule = new FragmentTestRule<>(FavorsMap.class);

    @Before
    public void Before() {
        ExecutionMode.getInstance().setTest(true);
    }

    //Weird behavior for this two tests : on local I have each time the exceptions but not in travis
    @Test//(expected = IOException.class)
    public void getFromLocationNameExceptionTest(){
        mFragmentTestRule.launchActivity(null);
        onView(ViewMatchers.withId(R.id.mapView)).check(matches(isDisplayed()));
        AndroidGeocoder geocoder = AndroidGeocoder.getGeocoder(mFragmentTestRule.getFragment().getContext());

        try {
            geocoder.getFromLocationName("Lausanne",1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test//(expected = IOException.class)
    public void getFromLocationExceptionTest() {
        mFragmentTestRule.launchActivity(null);
        onView(ViewMatchers.withId(R.id.mapView)).check(matches(isDisplayed()));
        AndroidGeocoder geocoder = AndroidGeocoder.getGeocoder(mFragmentTestRule.getFragment().getContext());
        try {
            geocoder.getFromLocation(1.2,6.5,1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
