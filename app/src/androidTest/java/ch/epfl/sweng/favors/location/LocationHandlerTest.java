package ch.epfl.sweng.favors.location;

import android.support.test.espresso.contrib.PickerActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.widget.DatePicker;

import com.google.firebase.firestore.GeoPoint;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.database.FakeDatabase;
import ch.epfl.sweng.favors.database.Favor;
import ch.epfl.sweng.favors.database.User;
import ch.epfl.sweng.favors.favors.FavorCreateFragment;
import ch.epfl.sweng.favors.utils.ExecutionMode;
import ch.epfl.sweng.favors.utils.FragmentTestRule;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.assertEquals;

@RunWith(AndroidJUnit4.class)
public class LocationHandlerTest {
    private UiDevice device;

    private User u;

    private final String FAKE_UID = "sklfklalsdj";
    private final String FAKE_EMAIL = "thisisatestemail@email.com";
    private final String FAKE_FIRST_NAME = "toto";
    private final String FAKE_LAST_NAME = "foo";
    private final String FAKE_SEX = "M";
    private final Integer FAKE_TIMESTAMP = 343354;
    private Favor f;
    private String FAKE_DOC_ID  = "klsafdjdlksdf";
    private String FAKE_TITLE = "Fake title";
    private String FAKE_OWNER_ID = "fklskkdja";
    private String FAKE_DESCRIPTION = "This is a fake description";
    private String FAKE_LOCATION_CITY = "Lausanne";
    private Boolean FAKE_IS_OPEN = true;
    private Double lat = 34.1786998;
    private Double lon = 86.6154153;
    private Object FAKE_LOCATION_OBJECT = new GeoPoint(lat, lon);
    private Object FAKE_LOCATION_OBJECT2 = new GeoPoint(46.5333,6.6667);

    @Before
    public void Before() {
        ExecutionMode.getInstance().setTest(true);
        FakeDatabase.getInstance().createBasicDatabase();

        u = new User(FAKE_UID);
        u.set(User.StringFields.firstName, FAKE_FIRST_NAME);
        u.set(User.StringFields.lastName, FAKE_LAST_NAME);
        u.set(User.StringFields.email, FAKE_EMAIL);
        u.set(User.StringFields.city, FAKE_EMAIL);
        u.set(User.IntegerFields.creationTimeStamp, FAKE_TIMESTAMP);
        User.UserGender.setGender(u, User.UserGender.M);

        FakeDatabase.getInstance().updateOnDb(u);
        f = new Favor(FAKE_DOC_ID);

        f.set(Favor.StringFields.ownerID, FAKE_OWNER_ID);
        f.set(Favor.StringFields.description, FAKE_DESCRIPTION);
        f.set(Favor.StringFields.title, FAKE_TITLE);
        f.set(Favor.StringFields.locationCity, FAKE_LOCATION_CITY);
        f.set(Favor.BooleanFields.isOpen, FAKE_IS_OPEN);
        f.set(Favor.ObjectFields.location, FAKE_LOCATION_OBJECT);

        FakeDatabase.getInstance().updateOnDb(f);
    }

    @Test
    public void getDistanceTest() {
        String test = LocationHandler.distanceBetween((GeoPoint)FAKE_LOCATION_OBJECT);
        assertEquals("6635km away", test);
    }

    @Test
    public void getDistanceTest2() {
        String test = LocationHandler.distanceBetween((GeoPoint)FAKE_LOCATION_OBJECT2);
        assertEquals("0m away", test);
    }

    @Test
    public void getDistanceTest3() {
        float test = LocationHandler.distanceTo((GeoPoint)FAKE_LOCATION_OBJECT2);
        float expexcted = 0;
        assertEquals(expexcted, test);
    }
}
