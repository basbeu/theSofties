package ch.epfl.sweng.favors;
import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.google.common.util.concurrent.FakeTimeLimiter;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Assert;
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


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.Set;

/**
 * Used as container to test fragments in isolation with Espresso
 */
/*
@RestrictTo(RestrictTo.Scope.TESTS)
class SingleFragmentActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout content = new FrameLayout(this);
        content.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        content.setId(R.id.content_frame);
        setContentView(content);
    }

    public void setFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.content_frame, fragment, "TEST")
                .commit();
    }

    public void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment).commit();
    }
}*/

@RunWith(AndroidJUnit4.class)

//TODO: HANDLE NULL POINTER EXCEPTION SETUSERINFO.HAVA LINE 51 AND 56 TO MAKE TESTS PASS
public class SetUserInfoTest {

    @Rule public ActivityTestRule<SetUserInfo> activityActivityTestRule = new ActivityTestRule<SetUserInfo>(SetUserInfo.class, true,  false);
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
        Intent intent = new Intent();
        intent.putExtra(FavorsMain.TEST_MODE, "true");
        activityActivityTestRule.launchActivity(intent);
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

   @Test
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
    }

}
