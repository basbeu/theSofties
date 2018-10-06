package ch.epfl.sweng.favours;

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

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.epfl.sweng.favours.databinding.LogInRegisterViewBinding;

import static ch.epfl.sweng.favours.Utils.*;

public class AuthentificationProcess extends Activity {

    static final String TAG = FavoursMain.TAG + "_Auth";
    static final String REQUIREMENTS_STRING = "Password must:\n" + "- Be between 8 and 20 characters\n" + "- Mix numbers and letters";
    private final String LOGGED_IN = "Logged in successfully";
    private static final int MAXPASSWORDLEN = 20;
    private static final int MINPASSWORDLEN = 8;



    LogInRegisterViewBinding binding;
    private FirebaseAuth mAuth;
    FavoursMain.Status status;

    public ObservableField<String> headerText = new ObservableField<>();
    public ObservableField<String> validationButton = new ObservableField<>();
    public ObservableField<String> requirementsText = new ObservableField<>();

    public ObservableBoolean isEmailCorrect = new ObservableBoolean(false);

    private TextWatcher emailTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            isEmailCorrect.set(isEmailValid(binding.emailTextField.getText().toString()));
        }
    };
    private TextWatcher passwordTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            isPasswordCorrect.set(passwordFitsRequirements(binding.passwordTextField.getText().toString()));
        }
    };
    private View.OnClickListener authentificationButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            authentification(binding.emailTextField.getText().toString(), binding.passwordTextField.getText().toString());
        }
    };

    public ObservableBoolean isPasswordCorrect = new ObservableBoolean(false){
        @Override
        public void set(boolean value) {
            super.set(value);
            if(status == FavoursMain.Status.Register){
                if(this.get()){
                    requirementsText.set("");
                }
                else{
                    requirementsText.set(REQUIREMENTS_STRING);
                }
            }
        }
    };


    private OnCompleteListener<AuthResult> signInComplete = new OnCompleteListener<AuthResult>(){
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
                RuntimeEnvironment.getInstance().isConnected.set(true);
                Log.d(TAG, "signInWithEmail:success");
                FirebaseUser user = mAuth.getCurrentUser();
                headerText.set("Welcome " + user.getDisplayName());
                /*  Validation check + Wait 2s + Back to last activity */
                loggedinView(status);

            } else {
                Log.w(TAG, "signInWithEmail:failure", task.getException());
                requirementsText.set("Wrong email or password\nPlease try again");
            }
        }
    };
    private OnCompleteListener<AuthResult> registerComplete = new OnCompleteListener<AuthResult>(){
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
                RuntimeEnvironment.getInstance().isConnected.set(true);
                Log.d(TAG, "createUserWithEmail:success");
                FirebaseUser user = mAuth.getCurrentUser();
                requirementsText.set("Welcome " + user.getEmail());

                /*  Intent new activity for user informations */
                /* Return to main screen */
                loggedinView(status);
            } else {
                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                requirementsText.set("Register failed, please try again");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        binding = DataBindingUtil.setContentView(this, R.layout.log_in_register_view);
        binding.setElements(this);
        // Check if the email is correct each time a letter was added
        binding.emailTextField.addTextChangedListener(emailTextWatcher);
        // Check if the password is correct each time a letter was added
        binding.passwordTextField.addTextChangedListener(passwordTextWatcher);
        // Get the Intent that started this activity and extract the string
        Bundle bundle = getIntent().getExtras();
        status = (FavoursMain.Status) bundle.get(FavoursMain.AUTHENTIFICATION_ACTION);
        setUI(status);
        binding.authentificationButton.setOnClickListener(authentificationButtonListener);
    }


    /**
     * Display UI Elements based on the current mode
     *
     * @param status The current mode
     */
    private void setUI(FavoursMain.Status status){
        switch(status){
            case Login:
                headerText.set("Please enter your login informations:");
                validationButton.set("Login");
                requirementsText.set("");
                break;
            case Register:
                headerText.set("Welcome here! Just some small steps...");
                validationButton.set("Register");
                requirementsText.set(REQUIREMENTS_STRING);
                break;
        }
    }

    /**
     * Function to check if the email is in a valid format
     * Inspired from: https://stackoverflow.com/questions/6119722/how-to-check-edittexts-text-is-email-address-or-not
     *
     * @param email The email to check
     * @return True if the email is in a valid format
     */
    private Boolean isEmailValid(String email){
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * Function to check if a password has a valid format
     * Inspired from : https://stackoverflow.com/questions/24857893/password-requirements-program
     *
     * @param password The password to check
     * @return True if the password fits with requirements
     */
    private Boolean passwordFitsRequirements(String password){

        if (password == null || password.length() < MINPASSWORDLEN || password.length() > MAXPASSWORDLEN) {
            return false;
        }

        return containsChar(password) && containsDigit(password);
    }

    /**
     * perform the login or registering functionalitie and update the status in runtime environment
     * @param email The email to log in with
     * @param password The user password
     */
    private void authentification(String email, String password) {
        if(!isEmailValid(email)){
            requirementsText.set("Wrong email format");
            return;
        }
        if(!passwordFitsRequirements(password)){
            requirementsText.set("Wrong password format");
            return;
        }
        if (status == FavoursMain.Status.Login) {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, signInComplete);
        }
        else if (status == FavoursMain.Status.Register) {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, registerComplete);
            mAuth.createUserWithEmailAndPassword(binding.emailTextField.getText().toString(), binding.passwordTextField.getText().toString());
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            headerText.set("You're already logged in");
        }
    }

    public void loggedinView(FavoursMain.Status status){
        Intent intent = new Intent(this, Logged_in_Screen.class);
        intent.putExtra(LOGGED_IN, status);
        startActivity(intent);
    }



}
