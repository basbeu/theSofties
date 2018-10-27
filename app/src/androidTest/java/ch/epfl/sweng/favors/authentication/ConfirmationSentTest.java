package ch.epfl.sweng.favors.authentication;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import ch.epfl.sweng.favors.database.Database;
import ch.epfl.sweng.favors.database.FakeDatabase;
import ch.epfl.sweng.favors.utils.ExecutionMode;
import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.database.User;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class ConfirmationSentTest {
    @Rule public ActivityTestRule<ConfirmationSent> activityActivityTestRule = new ActivityTestRule<ConfirmationSent>(ConfirmationSent.class);

    @Before
    public void Before(){
        ExecutionMode.getInstance().setTest(true);
        FakeDatabase.getInstance().createBasicDatabase();
    }

    @Test
    public void confirmationSentView(){
        onView(ViewMatchers.withId(R.id.checkEmail)).check(matches(isDisplayed()));
    }

    @Test
    public void gotItButtonWorks(){
        onView(withId(R.id.gotItButton)).perform(click());
        onView(withId(R.id.loginMessageText)).check(matches(isDisplayed()));
    }

    @Test
    public void resendButtonWorks(){

        ExecutionMode.getInstance().setInvalidAuthTest(false);
        onView(withId(R.id.resendConfirmationMailButton)).perform(click());
        //Wait for the toast to be displayed
        try{
            Thread.sleep(2000);
            //This line tests if a toast is displayed
            onView(withText("Email confirmation sent successfully")).inRoot(withDecorView(not(is(activityActivityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
        }catch(Exception e){
            fail("Can't sleep");
        }

    }

    @Test
    public void resendButtonWorksWithUnvalidUser(){
        //Wait for the toast to be displayed
        ExecutionMode.getInstance().setInvalidAuthTest(true);
        onView(withId(R.id.resendConfirmationMailButton)).perform(click());
        try{
            Thread.sleep(2000);

            //This line tests if a toast is displayed
            onView(withText("Unable to send email")).inRoot(withDecorView(not(is(activityActivityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
        }catch(Exception e){
            fail("Can't sleep");
        }
    }
}
