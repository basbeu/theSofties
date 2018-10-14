package ch.epfl.sweng.favors;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
/*@RunWith(PowerMockRunner.class)
@PrepareForTest(com.google.firebase.auth.FirebaseAuth.class)
@PowerMockRunnerDelegate(JUnit4.class)*/


public class EditProfileFragmentTest {

    @Rule
    public FragmentTestRule<EditProfileFragment> mFragmentTestRule = new FragmentTestRule<>(EditProfileFragment.class);
    //@Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    /*@Mock FirebaseAuth mockedAuth;
    public User testUser;


    @Before
    public void before(){
        PowerMockito.mockStatic(FirebaseAuth.class);
        when(mockedAuth.getUid()).thenReturn("7777");
        when(mockedAuth.getCurrentUser().getEmail()).thenReturn("toto@email.com");
        testUser = new User(mockedAuth);
        when(User.getMain()).thenReturn(testUser);
    }

    */

    @Test
    public void fragment_can_be_instantiated() {

        //mFragmentTestRule.launchActivity(null);
        //onView(withId(R.id.profileTitle)).check(matches(isDisplayed()));
    }
    
}
