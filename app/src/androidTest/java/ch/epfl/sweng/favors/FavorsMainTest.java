package ch.epfl.sweng.favors;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.v4.app.ActivityCompat;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.assertEquals;

@RunWith(AndroidJUnit4.class)



public class FavorsMainTest {
    @Rule public ActivityTestRule<FavorsMain> activityActivityTestRule = new ActivityTestRule<>(FavorsMain.class);
    private UiDevice device;

    @Before
    public void Before(){
        device = UiDevice.getInstance(getInstrumentation());
    }


    @Test
    public void canRegister() throws Exception {
        //assertViewWithTextIsVisible(UiDevice.getInstance(getInstrumentation()), "ALLOW");
        //assertViewWithTextIsVisible(UiDevice.getInstance(getInstrumentation()), "DENY");

        UiObject allowButton = device.findObject(new UiSelector().text("ALLOW"));
        UiObject denyButton = device.findObject(new UiSelector().text("DENY"));

        if(denyButton.exists()){
           denyButton.click();
        }
        else if(allowButton.exists()){
            allowButton.click();
        }


        onView(withId(R.id.registerButton)).perform(click());
        onView(withId(R.id.authentificationButton)).check(matches(isDisplayed()));
        onView(withId(R.id.loginMessageText)).check(matches(withText("Welcome here! Just some small steps...")));

    }

    @Test
    public void canLogin() throws Exception {
        //assertViewWithTextIsVisible(UiDevice.getInstance(getInstrumentation()), "ALLOW");
        //assertViewWithTextIsVisible(UiDevice.getInstance(getInstrumentation()), "DENY");

        //denyCurrentPermission(UiDevice.getInstance(getInstrumentation()));

        UiObject allowButton = device.findObject(new UiSelector().text("ALLOW"));
        UiObject denyButton = device.findObject(new UiSelector().text("DENY"));

        if(denyButton.exists()){
            denyButton.click();
        }
        else if(allowButton.exists()){
            allowButton.click();
        }

        onView(withId(R.id.loginButton)).perform(click());
        onView(withId(R.id.authentificationButton)).check(matches(isDisplayed()));
        onView(withId(R.id.loginMessageText)).check(matches(withText("Please enter your login informations:")));

    }



    public static void assertViewWithTextIsVisible(UiDevice device, String text) {
        UiObject allowButton = device.findObject(new UiSelector().text(text));
        if (!allowButton.exists()) {
            throw new AssertionError("View with text <" + text + "> not found!");
        }
    }

    public static void denyCurrentPermission(UiDevice device) throws UiObjectNotFoundException {
        UiObject denyButton = device.findObject(new UiSelector().text("DENY"));
        denyButton.click();
    }

    public static void allowCurrentPermission(UiDevice device) throws UiObjectNotFoundException {
        UiObject allowButton = device.findObject(new UiSelector().text("ALLOW"));
        allowButton.click();
    }




}
