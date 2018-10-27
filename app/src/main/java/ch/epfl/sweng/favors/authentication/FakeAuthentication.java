package ch.epfl.sweng.favors.authentication;


import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;

public class FakeAuthentication extends Authentication {

    private static FakeAuthentication auth;
    private static final String UID = "fakeId90";
    private static final String EMAIL = "toto@mail.com";

    private FakeAuthentication(){

    }

    public static FakeAuthentication getInstance(){
        if(auth == null){
           auth = new FakeAuthentication();
        }

        return auth;
    }

    @Override
    public boolean isEmailVerified() {
        return true;
    }

    @Override
    public Task<AuthResult> createUserWithEmailAndPassword(String email, String password) {
        return Tasks.forResult(null);
    }

    @Override
    public Task<Void> sendPasswordResetEmail(String email) {
        return Tasks.forResult((Void)null);
    }

    @Override
    public Task<AuthResult> signInWithEmailAndPassword(String email, String password) {
        return Tasks.forResult(null);
    }

    @Override
    public void signOut() {

    }

    @Override
    public String getUid() {
        return UID;
    }

    @Override
    public Task<Void> sendEmailVerification() {
        return Tasks.forResult((Void)null);
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
