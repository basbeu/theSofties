package ch.epfl.sweng.favours;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ch.epfl.sweng.favours.databinding.LogInRegisterViewBinding;

public class AuthentificationProcess extends Activity {

    static final String TAG = FavoursMain.TAG + "_Authentification";


    public ObservableField<String> headerText = new ObservableField<>("Do what you have to do:");
    public ObservableField<String> validationButton = new ObservableField<>("OK");
    LogInRegisterViewBinding binding;
    private FirebaseAuth mAuth;

    FavoursMain.Status status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        binding = DataBindingUtil.setContentView(this, R.layout.log_in_register_view);
        binding.setElements(this);


        // Get the Intent that started this activity and extract the string
        Bundle bundle = getIntent().getExtras();
        status = (FavoursMain.Status) bundle.get(FavoursMain.AUTHENTIFICATION_ACTION);
        binding.authentificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authentification();
            }

        });

    }

    private void authentification() {
        if (status == FavoursMain.Status.Login) {
            mAuth.signInWithEmailAndPassword(binding.emailTextField.getText().toString(), binding.passwordTextField.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                headerText.set("Welcome " + user.getDisplayName());
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(AuthentificationProcess.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                headerText.set("Login failed, please try again");
                            }
                        }
                    });
        }
        else if (status == FavoursMain.Status.Register) {
            mAuth.createUserWithEmailAndPassword(binding.emailTextField.getText().toString(), binding.passwordTextField.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Toast.makeText(AuthentificationProcess.this, "SUIS ICI",
                                    Toast.LENGTH_SHORT).show();
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                headerText.set("Welcome " + user.getEmail());
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(AuthentificationProcess.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                headerText.set("Register failed, please try again");
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
