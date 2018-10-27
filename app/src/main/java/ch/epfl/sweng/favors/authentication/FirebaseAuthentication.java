package ch.epfl.sweng.favors.authentication;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class FirebaseAuthentication extends Authentication{
    private static FirebaseAuthentication auth;
    private FirebaseAuth firebaseAuth;

    private FirebaseAuthentication(){
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public static FirebaseAuthentication getInstance(){
        if(auth == null){
            auth = new FirebaseAuthentication();
        }

        return auth;
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
        return firebaseAuth.getCurrentUser().sendEmailVerification();
    }

    @Override
    public String getEmail() {
        return firebaseAuth.getCurrentUser().getEmail();
    }

    @Override
    public Task<Void> delete() {
        if(firebaseAuth.getCurrentUser()!=null){
            return firebaseAuth.getCurrentUser().delete();
        }
        return Tasks.forCanceled();
    }


}
