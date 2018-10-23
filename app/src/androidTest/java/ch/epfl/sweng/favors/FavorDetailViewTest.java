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
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

public class FavorDetailViewTest {

    //TODO: HANDLE NULL POINTER EXCEPTION SETUSERINFO.HAVA LINE 51 AND 56 TO MAKE TESTS PASS

    @Rule public FragmentTestRule<FavorDetailView> mFragmentTestRule = new FragmentTestRule<FavorDetailView>(FavorDetailView.class);
        @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

        @Mock private FirebaseUser fbFakeUser;
        @Mock private FirebaseAuth fakeAuth;

        @Before
        public void Before(){
            ExecutionMode.getInstance().setTest(true);
            User fakeUser = new User(fakeAuth);
            User.setMain(fakeUser);
        }

        @Test
        public void imInterestedToast(){
            mFragmentTestRule.launchActivity(null);
            onView(withId(R.id.favIntrestedButton)).perform(scrollTo(), click());
            onView(withText("We will inform the poster of the add that you are intrested to help!")).inRoot(withDecorView(not(is(mFragmentTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
        }

    @Test
        public void reportAbusiveAddToast(){
        mFragmentTestRule.launchActivity(null);
        onView(withId(R.id.favReportAbusiveAdd)).perform(scrollTo(), click());
        try{
            Thread.sleep(2000);
            //This line tests if a toast is displayed
            onView(withText("issue has been reported! Sorry for the inconvenience")).inRoot(withDecorView(not(is(mFragmentTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
        } catch(Exception e){
            fail("Can't sleep");
        }
    }

}
