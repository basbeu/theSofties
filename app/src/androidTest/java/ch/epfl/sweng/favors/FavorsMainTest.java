package ch.epfl.sweng.favors;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Looper;
import android.support.test.espresso.assertion.LayoutAssertions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiSelector;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.location.LocationServices;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.fail;
import static org.junit.Assert.fail;

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
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)



public class FavorsMainTest {
    @Rule public ActivityTestRule<FavorsMain> activityActivityTestRule = new ActivityTestRule<>(FavorsMain.class, true, false);

    private UiDevice device;

          

    @Before
    public void Before(){
        device = UiDevice.getInstance(getInstrumentation());
    }


    @Test
    public void canRegister() throws Exception {


        activityActivityTestRule.launchActivity(null);

        UiObject allowButton = device.findObject(new UiSelector().text("ALLOW"));
        UiObject denyButton = device.findObject(new UiSelector().text("DENY"));

        if(allowButton.exists() && denyButton.exists()){
            denyButton.click();
        }

        UiObject register = device.findObject(new UiSelector().text("REGISTER"));

        if(register.exists()){
            register.click();
        }
    }

    @Test
    public void canLogin() throws Exception {

        activityActivityTestRule.launchActivity(null);


        UiObject allowButton = device.findObject(new UiSelector().text("ALLOW"));
        UiObject denyButton = device.findObject(new UiSelector().text("DENY"));

        if(denyButton.exists() && allowButton.exists()){
            denyButton.click();
        }

        UiObject login = device.findObject(new UiSelector().text("LOGIN"));

        if(login.exists()){
            login.click();
        }
    }

}
