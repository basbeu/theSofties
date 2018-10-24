package ch.epfl.sweng.favors;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import ch.epfl.sweng.favors.database.User;
import ch.epfl.sweng.favors.databinding.ActivityMainBinding;


/**
 * This is the main class of the Favors application
 * the entry point for the actual app
 */
public class FavorsMain extends AppCompatActivity {

    public static final String TEST_MODE = "test-mode";

    public static final String TAG = "FAVORS_MAIN";
    public ObservableField<String> appName = new ObservableField<>("Favors");

    public ActivityMainBinding binding;

    private static Context context;
    public static Context getContext(){
        return context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setElements(this);

        binding.loginButton.setOnClickListener(v-> loginView(AuthentificationProcess.Action.Login,  v));
        binding.registerButton.setOnClickListener(v->loginView(AuthentificationProcess.Action.Register,  v));

        // if logged in -> display main view
        if(User.getMain().isLoggedIn() && FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()){
            loggedinView();
        }

    }

    public void loginView(AuthentificationProcess.Action action, View view){
        Intent intent = new Intent(view.getContext(), AuthentificationProcess.class);
        intent.putExtra(AuthentificationProcess.AUTHENTIFICATION_ACTION, action);
        startActivity(intent);
        finish();
    }

    private void loggedinView(){
        Intent intent = new Intent(this, Logged_in_Screen.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {

    }
}