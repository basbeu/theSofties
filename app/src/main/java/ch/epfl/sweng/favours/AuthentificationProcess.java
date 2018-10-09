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
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ch.epfl.sweng.favours.databinding.LogInRegisterViewBinding;

import static ch.epfl.sweng.favours.Utils.displayToastOnTaskCompletion;
import static ch.epfl.sweng.favours.Utils.isEmailValid;
import static ch.epfl.sweng.favours.Utils.logout;
import static ch.epfl.sweng.favours.Utils.passwordFitsRequirements;


public class AuthentificationProcess extends Activity {

    public static final String TAG = FavoursMain.TAG + "_Auth";
    public static final String REQUIREMENTS_STRING = "Password must:\n" + "- Be between 8 and 20 characters\n" + "- Mix numbers and letters";


    public LogInRegisterViewBinding binding;
    private FirebaseAuth mAuth;
    public FavoursMain.Status status;

    public ObservableField<String> headerText = new ObservableField<>();
    public ObservableField<String> validationButton = new ObservableField<>();
    public ObservableField<String> requirementsText = new ObservableField<>();

    public ObservableBoolean isEmailCorrect = new ObservableBoolean(false);

    private TextWatcher emailTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            isEmailCorrect.set(isEmailValid(binding.emailTextField.getText().toString()));
        }
    };
    private TextWatcher passwordTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

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

    private OnCompleteListener<AuthResult> registerComplete = task -> {
        if (task.isSuccessful()) {
            Button resendConfirmationMail = (Button)findViewById(R.id.resendConfirmationMailButton);
            resendConfirmationMail.setVisibility(View.VISIBLE);

            Button log_out = (Button)findViewById(R.id.logOutButton);
            log_out.setVisibility(View.VISIBLE);

            RuntimeEnvironment.getInstance().isConnected.set(true);
            Log.d(TAG, "createUserWithEmail:success");
            final FirebaseUser user = mAuth.getCurrentUser();
            sendConfirmationMail(user);
            resendConfirmationMail.setOnClickListener(v-> sendConfirmationMail(user));
            log_out.setOnClickListener(v->logout(this));
            /*  Intent new activity for user informations */
                /* Return to main screen FOR THE MOMENT NEVER REACHED because condition instantly checked
                thus impossible to fulfill because when clicked on register button it is impossible to verify its email instantly
                thus it should be possible to check this condition successfully until it is fulfilled!*/
            if(mAuth.getCurrentUser().isEmailVerified()) {
                // requirementsText.set("Welcome " + user.getEmail());
                loggedinView(status);
            }
        } else {
            Log.w(TAG, "createUserWithEmail:failure", task.getException());
            requirementsText.set("Register failed, please try again");
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

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null && mAuth.getCurrentUser().isEmailVerified()){
            headerText.set("You're already logged in");
        }
    }

    private OnCompleteListener<AuthResult> signInComplete = new OnCompleteListener<AuthResult>(){
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful() && mAuth.getCurrentUser().isEmailVerified()) {
                RuntimeEnvironment.getInstance().isConnected.set(true);
                Log.d(TAG, "signInWithEmail:success");
                final FirebaseUser user = mAuth.getCurrentUser();
                headerText.set("Welcome " + user.getDisplayName());
                Button resetPassword = (Button)findViewById(R.id.resetPasswordButton);
                resetPassword.setVisibility(View.VISIBLE);
                resetPassword.setOnClickListener(v -> {sendPasswordResetEmail(task, user);});
                /*  Validation check + Wait 2s + Back to last activity */
                User.getMain().updateFromDb();
                loggedinView(status);
            } else {
                Log.w(TAG, "signInWithEmail:failure", task.getException());
                requirementsText.set("Wrong email or password or email not verified\nPlease try again");
            }
        }
    };

    private void sendPasswordResetEmail(Task<AuthResult> task, FirebaseUser user){
        FirebaseAuth.getInstance().sendPasswordResetEmail(user.getEmail())
                .addOnCompleteListener(w->displayToastOnTaskCompletion(task,AuthentificationProcess.this, "Reset password email sent to " + user.getEmail(),"Failed to send reset password email."));
    }

    private void sendConfirmationMail(final FirebaseUser user){
        user.sendEmailVerification()
                .addOnCompleteListener(AuthentificationProcess.this, task-> {
                    // Re-enable button
                    findViewById(R.id.resendConfirmationMailButton).setEnabled(true);
                    displayToastOnTaskCompletion(task,AuthentificationProcess.this, "Verification email sent to " + user.getEmail(),"Failed to send verification email.");
                });
    }

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
     * perform the login or registering functionality and update the status in runtime environment
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

    private void loggedinView(FavoursMain.Status status){
        if(mAuth.getCurrentUser().isEmailVerified()) {
            Intent intent = new Intent(this, Logged_in_Screen.class);
            intent.putExtra(FavoursMain.LOGGED_IN, status);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, AuthentificationProcess.class);
            intent.putExtra(FavoursMain.LOGGED_OUT, status);
            startActivity(intent);
        }
    }
}


