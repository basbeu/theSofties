package ch.epfl.sweng.favors;

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
    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock private FirebaseUser fbFakeUser;
    @Mock private FirebaseAuth fakeAuth;
    private final String FAKEEMAIL = "thisisatestemail@email.com";

    @Before
    public void Before(){
        ExecutionMode.getInstance().setTest(true);
        User fakeUser = new User(fakeAuth);
        User.setMain(fakeUser);
        User.UserGender.setGender(User.getMain(), User.UserGender.M);
        when(fakeAuth.getCurrentUser()).thenReturn(fbFakeUser);
        when(fbFakeUser.getEmail()).thenReturn(FAKEEMAIL);


    }

    @Test
    public void confirmationSentView(){
        onView(withId(R.id.checkEmail)).check(matches(isDisplayed()));
    }

    @Test
    public void gotItButtonWorks(){
        onView(withId(R.id.gotItButton)).perform(click());
        onView(withId(R.id.loginMessageText)).check(matches(isDisplayed()));
    }

    @Test
    public void resendButtonWorks(){
        when(fbFakeUser.sendEmailVerification()).thenReturn(Tasks.forResult(null));
        onView(withId(R.id.resendConfirmationMailButton)).perform(click());
        //Wait for the toast to be displayed
        try{
            Thread.sleep(500);
            //This line tests if a toast is displayed
            onView(withText("Email confirmation sent successfully")).inRoot(withDecorView(not(is(activityActivityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
            Thread.sleep(1000);
        }catch(Exception e){
            fail("Can't sleep");
        }

    }

    @Test
    public void resendButtonWorksWithUnvalidUser(){
        when(fbFakeUser.sendEmailVerification()).thenReturn(Tasks.forException(new Exception()));
        //Wait for the toast to be displayed
        try{
            Thread.sleep(500);
            onView(withId(R.id.resendConfirmationMailButton)).perform(click());
            Thread.sleep(500);
            //This line tests if a toast is displayed
            onView(withText("Unable to send email")).inRoot(withDecorView(not(is(activityActivityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
        }catch(Exception e){
            fail("Can't sleep");
        }
    }



}
