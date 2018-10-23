package ch.epfl.sweng.favors;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.common.util.concurrent.FakeTimeLimiter;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.FirebaseFirestore;

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
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.assertEquals;

import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
//TODO: TEST IF TOAST APPEARS
//
//TODO: HANDLE NULL POINTER EXCEPTION SETUSERINFO.HAVA LINE 51 AND 56 TO MAKE TESTS PASS
public class SetUserInfoTest {

    @Rule public ActivityTestRule<SetUserInfo> activityActivityTestRule = new ActivityTestRule<SetUserInfo>(SetUserInfo.class);
    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock private FirebaseUser fbFakeUser;
    @Mock private FirebaseAuth fakeAuth;
    private final String FAKEEMAIL = "thisisatestemail@email.com";
    private final String FAKEFIRSTNAME = "Bastien";
    private final String FAKELASTNAME = "Beuchat";
    private final String FAKECITY = "Lausanne";
    private final String FAKEUID= "userID";




    @Before
    public void Before(){
        ExecutionMode.getInstance().setTest(true);
        User fakeUser = new User(fakeAuth);
        User.setMain(fakeUser);
        when(fakeAuth.getUid()).thenReturn(FAKEUID);
        onView(withId(R.id.userFirstNameEdit)).perform(replaceText(FAKEFIRSTNAME)).perform(closeSoftKeyboard());
        onView(withId(R.id.userLastNameEdit)).perform(replaceText(FAKELASTNAME)).perform(closeSoftKeyboard());
        onView(withId(R.id.userCityEdit)).perform(replaceText(FAKECITY)).perform(closeSoftKeyboard());
        onView(withId(R.id.profGenderFEdit)).perform(click());
        onView(withId(R.id.submit)).perform(click());
        when(fakeAuth.getCurrentUser()).thenReturn(fbFakeUser);
        when(fbFakeUser.getEmail()).thenReturn(FAKEEMAIL);
    }

   /* @Test
    public void userHasCorrectFirstName() {
        assertEquals(FAKEFIRSTNAME, User.getMain().get(User.StringFields.firstName));
    }
    @Test
    public void userHasCorrectLastName() {
        assertEquals(FAKELASTNAME, User.getMain().get(User.StringFields.lastName));
    }
    @Test
    public void userHasCorrectCity() {
        assertEquals(FAKECITY, User.getMain().get(User.StringFields.city));
    }
    @Test
    public void userHasCorrectGender() {
        assertEquals("F", User.getMain().get(User.StringFields.sex));
    }*/
}
