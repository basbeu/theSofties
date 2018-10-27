package ch.epfl.sweng.favors.authentication;


import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import ch.epfl.sweng.favors.utils.ExecutionMode;

public abstract class Authentication {
    private static Authentication auth;

    public static Authentication getInstance(){
        if(auth == null){
            if(ExecutionMode.getInstance().isTest()){
                auth = FakeAuthentication.getInstance();
            }else{
                auth = FirebaseAuthentication.getInstance();
            }
        }

        return auth;
    }

    public abstract boolean isEmailVerified();
    public abstract Task<AuthResult> CreateUserWithEmailAndPassword(String email, String password);
    public abstract Task<Void> sendPasswordResetEmail(String email);
    public abstract Task<AuthResult> signInWithEmailAndPassword(String email, String password);
    public abstract void signOut();
    public abstract String getUid();

}
