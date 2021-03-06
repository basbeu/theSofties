package ch.epfl.sweng.favors.authentication;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.iid.FirebaseInstanceId;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.database.Database;
import ch.epfl.sweng.favors.database.User;
import ch.epfl.sweng.favors.databinding.LogInRegisterViewBinding;
import ch.epfl.sweng.favors.main.FavorsMain;
import ch.epfl.sweng.favors.main.LoggedInScreen;
import ch.epfl.sweng.favors.utils.ExecutionMode;
import ch.epfl.sweng.favors.utils.TextWatcherCustom;
import ch.epfl.sweng.favors.utils.Utils;

/**
 * Authentication Process
 * This class
 */
public class AuthenticationProcess extends Activity {

    public static final String TAG = FavorsMain.TAG + "_Auth";
    public static final String REQUIREMENTS_STRING = "Password must:\n" + "- Be between 8 and 20 characters\n" + "- Mix numbers and letters";

    public static String AUTHENTICATION_ACTION = "AUTHENTICATION_ACTION";
    public enum Action{Login, Register} ;


    public LogInRegisterViewBinding binding;

    private Authentication mAuth;

    public Action action;

    public ObservableField<String> headerText = new ObservableField<>();
    public ObservableField<String> validationButton = new ObservableField<>();
    public ObservableField<String> requirementsText = new ObservableField<>();

    public ObservableBoolean isEmailCorrect = new ObservableBoolean(false);

    private TextWatcher emailTextWatcher = new TextWatcherCustom() {
        @Override
        public void afterTextChanged(Editable s) {
            isEmailCorrect.set(Utils.isEmailValid(binding.emailTextField.getText().toString()));
        }
    };

    private TextWatcher passwordTextWatcher = new TextWatcherCustom() {
        @Override
        public void afterTextChanged(Editable s) {
            isPasswordCorrect.set(Utils.passwordFitsRequirements(binding.passwordTextField.getText().toString()));
        }
    };

    // login button
    private View.OnClickListener authenticationButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // perform the login
            authentication(binding.emailTextField.getText().toString(), binding.passwordTextField.getText().toString());
        }
    };

    // password reset button - checks if there is such a user account
    private View.OnClickListener resetButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            mAuth.sendPasswordResetEmail(binding.emailTextField.getText().toString())
                    .addOnCompleteListener(w-> Utils.displayToastOnTaskCompletion(w,
                            AuthenticationProcess.this,
                            "Reset password email sent to "
                                    + binding.emailTextField.getText().toString(),
                            "No account with this email."));
        }
    };

    /**
     * sets if the password is valid
     */
    public ObservableBoolean isPasswordCorrect = new ObservableBoolean(false){
        @Override
        public void set(boolean value) {
            super.set(value);
            requirementsText.set(action == Action.Register && this.get() ? "" : REQUIREMENTS_STRING);
        }
    };

    /**
     * Changes UI depending on whether user is already signed-in
     */
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if (mAuth.isEmailVerified()){
            headerText.set("You're already logged in");
        }
    }

    private OnCompleteListener<AuthResult> registerComplete = task -> {
        if (task.isSuccessful()) {
            Log.d(TAG, "createUserWithEmail:success");

            sendConfirmationMail();
            confirmationSent();
        } else {
            Log.w(TAG, "createUserWithEmail:failure", task.getException());
            requirementsText.set("Register failed, please try again!");
        }
    };

    private OnCompleteListener<AuthResult> signInComplete = new OnCompleteListener<AuthResult>(){
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful() && mAuth.isEmailVerified()) {
                Log.d(TAG, "signInWithEmail:success");
                //if (mAuth instanceof FirebaseAuthentication) {
                Log.d(TAG, "logging in");
                loggedinView(action);
                //}
            } else {
                Log.w(TAG, "signInWithEmail:failure", task.getException());
                requirementsText.set("Wrong email or password or email not verified\nPlease try again!");
            }
        }
    };

    /**
     * Sends a verification email to the user's email and displays a toast with success or failure
     */
    private void sendConfirmationMail(){
        mAuth.sendEmailVerification().addOnCompleteListener(AuthenticationProcess.this, task-> {
            // Re-enable button
            findViewById(R.id.resendConfirmationMailButton).setEnabled(true);
            Utils.displayToastOnTaskCompletion(task,AuthenticationProcess.this,
                    "Verification email sent to " + mAuth.getEmail(),
                    "Failed to send verification email.");
        });
    }

    /**
     * Creates
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!ExecutionMode.getInstance().isTest()) {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        }
        Log.d("TestMode", ExecutionMode.getInstance().isTest() ? "true" : "false");

        mAuth = Authentication.getInstance();

        binding = DataBindingUtil.setContentView(this, R.layout.log_in_register_view);
        binding.setElements(this);
        // Check if the email is correct each time a letter was added
        binding.emailTextField.addTextChangedListener(emailTextWatcher);
        // Check if the password is correct each time a letter was added
        binding.passwordTextField.addTextChangedListener(passwordTextWatcher);

        // Get the Intent that started this activity and extract the string
        if (getIntent().hasExtra(AUTHENTICATION_ACTION)) {
            action = (Action) getIntent().getExtras().get(AUTHENTICATION_ACTION);
            setUI(action);
        } else {
            setUI(Action.Login);
        }

        binding.authenticationButton.setOnClickListener(authenticationButtonListener);
        binding.resetPasswordButton.setOnClickListener(resetButtonListener);
    }

    /**
     * Display UI Elements based on the current mode
     *
     * @param action The current mode
     */
    private void setUI(Action action){
        switch(action){
            case Login:
                headerText.set("Please enter your login informations:");
                validationButton.set("Login");
                requirementsText.set("");
                Button resetPassword = findViewById(R.id.resetPasswordButton);
                resetPassword.setVisibility(View.VISIBLE);
                break;
            case Register:
                headerText.set("Welcome here! Just some small steps...");
                validationButton.set("Register");
                requirementsText.set(REQUIREMENTS_STRING);
                break;
        }
    }

    /**
     * perform the login or registering functionality and update the status in runtime environment
     * @param email The email to log in with
     * @param password The user password
     */
    private void authentication(String email, String password) {
        if (!Utils.isEmailValid(email)){
            Log.d(TAG,"invalid email format");
            requirementsText.set("Wrong email format");
            return;
        }
        if (!Utils.passwordFitsRequirements(password)){
            Log.d(TAG,"invalid password format");
            requirementsText.set("Wrong password format");
            return;
        }
        if (action == Action.Login) {
            Log.d(TAG,"Login");
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, signInComplete);
        }
        else if (action == Action.Register) {
            Log.d(TAG,"Register");
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, registerComplete);
            mAuth.createUserWithEmailAndPassword(binding.emailTextField.getText().toString(), binding.passwordTextField.getText().toString());
        }
    }

    private void loggedinView(Action action){
        if (mAuth.isEmailVerified()) {
            User.updateMain();

            Database.getInstance().updateFromDb(User.getMain()).addOnCompleteListener(
                    (t)->{
                        String tokenId = "1";

                        if (!ExecutionMode.getInstance().isTest()) {
                            tokenId = FirebaseInstanceId.getInstance().getToken();
                        }

                        User.getMain().set(User.StringFields.token_id, tokenId);
                        Database.getInstance().updateOnDb(User.getMain()).addOnCompleteListener((s)->{
                            Intent intent = new Intent(this, LoggedInScreen.class);
                            startActivity(intent);
                            finish();
                        });
                    }
            );


        } else {
            Intent intent = new Intent(this, AuthenticationProcess.class);
            startActivity(intent);
        }
    }
    private void confirmationSent(){
        Intent intent = new Intent(this, SetUserInfo.class);
        startActivity(intent);
    }

    /*
    Explicitly calls the FavorMain because the back button will not work to go back to FavorMain.
    This behavior is wanted because we don't want to accidentally have a user reach the login screen when he is logged in
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, FavorsMain.class);
        startActivity(intent);
        finish();
    }
}


