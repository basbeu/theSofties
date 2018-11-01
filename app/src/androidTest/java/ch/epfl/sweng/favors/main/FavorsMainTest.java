package ch.epfl.sweng.favors.main;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiSelector;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

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

        if(allowButton.exists()){
            allowButton.click();
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

        if(allowButton.exists()){
            allowButton.click();
        }

        UiObject login = device.findObject(new UiSelector().text("LOGIN"));

        if(login.exists()){
            login.click();
        }
    }

    @Test
    public void getsContext() throws Exception{
        activityActivityTestRule.launchActivity(null);
        UiObject allowButton = device.findObject(new UiSelector().text("ALLOW"));

        if(allowButton.exists()){
            allowButton.click();
        }
        FavorsMain.getContext();
    }
}
