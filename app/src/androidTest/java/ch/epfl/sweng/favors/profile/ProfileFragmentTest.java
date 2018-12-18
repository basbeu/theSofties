package ch.epfl.sweng.favors.profile;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.authentication.FakeAuthentication;
import ch.epfl.sweng.favors.database.FakeDatabase;
import ch.epfl.sweng.favors.database.Favor;
import ch.epfl.sweng.favors.database.ObservableArrayList;
import ch.epfl.sweng.favors.database.fields.DatabaseField;
import ch.epfl.sweng.favors.utils.ExecutionMode;
import ch.epfl.sweng.favors.utils.FragmentTestRule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ProfileFragmentTest {

    @Rule public FragmentTestRule<ProfileFragment> mFragmentTestRule = new FragmentTestRule<>(ProfileFragment.class);

    @Before
    public void Before(){
        ExecutionMode.getInstance().setTest(true);
        FakeDatabase.getInstance().createBasicDatabase();
    }

    @Test
    public void fragment_can_be_instantiated() {

        mFragmentTestRule.launchActivity(null);
        onView(ViewMatchers.withId(R.id.profileTitle)).check(matches(isDisplayed()));
    }


    @Test
    public void email_is_displayed(){
        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.profEmail)).check(matches(withText(FakeAuthentication.EMAIL)));
    }

    @Test
    public void editProfile(){
        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.editProfileButton)).check(matches(isDisplayed()));
    }

    @Test
    public void deleteAccount(){
        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.delete)).check(matches(isDisplayed()));
    }

    @Test
    public void cccdeleteUserIdInInterested(){
        mFragmentTestRule.launchActivity(null);
        String userID = "1234";
        Favor favor = new Favor();
        favor.set(Favor.StringFields.ownerID, userID);
        List<String> userIDs = new ArrayList<>();
        userIDs.add(userID);
        favor.set(Favor.ObjectFields.interested, userIDs);
        ObservableArrayList<Favor> interestingFavorsList = new ObservableArrayList<>();
        interestingFavorsList.add(favor);
        Map<DatabaseField, Object> interestedPeopleUserId = new HashMap<>();
        ProfileFragment.removeUserFromInterestedPeopleInFavors(interestedPeopleUserId,
                interestingFavorsList, userID);
        List<String> interestedPeople = (List<String>) interestingFavorsList.get(0).get(Favor.ObjectFields.interested);
        assertTrue(interestedPeople.isEmpty());
    }

}
