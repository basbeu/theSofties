package ch.epfl.sweng.favors.authentication;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ch.epfl.sweng.favors.database.User;
import ch.epfl.sweng.favors.utils.ExecutionMode;

/**
 * Singleton class that represents the authentication with Firebase
 */
public class FirebaseAuthentication extends Authentication{
    private static FirebaseAuthentication auth;
    private FirebaseAuth firebaseAuth;

    private FirebaseAuthentication(){
        firebaseAuth = FirebaseAuth.getInstance();
    }
    private FirebaseAuthentication(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
        Log.d("DEBUG_TEST", "I am here");
    }

    public static void setFirebase(FirebaseAuth firebaseAuth){
        if(!ExecutionMode.getInstance().isTest()) throw new IllegalStateException();
        Log.d("DEBUG_TEST", "I am here2");
        if(auth == null)
            auth = new FirebaseAuthentication(firebaseAuth);
        else throw new IllegalStateException();
    }



    /**
     * @return FirebaseAuthentication the instance of authentication for the current session
     */
    public static FirebaseAuthentication getInstance(){
        if(auth == null){
            auth = new FirebaseAuthentication();
        }
        return auth;
    }

    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }

    @Override
    public boolean isEmailVerified() {
        return firebaseAuth.getCurrentUser()!=null && firebaseAuth.getCurrentUser().isEmailVerified();
    }

    @Override
    public Task<AuthResult> createUserWithEmailAndPassword(String email, String password) {
        return firebaseAuth.createUserWithEmailAndPassword(email,password);
    }

    @Override
    public Task<Void> sendPasswordResetEmail(String email) {
        return firebaseAuth.sendPasswordResetEmail(email);
    }

    @Override
    public Task<AuthResult> signInWithEmailAndPassword(String email, String password) {
        return firebaseAuth.signInWithEmailAndPassword(email,password);
    }

    @Override
    public void signOut() {
        firebaseAuth.signOut();
    }

    @Override
    public String getUid() {
        return firebaseAuth.getUid();
    }

    @Override
    public Task<Void> sendEmailVerification() {
        if(firebaseAuth.getCurrentUser()!=null) {
            return firebaseAuth.getCurrentUser().sendEmailVerification();
        }
        return null;
    }

    @Override
    public String getEmail() {
        if(firebaseAuth.getCurrentUser()!=null) {
            return firebaseAuth.getCurrentUser().getEmail();
        }
        return null;
    }

    @Override
    public Task<Void> delete() {
        if(firebaseAuth.getCurrentUser()!=null){
            return firebaseAuth.getCurrentUser().delete();
        }
        return Tasks.forCanceled();
    }

    public void cleanUp(){
        auth = null;
        firebaseAuth = null;
    }


}
