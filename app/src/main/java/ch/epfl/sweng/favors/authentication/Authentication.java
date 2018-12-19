package ch.epfl.sweng.favors.authentication;


import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import ch.epfl.sweng.favors.utils.ExecutionMode;

/**
 * Singleton for the authentication within the app
 */
public abstract class Authentication {
    private static Authentication auth;

    public static Authentication getInstance(){

        if(auth == null) {
            if(ExecutionMode.getInstance().isTest()){
                auth = FakeAuthentication.getInstance();
            } else {
                auth = FirebaseAuthentication.getInstance();
            }
            //FakeDatabase.getInstance().createBasicDatabase();
        }
        return auth;
    }

    /**
     * @return if the Email has been verified by the user
     */
    public abstract boolean isEmailVerified();

    /**
     * Create a new User Account with an email and password
     * @param email String must be a valid email
     * @param password String password
     * @return Task<AuthResult> Task that creates the user account
     */
    public abstract Task<AuthResult> createUserWithEmailAndPassword(String email, String password);

    /**
     * Sends an email to the User with instructions to reset password
     * @param email String must be a valid email
     * @return Task<Void> The task that sends the email
     */
    public abstract Task<Void> sendPasswordResetEmail(String email);

    /**
     * Log into a user profile using email and password
     * @param email String Must be a valid email
     * @param password String password of the user
     * @return Task<AuthResult> Task that login the user
     */
    public abstract Task<AuthResult> signInWithEmailAndPassword(String email, String password);

    /**
     * Signs out user out of current session
     */
    public abstract void signOut();

    /**
     * @return UID with which current user is linked into the database
     */
    public abstract String getUid();

    /**
     * Send verification email to the user
     * @return Task<Void> that sends the email to the user
     */
    public abstract Task<Void>sendEmailVerification();

    /**
     * @return email of the user currently logged-in
     */
    public abstract String getEmail();

    /**
     * delete the user locally
     * @return Task<Void> that deletes the user in locally
     */
    public abstract Task<Void> delete();


}
