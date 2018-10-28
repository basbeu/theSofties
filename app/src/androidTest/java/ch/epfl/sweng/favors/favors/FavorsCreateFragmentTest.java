package ch.epfl.sweng.favors.favors;

import android.support.test.espresso.contrib.PickerActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.widget.DatePicker;
import android.widget.Spinner;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.database.FakeDatabase;
import ch.epfl.sweng.favors.database.Favor;
import ch.epfl.sweng.favors.database.Interest;
import ch.epfl.sweng.favors.utils.ExecutionMode;
import ch.epfl.sweng.favors.utils.FragmentTestRule;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
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
import static java.lang.Thread.sleep;

@RunWith(AndroidJUnit4.class)
public class FavorsCreateFragmentTest {
    private UiDevice device;

    @Rule
    public FragmentTestRule<FavorCreateFragment> mFragmentTestRule = new FragmentTestRule<>(FavorCreateFragment.class);

    @Before
    public void Before(){
        ExecutionMode.getInstance().setTest(true);
        FakeDatabase.getInstance().createBasicDatabase();
        device = UiDevice.getInstance(getInstrumentation());
    }

    @Test
    public void fragment_can_be_instantiated() {
        mFragmentTestRule.launchActivity(null);
        onView(ViewMatchers.withId(R.id.createFavorTitle)).check(matches(isDisplayed()));
    }

    @Test
    public void isStringValidTest(){

        assertEquals(FavorCreateFragment.isStringValid("blablabla"), true);

    }

    @Test
    public void changesTitle() {
        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.titleFavor)).perform(typeText("title")).check(matches(withText("title")));
    }

    @Test
    public void changesDescription() {
        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.descriptionFavor)).perform(typeText("description")).check(matches(withText("description")));
    }

    @Test
    public void changesLocation() {
        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.locationFavor)).perform(typeText("location")).check(matches(withText("location")));
    }

    @Test
    public void interestSpinnerTest(){
        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.categoryFavor)).perform(scrollTo(),click());
    }

    @Test
    public void testDatePicker() {
        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.deadlineFavor)).perform(scrollTo(), click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2000,10,23));
    }

    @Test
    public void createFavor() throws UiObjectNotFoundException, InterruptedException {
        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.titleFavor)).perform(typeText("Test Expert")).perform(closeSoftKeyboard()).check(matches(withText("Test Expert")));
        onView(withId(R.id.descriptionFavor)).perform(typeText("Help me with my tests")).perform(closeSoftKeyboard()).check(matches(withText("Help me with my tests")));
        onView(withId(R.id.locationFavor)).perform(typeText("TestCity")).perform(closeSoftKeyboard()).check(matches(withText("TestCity")));
        onView(withId(R.id.deadlineFavor)).perform(scrollTo(), click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2050,10,23));
        UiObject allowButton = device.findObject(new UiSelector().text("OK"));
        if (allowButton != null) {
            allowButton.click();
        }

//        Favor f5 = new Favor("F5");

//        f5.set(Favor.StringFields.ownerID, "U20");
//        f5.set(Favor.StringFields.category, "Hand help");
//        f5.set(Favor.StringFields.deadline, "12.12.20");
//        f5.set(Favor.StringFields.description, "I need help to get rid of an old friend.");
//        f5.set(Favor.StringFields.title, "KILL THE BATMAN");
//        f5.set(Favor.StringFields.locationCity, "Gotham City");
//
//        FakeDatabase.getInstance().updateOnDb(f5);
//
//        Interest i3 = new Interest("I3");
//
//        i3.set(Interest.StringFields.title, "SWISS AVIATION");
//        i3.set(Interest.StringFields.description, "Fly to the sky");

//        FakeDatabase.getInstance().updateOnDb(i3);
//        onView(withId(R.id.categoryFavor)).perform(scrollTo(), click());
//        onData(Matchers.equalTo(Spinner.class.getName())).perform()

//        sleep(2000);
        onView(withId(R.id.addFavor)).perform(scrollTo(), click());

//        UiObject createButton = device.findObject(new UiSelector().text("CREATE THE FAVOR"));
//        if (createButton != null) {
//            createButton.click();
//        }

        onView(withId(R.id.titleFavor)).perform(scrollTo(),replaceText("Test Expert2")).perform(closeSoftKeyboard()).check(matches(withText("Test Expert2")));
        onView(withId(R.id.addFavor)).perform(scrollTo(), click());

    }

}
