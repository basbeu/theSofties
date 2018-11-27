package ch.epfl.sweng.favors.authentication;


import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;

import ch.epfl.sweng.favors.database.User;
import ch.epfl.sweng.favors.utils.ExecutionMode;

/**
 * Singleton class that is a fake authentication for testing purpose
 */

public class FakeAuthentication extends Authentication {

    private static FakeAuthentication auth;
    public static final String UID = "fakeId90";
    public static final String EMAIL = "toto@mail.com";
    public static final String FIRST_NAME = "Fake";
    public static final String LAST_NAME = "Auth";
    public static final String CITY = "Fake City";
    public static final Long TOKENS = 5L;
    public static final User.UserGender GENDER = User.UserGender.F;

    private FakeAuthentication(){

    }

    /**
     * @return FakeAuthentication the fake authentication for the current session
     */
    public static FakeAuthentication getInstance(){
        if(auth == null){
            auth = new FakeAuthentication();
        }

        return auth;
    }

    @Override
    public boolean isEmailVerified() {
        return !ExecutionMode.getInstance().isInvalidAuthTest();
    }

    @Override
    public Task<AuthResult> createUserWithEmailAndPassword(String email, String password) {
        if(!ExecutionMode.getInstance().isInvalidAuthTest()){
            return Tasks.forResult(null);
        }
        else{
            return Tasks.forException(new Exception());
        }


    }

    @Override
    public Task<Void> sendPasswordResetEmail(String email) {
        return Tasks.forResult((Void)null);
    }

    @Override
    public Task<AuthResult> signInWithEmailAndPassword(String email, String password) {
        Log.d("FakeAuth", "Create task");
        return Tasks.forResult(null);
    }

    @Override
    public void signOut() {

    }

    @Override
    public String getUid() {
        Log.d("FAKEAUTH", UID);
        return UID;
    }

    @Override
    public Task<Void> sendEmailVerification() {
        if(!ExecutionMode.getInstance().isInvalidAuthTest()){
            return Tasks.forResult((Void)null);
        }
        else{
            return Tasks.forException(new Exception());
        }
    }

    @Override
    public String getEmail() {
        return EMAIL;
    }

    @Override
    public Task<Void> delete() {
        return Tasks.forCanceled();
    }
}
