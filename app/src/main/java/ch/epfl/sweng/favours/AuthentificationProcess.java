package ch.epfl.sweng.favours;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.epfl.sweng.favours.databinding.LogInRegisterViewBinding;

public class AuthentificationProcess extends Activity {

    static final String TAG = FavoursMain.TAG + "_Authentification";
    static final String REQUIREMENTS_STRING = "Password must:\n" +
            "- Be between 8 and 20 characters\n" +
            "- Mix numbers and letters";

    LogInRegisterViewBinding binding;
    private FirebaseAuth mAuth;
    FavoursMain.Status status;

    public ObservableField<String> headerText = new ObservableField<>();
    public ObservableField<String> validationButton = new ObservableField<>();
    public ObservableField<String> requirementsText = new ObservableField<>();

    public ObservableBoolean isEmailCorrect = new ObservableBoolean(false);
    public ObservableBoolean isPasswordCorrect = new ObservableBoolean(false){
        @Override
        public void set(boolean value) {
            super.set(value);
            if(this.get() && status == FavoursMain.Status.Register){
                requirementsText.set("");
            }
            else if (!this.get() && status == FavoursMain.Status.Register){
                requirementsText.set(REQUIREMENTS_STRING);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        binding = DataBindingUtil.setContentView(this, R.layout.log_in_register_view);
        binding.setElements(this);

        // Check if the email is correct each time a letter was added
        binding.emailTextField.addTextChangedListener(new TextWatcher() {
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
        });

        // Check if the password is correct each time a letter was added
        binding.passwordTextField.addTextChangedListener(new TextWatcher() {
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
        });

        // Get the Intent that started this activity and extract the string
        Bundle bundle = getIntent().getExtras();
        status = (FavoursMain.Status) bundle.get(FavoursMain.AUTHENTIFICATION_ACTION);
        setUI(status);

        binding.authentificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authentification(binding.emailTextField.getText().toString(), binding.passwordTextField.getText().toString());
            }

        });


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

        if (password == null || password.length() < 8 || password.length() > 20) {
            return false;
        }

        boolean containsChar = false;
        boolean containsDigit = false;

        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) {
                containsChar = true;
            } else if (Character.isDigit(c)) {
                containsDigit = true;
            }
            if (containsChar && containsDigit) {
                return true;
            }
        }
        return false;
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
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                RuntimeEnvironment.getInstance().isConnected.set(true);
                                Log.d(TAG, "signInWithEmail:success");

                                FirebaseUser user = mAuth.getCurrentUser();
                                headerText.set("Welcome " + user.getDisplayName());

                                /*  Validation check + Wait 2s + Back to last activity */

                            } else {
                                Log.w(TAG, "signInWithEmail:failure", task.getException());

                                requirementsText.set("Wrong email or password\nPlease try again");
                            }
                        }
                    });
        }
        else if (status == FavoursMain.Status.Register) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                RuntimeEnvironment.getInstance().isConnected.set(true);
                                Log.d(TAG, "createUserWithEmail:success");

                                FirebaseUser user = mAuth.getCurrentUser();
                                requirementsText.set("Welcome " + user.getEmail());

                                /*  Intent new activity for user informations */

                                /* Return to main screen */


                            } else {
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());

                                requirementsText.set("Register failed, please try again");
                            }
                        }
                    });
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



}
