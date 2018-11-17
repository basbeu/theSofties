package ch.epfl.sweng.favors.authentication;


import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import ch.epfl.sweng.favors.database.FakeDatabase;
import ch.epfl.sweng.favors.utils.ExecutionMode;

/**
 * Abstract class that represent a Singleton for the authentication in the app
 */
public abstract class Authentication {
    private static Authentication auth;

    public static Authentication getInstance(){
        // For espresso tests
        // FakeDatabase.getInstance().createBasicDatabase();

        if(auth == null){
            if(ExecutionMode.getInstance().isTest()){
                auth = FakeAuthentication.getInstance();
            }else{
                auth = FirebaseAuthentication.getInstance();
            }
        }

        return auth;
    }

    /**
     * @return boolean that give true if the email is verified, false else
     */
    public abstract boolean isEmailVerified();

    /**
     * Method that create a new User Account with an email and pasword
     * @param email String must be a valid email
     * @param password String password
     * @return Task<AuthResult> Task that create the user account
     */
    public abstract Task<AuthResult> createUserWithEmailAndPassword(String email, String password);

    /**
     * Method that send an email of reset password
     * @param email String must be a valid email
     * @return Task<Void> The task that sends the email
     */
    public abstract Task<Void> sendPasswordResetEmail(String email);

    /**
     * Metod that login a user with an email and a password
     * @param email String Must be a valid email
     * @param password String password of the user
     * @return Task<AuthResult> Task that login the user
     */
    public abstract Task<AuthResult> signInWithEmailAndPassword(String email, String password);

    /**
     * Method that signout the current user
     */
    public abstract void signOut();

    /**
     * @return String that represent the UID of the current user logged in
     */
    public abstract String getUid();

    /**
     * Method that send and verificaton mail
     * @return Task<Void> that send the email
     */
    public abstract Task<Void>sendEmailVerification();

    /**
     * @return String that represents the email of the current logged in user
     */
    public abstract String getEmail();

    /**
     * Method that delete the local cached user
     * @return Task<Void> that delete the user in local
     */
    public abstract Task<Void> delete();


}
