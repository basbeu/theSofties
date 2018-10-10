package ch.epfl.sweng.favors;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.databinding.ActivityMainBinding;

public class FavorsMain extends AppCompatActivity {

    public static final String TAG = "FavorsApp";
    public ObservableField<String> appName = new ObservableField<>("Favors");

    public enum Status{Register, Login, LoggedIn, Disconnect, Reset};
    public static String AUTHENTIFICATION_ACTION = "AUTHENTIFICATION_ACTION";
    public ActivityMainBinding binding;

    public ObservableBoolean isConnected;
    public static final String LOGGED_IN = "Logged in successfully";
    public static final String LOGGED_OUT = "Disconnected successfully";

    private RuntimeEnvironment runtimeEnvironment;

    private static Context context;
    public static Context getContext(){
        return context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        runtimeEnvironment = new RuntimeEnvironment();
        isConnected = RuntimeEnvironment.getInstance().isConnected;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setElements(this);

        binding.loginButton.setOnClickListener(v-> loginViewLoad(Status.Login,  v));
        binding.registerButton.setOnClickListener(v->loginViewLoad(Status.Register,  v));
        binding.logoutButton.setOnClickListener(v->{
                FirebaseAuth.getInstance().signOut();
                RuntimeEnvironment.getInstance().isConnected.set(false);
        });

        if(RuntimeEnvironment.getInstance().isConnected.get() && FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()){
            loggedinView(Status.LoggedIn);
        }
    }

    public void loginViewLoad(Status status, View view){
        Intent intent = new Intent(view.getContext(), AuthentificationProcess.class);
        intent.putExtra(AUTHENTIFICATION_ACTION, status);
        startActivity(intent);
    }

    private void loggedinView(FavorsMain.Status status){
        Intent intent = new Intent(this, Logged_in_Screen.class);
        intent.putExtra(LOGGED_IN, status);
        startActivity(intent);
    }
}

