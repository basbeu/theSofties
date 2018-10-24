package ch.epfl.sweng.favors;

import android.os.Looper;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;

import ch.epfl.sweng.favors.database.User;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class UtilsTest {
    @Mock private FirebaseAuth fakeAuth;

    @Before
    public void Before() {
        ExecutionMode.getInstance().setTest(true);
    }

    @Test
    public void containsCharWithNoChar(){
        String s1 = "12347226";
        String s2 = "4";
        String s3 = "00002900";
        String s4 = "";

        assertEquals(false, Utils.containsChar(s1));
        assertEquals(false, Utils.containsChar(s2));
        assertEquals(false, Utils.containsChar(s3));
        assertEquals(false, Utils.containsChar(s4));
    }

    @Test
    public void containsCharWithChar(){
        String s1 = "1763h";
        String s2 = "A83739";
        String s3 = "JsiWOskAP";
        String s4 = "883772j02";

        assertEquals(true, Utils.containsChar(s1));
        assertEquals(true, Utils.containsChar(s2));
        assertEquals(true, Utils.containsChar(s3));
        assertEquals(true, Utils.containsChar(s4));
    }

    @Test
    public void containsDigitWithDigit(){
        String s1 = "swijdew8wqd";
        String s2 = "JshdiAAAi9";
        String s3 = "9swijdewwqd";
        String s4 = "993733773829";

        assertEquals(true, Utils.containsDigit(s1));
        assertEquals(true, Utils.containsDigit(s2));
        assertEquals(true, Utils.containsDigit(s3));
        assertEquals(true, Utils.containsDigit(s4));
    }

    @Test
    public void containsDigitWithNoDigit(){
        String s1 = "ijaszwjwd";
        String s2 = "a";
        String s3 = "LLAksjdeU";
        String s4 = "llAKKSllsoOsjdhZZS";


        assertEquals(false, Utils.containsDigit(s1));
        assertEquals(false, Utils.containsDigit(s2));
        assertEquals(false, Utils.containsDigit(s3));
        assertEquals(false, Utils.containsDigit(s4));
    }

    @Test
    public void validEmail(){
        String e1 = "toto@email.com";
        String e2 = "toto@gmail.ch";
        String e3 = "tata@hotmail.fr";
        String e4 = "a@mail.com";

        assertEquals(Boolean.TRUE, Utils.isEmailValid(e1));
        assertEquals(Boolean.TRUE, Utils.isEmailValid(e2));
        assertEquals(Boolean.TRUE, Utils.isEmailValid(e3));
        assertEquals(Boolean.TRUE, Utils.isEmailValid(e4));
    }

    @Test
    public void invalidEmail(){
        String e1 = "email@toto";
        String e2 = "toto.gmail.com";
        String e3 = ".com@totogmail";
        String e4 = "@com.com";

        assertEquals(Boolean.FALSE, Utils.isEmailValid(e1));
        assertEquals(Boolean.FALSE, Utils.isEmailValid(e2));
        assertEquals(Boolean.FALSE, Utils.isEmailValid(e3));
        assertEquals(Boolean.FALSE, Utils.isEmailValid(e4));

    }

    @Test
    public void passwordWithRequirements(){
        String p1 = "hsj26u3J";
        String p2 = "HPAkdj7wu";
        String p3 = "92ksjd73jd99sjaksjdu";
        String p4 = "8273d829";

        assertEquals(Boolean.TRUE, Utils.passwordFitsRequirements(p1));
        assertEquals(Boolean.TRUE, Utils.passwordFitsRequirements(p2));
        assertEquals(Boolean.TRUE, Utils.passwordFitsRequirements(p3));
        assertEquals(Boolean.TRUE, Utils.passwordFitsRequirements(p4));

    }

    @Test
    public void passwordWithoutRequirements(){
        String p1 = "meoaeJAi";
        String p2 = "ksjd73jd93ppdl28sj2i8";
        String p3 = "17384938";
        String p4 = "83js92i";

        assertEquals(Boolean.FALSE, Utils.passwordFitsRequirements(p1));
        assertEquals(Boolean.FALSE, Utils.passwordFitsRequirements(p2));
        assertEquals(Boolean.FALSE, Utils.passwordFitsRequirements(p3));
        assertEquals(Boolean.FALSE, Utils.passwordFitsRequirements(p4));

    }
/*
    @Test
    public void logoutTest(){
        ActivityTestRule<Logged_in_Screen> activityActivityTestRule = new ActivityTestRule<>(Logged_in_Screen.class);
        activityActivityTestRule.launchActivity(null);
        Looper.prepare();

        //Mockito.doNothing().when(fakeAuth).signOut();
        Mockito.doCallRealMethod().when(fakeAuth).signOut();
        Utils.logout(activityActivityTestRule.getActivity(),fakeAuth);
    }*/

    @Test
    public void failingToast(){
        ActivityTestRule<TestActivity> activityActivityTestRule = new ActivityTestRule<>(TestActivity.class);
        activityActivityTestRule.launchActivity(null);
        Looper.prepare();

        Utils.displayToastOnTaskCompletion(Tasks.forCanceled(),activityActivityTestRule.getActivity(),"success","failure");
    }



}
