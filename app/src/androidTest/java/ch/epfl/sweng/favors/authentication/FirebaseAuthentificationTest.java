//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ch.epfl.sweng.favors.authentication;

import android.support.test.runner.AndroidJUnit4;
import ch.epfl.sweng.favors.utils.ExecutionMode;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

@RunWith(AndroidJUnit4.class)
public class FirebaseAuthentificationTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private FirebaseAuth firebaseAuth;
    @Mock
    private FirebaseUser fakeUser;
    @Mock
    private AuthResult fakeAuthResult;

    @Before
    public void setup() throws InterruptedException {
        FirebaseAuthentication.getInstance().cleanUp();
        ExecutionMode.getInstance().setTest(true);
        Mockito.when(this.fakeUser.delete()).thenReturn(Tasks.forResult((Void)null));
        Mockito.when(this.fakeUser.getEmail()).thenReturn("email");
        Mockito.when(this.fakeUser.sendEmailVerification()).thenReturn(Tasks.forResult((Void)null));
        Mockito.when(this.fakeUser.isEmailVerified()).thenReturn(true);
        Mockito.when(this.firebaseAuth.getCurrentUser()).thenReturn(this.fakeUser);
        Mockito.when(this.firebaseAuth.createUserWithEmailAndPassword("email", "password")).thenReturn(Tasks.forResult(this.fakeAuthResult));
        Mockito.when(this.firebaseAuth.sendPasswordResetEmail("email")).thenReturn(Tasks.forResult((Void)null));
        Mockito.when(this.firebaseAuth.signInWithEmailAndPassword("email", "password")).thenReturn(Tasks.forResult(this.fakeAuthResult));
        Mockito.when(this.firebaseAuth.getUid()).thenReturn("I WANT PIZZA");
        Mockito.when(this.firebaseAuth.getCurrentUser()).thenReturn(this.fakeUser);
        Class firebaseAuthentification = FirebaseAuthentication.class;
        FirebaseAuthentication.setFirebase(this.firebaseAuth);
    }

    @Test
    public void getInstanceReturns() {
        FirebaseAuthentication.getInstance();
    }

    @Test
    public void isEmailVerifiedReturns() {
        FirebaseAuthentication.getInstance().isEmailVerified();
    }

    @Test
    public void createUserWithEmailAndPasswordReturns() {
        FirebaseAuthentication.getInstance().createUserWithEmailAndPassword("email", "password");
    }

    @Test
    public void sendPasswordResetEmailReturns() {
        FirebaseAuthentication.getInstance().sendPasswordResetEmail("email");
    }

    @Test
    public void signInWithEmailAndPasswordReturns() {
        FirebaseAuthentication.getInstance().signInWithEmailAndPassword("email", "password");
    }

    @Test
    public void getUidReturns() {
        FirebaseAuthentication.getInstance().getUid();
    }

    @Test
    public void sendEmailVerificationReturns() {
        FirebaseAuthentication.getInstance().sendEmailVerification();
    }

    @Test
    public void getEmailReturns() {
        FirebaseAuthentication.getInstance().getEmail();
    }

    @Test
    public void deleteReturns() {
        FirebaseAuthentication.getInstance().delete();
    }

    @After
    public void cleanUpStuff() {
        ExecutionMode.getInstance().setTest(false);
        FirebaseAuthentication.getInstance().cleanUp();
    }
}
