package ch.epfl.sweng.favors.favors;


import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.uiautomator.UiDevice;


import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.database.Database;
import ch.epfl.sweng.favors.database.FakeDatabase;
import ch.epfl.sweng.favors.database.Favor;
import ch.epfl.sweng.favors.database.User;
import ch.epfl.sweng.favors.utils.ExecutionMode;
import ch.epfl.sweng.favors.utils.FragmentTestRule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.mockito.Mockito.when;


public class FavorDetailViewTest {
    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();


    private UiDevice device;

    @Rule public FragmentTestRule<FavorDetailView> mFragmentTestRule = new FragmentTestRule<FavorDetailView>(FavorDetailView.class);
    @Mock private User mainUser;

    @Before
    public void Before(){
        ExecutionMode.getInstance().setTest(true);
        FakeDatabase.getInstance().createBasicDatabase();
    }


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
        //onView(withText("We will inform the poster of the add that you are interested to help!")).inRoot(withDecorView(not(is(mFragmentTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void imInterestedToastFailed(){

        Favor f1 = new Favor("F1");

        f1.set(Favor.StringFields.ownerID, "U3");
        f1.set(Favor.StringFields.category, "Hand help");
        f1.set(Favor.StringFields.deadline, "12.12.20");
        f1.set(Favor.StringFields.description, "I need help to get rid of an old friend.");
        f1.set(Favor.StringFields.title, "KILL THE BATMAN");
        f1.set(Favor.StringFields.locationCity, "Gotham City");
        f1.set(Favor.StringFields.ownerEmail, "toto.tata@pipi.com");

        Database.getInstance().updateOnDb(f1);


        mFragmentTestRule.launchActivity(null);


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

        ExecutionMode.getInstance().setInvalidAuthTest(false);
    }

    @Test
    public void imageDisplayed(){
        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.imageView)).perform(scrollTo()).check(matches(isDisplayed()));
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
        //onView(withText("issue has been reported! Sorry for the inconvenience")).inRoot(withDecorView(not(is(mFragmentTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Ignore
    public void successfulPayment(){

        User u1 = new User("U1");
        u1.set(User.StringFields.firstName, "Toto");
        u1.set(User.StringFields.lastName, "Lolo");
        u1.set(User.StringFields.email, "toto.lolo@test.com");
        u1.set(User.StringFields.city, "Tombouctou");
        User.UserGender.setGender(u1, User.UserGender.M);

        User u3 = new User("U3");
        u3.set(User.StringFields.firstName, "Harvey");
        u3.set(User.StringFields.lastName, "Dent");
        u3.set(User.StringFields.email, "harvey.dent@gotham.com");
        u3.set(User.StringFields.city, "Arkham Asylum");
        User.UserGender.setGender(u3, User.UserGender.M);

        when(mainUser.getMain()).thenReturn(u3);


        ArrayList<String> selectedPeople1 = new ArrayList<>();
        selectedPeople1.add(u1.getId());
        ArrayList<String> interestedPeople1 = new ArrayList<>();
        interestedPeople1.add(u1.getId());

        mFragmentTestRule.launchActivity(null);
        Favor f1 = new Favor("F1");

        f1.set(Favor.StringFields.ownerID, "U3");
        f1.set(Favor.StringFields.category, "Hand help");
        f1.set(Favor.StringFields.deadline, "12.12.20");
        f1.set(Favor.StringFields.description, "I need help to get rid of an old friend.");
        f1.set(Favor.StringFields.title, "KILL THE BATMAN");
        f1.set(Favor.StringFields.locationCity, "Gotham City");
        f1.set(Favor.StringFields.ownerEmail, "toto.tata@pipi.com");
        f1.set(Favor.ObjectFields.interested, interestedPeople1);
        f1.set(Favor.LongFields.nbPerson, 1L);
        f1.set(Favor.LongFields.tokenPerPerson, 1L);


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

        onView(withId(R.id.payButton)).perform(scrollTo(), click());

    }

    @Ignore
    public void paymentOnEmptyList(){
        mFragmentTestRule.launchActivity(null);

        User u1 = new User("U1");


        u1.set(User.StringFields.firstName, "Toto");
        u1.set(User.StringFields.lastName, "Lolo");
        u1.set(User.StringFields.email, "toto.lolo@test.com");
        u1.set(User.StringFields.city, "Tombouctou");
        User.UserGender.setGender(u1, User.UserGender.M);

        //when(mainUser.getMain()).thenReturn(u1);


        Favor f2 = new Favor("F2");

        ArrayList<String> selectedPeople2 = new ArrayList<>();
        ArrayList<String> interestedPeople2 = new ArrayList<>();

        f2.set(Favor.StringFields.ownerID, "U1");
        f2.set(Favor.StringFields.category, "Cooking");
        f2.set(Favor.StringFields.deadline, "10.01.19");
        f2.set(Favor.StringFields.description, "Cook me a cookie");
        f2.set(Favor.StringFields.title, "I am hungry pls hurry");
        f2.set(Favor.StringFields.locationCity, "Tombouctou");
        f2.set(Favor.StringFields.ownerEmail, "toto.tata@pipi.com");
        f2.set(Favor.ObjectFields.interested, interestedPeople2);
        f2.set(Favor.LongFields.nbPerson, 1L);
        f2.set(Favor.LongFields.tokenPerPerson, 1L);

        Database.getInstance().updateOnDb(f2);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mFragmentTestRule.getFragment().setFields(f2);


        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.payButton)).perform(scrollTo(), click());

    }

    @Ignore
    public void paymentOnTooManyPeople(){
        User u1 = new User("U1");
        u1.set(User.StringFields.firstName, "Toto");
        u1.set(User.StringFields.lastName, "Lolo");
        u1.set(User.StringFields.email, "toto.lolo@test.com");
        u1.set(User.StringFields.city, "Tombouctou");
        User.UserGender.setGender(u1, User.UserGender.M);

        User u2 = new User("U2");
        u2.set(User.StringFields.firstName, "Bruce");
        u2.set(User.StringFields.lastName, "Wayne");
        u2.set(User.StringFields.email, "bruce.wayne@waynecorp.com");
        u2.set(User.StringFields.city, "Gotham City");
        User.UserGender.setGender(u2, User.UserGender.M);

        User u3 = new User("U3");
        u3.set(User.StringFields.firstName, "Harvey");
        u3.set(User.StringFields.lastName, "Dent");
        u3.set(User.StringFields.email, "harvey.dent@gotham.com");
        u3.set(User.StringFields.city, "Arkham Asylum");
        User.UserGender.setGender(u3, User.UserGender.M);

        when(mainUser.getMain()).thenReturn(u3);

        mFragmentTestRule.launchActivity(null);
        Favor f3 = new Favor("F3");

        ArrayList<String> selectedPeople3 = new ArrayList<>();
        selectedPeople3.add(u1.getId());
        selectedPeople3.add(u2.getId());
        ArrayList<String> interestedPeople3 = new ArrayList<>();
        interestedPeople3.add(u1.getId());
        interestedPeople3.add(u2.getId());

        f3.set(Favor.StringFields.ownerID, "U3");
        f3.set(Favor.StringFields.category, "Riddle");
        f3.set(Favor.StringFields.deadline, "12.12.20");
        f3.set(Favor.StringFields.description, "We're five little items of an everyday sort; you'll find us all in 'a tennis court'");
        f3.set(Favor.StringFields.title, "TICK TOK");
        f3.set(Favor.StringFields.locationCity, "Gotham City");
        f3.set(Favor.StringFields.ownerEmail, "toto.tata@pipi.com");
        f3.set(Favor.ObjectFields.interested, interestedPeople3);
        f3.set(Favor.LongFields.nbPerson, 1L);
        f3.set(Favor.LongFields.tokenPerPerson, 1L);

        Database.getInstance().updateOnDb(f3);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mFragmentTestRule.getFragment().setFields(f3);


        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.payButton)).perform(scrollTo(), click());

    }


}
