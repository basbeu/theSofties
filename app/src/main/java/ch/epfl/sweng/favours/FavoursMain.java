package ch.epfl.sweng.favours;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.List;

import ch.epfl.sweng.favours.databinding.ActivityMainBinding;

public class FavoursMain extends AppCompatActivity {

    static final String TAG = "FavoursApp";
    public ObservableField<String> appName = new ObservableField<>("Favours");

    public enum Status{Register, Login, LoggedIn, Disconnect};
    public static String AUTHENTIFICATION_ACTION = "AUTHENTIFICATION_ACTION";
    ActivityMainBinding binding;

    public ObservableBoolean isConnected;

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


        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginViewLoad(Status.Login,  v);
            }
        });


        binding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginViewLoad(Status.Register,  v);
            }
        });

        binding.logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance()
                        .signOut();
                RuntimeEnvironment.getInstance().isConnected.set(false);
            }
        });

    }

    public void loginViewLoad(Status status, View view){
        Intent intent = new Intent(view.getContext(), AuthentificationProcess.class);
        intent.putExtra(AUTHENTIFICATION_ACTION, status);
        startActivity(intent);
    }
}

