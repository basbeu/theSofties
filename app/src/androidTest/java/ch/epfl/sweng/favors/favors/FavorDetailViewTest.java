package ch.epfl.sweng.favors.favors;

import android.content.Intent;
import android.os.Bundle;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.database.Database;
import ch.epfl.sweng.favors.database.FakeDatabase;
import ch.epfl.sweng.favors.database.Favor;
import ch.epfl.sweng.favors.utils.ExecutionMode;
import ch.epfl.sweng.favors.utils.FragmentTestRule;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.fail;

public class FavorDetailViewTest {

    private UiDevice device;

    @Rule public FragmentTestRule<FavorDetailView> mFragmentTestRule = new FragmentTestRule<FavorDetailView>(FavorDetailView.class);


    @Before
    public void Before(){
        ExecutionMode.getInstance().setTest(true);
        FakeDatabase.getInstance().createBasicDatabase();
    }


    //@Ignore("Testing interface not available")
    @Test
    public void imInterestedToast(){

        mFragmentTestRule.launchActivity(null);

        Favor f1 = new Favor("F1");

        f1.set(Favor.StringFields.ownerID, "U3");
        f1.set(Favor.StringFields.category, "Hand help");
        f1.set(Favor.StringFields.deadline, "12.12.20");
        f1.set(Favor.StringFields.description, "I need help to get rid of an old friend.");
        f1.set(Favor.StringFields.title, "KILL THE BATMAN");
        f1.set(Favor.StringFields.locationCity, "Gotham City");
        f1.set(Favor.StringFields.ownerEmail, "toto.tata@pipi.com");

        Database.getInstance().updateOnDb(f1);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mFragmentTestRule.getFragment().setFields(f1);


        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(ViewMatchers.withId(R.id.interestedButton)).perform(scrollTo(), click());
        onView(withText("We will inform the poster of the add that you are interested to help!")).inRoot(withDecorView(not(is(mFragmentTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void imInterestedToastFailed(){

        ExecutionMode.getInstance().setInvalidAuthTest(true);

        mFragmentTestRule.launchActivity(null);

        Favor f1 = new Favor("F1");

        f1.set(Favor.StringFields.ownerID, "U3");
        f1.set(Favor.StringFields.category, "Hand help");
        f1.set(Favor.StringFields.deadline, "12.12.20");
        f1.set(Favor.StringFields.description, "I need help to get rid of an old friend.");
        f1.set(Favor.StringFields.title, "KILL THE BATMAN");
        f1.set(Favor.StringFields.locationCity, "Gotham City");
        f1.set(Favor.StringFields.ownerEmail, "toto.tata@pipi.com");

        Database.getInstance().updateOnDb(f1);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mFragmentTestRule.getFragment().setFields(f1);


        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(ViewMatchers.withId(R.id.interestedButton)).perform(scrollTo(), click());
        onView(withText("Sorry an error occured, try again later...")).inRoot(withDecorView(not(is(mFragmentTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));

        ExecutionMode.getInstance().setInvalidAuthTest(false);
    }

    @Test
    public void imageDisplayed(){
        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.imageView)).perform(scrollTo()).check(matches(isDisplayed()));
    }



    @Test
    public void image2Displayed(){

        /*mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.imageView2)).perform(scrollTo()).check(matches(isDisplayed()));*/
    }

    @Ignore
    @Test
    public void tokensAmountDisplayed(){
        mFragmentTestRule.launchActivity(null);
        Favor f1 = new Favor("F1");

        f1.set(Favor.StringFields.ownerID, "U3");
        f1.set(Favor.StringFields.category, "Hand help");
        f1.set(Favor.StringFields.deadline, "12.12.20");
        f1.set(Favor.StringFields.description, "I need help to get rid of an old friend.");
        f1.set(Favor.StringFields.title, "KILL THE BATMAN");
        f1.set(Favor.StringFields.locationCity, "Gotham City");
        f1.set(Favor.StringFields.ownerEmail, "toto.tata@pipi.com");

        Database.getInstance().updateOnDb(f1);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mFragmentTestRule.getFragment().setFields(f1);


        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.favTokAmmount)).perform(scrollTo()).check(matches(isDisplayed()));
    }

    @Test
    public void reportAbusiveAddToast(){

        mFragmentTestRule.launchActivity(null);

        Favor f1 = new Favor("F1");

        f1.set(Favor.StringFields.ownerID, "U3");
        f1.set(Favor.StringFields.category, "Hand help");
        f1.set(Favor.StringFields.deadline, "12.12.20");
        f1.set(Favor.StringFields.description, "I need help to get rid of an old friend.");
        f1.set(Favor.StringFields.title, "KILL THE BATMAN");
        f1.set(Favor.StringFields.locationCity, "Gotham City");
        f1.set(Favor.StringFields.ownerEmail, "toto.tata@pipi.com");

        Database.getInstance().updateOnDb(f1);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mFragmentTestRule.getFragment().setFields(f1);


        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.favReportAbusiveAdd)).perform(scrollTo(), click());
        onView(withText("issue has been reported! Sorry for the inconvenience")).inRoot(withDecorView(not(is(mFragmentTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

//    @Test
//    public void setFieldsTest(){
//        Bundle bundle = new Bundle();
//        bundle.putString(FavorDetailView.FAVOR_ID,"F1");
//        mFragmentTestRule.launchActivity(new Intent(bundle));
//
//        onView(withId(R.id.favTokAmmount)).check(matches(isDisplayed()));
//    }

}
