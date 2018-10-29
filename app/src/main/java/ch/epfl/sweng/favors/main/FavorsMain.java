package ch.epfl.sweng.favors.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.authentication.Authentication;
import ch.epfl.sweng.favors.authentication.AuthenticationProcess;
import ch.epfl.sweng.favors.databinding.ActivityMainBinding;
import ch.epfl.sweng.favors.location.LocationHandler;


/**
 * This is the main class of the Favors application
 * the entry point for the actual app
 */
public class FavorsMain extends AppCompatActivity {

    public static final String TEST_MODE = "test-mode";

    public static final String TAG = "FAVORS_MAIN";
    public ObservableField<String> appName = new ObservableField<>("Favors");

    public ActivityMainBinding binding;

    public enum Permissions{LOCATION_SERVICE};

    private static Context context;
    public static Context getContext(){
        return context;
    }

    /**
     * This interface is the contract for receiving the results for permission requests.
     * @param requestCode - the int that defines which permission was asked for
     * @param permissions - the actual manifest permissions that are passed on
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Permissions p = Permissions.values()[requestCode];

        if(p == Permissions.LOCATION_SERVICE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                LocationHandler.getHandler().permissionFeedback();
            } else {
                Toast.makeText(this, "The app requires you to enable Location services!", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setElements(this);

        binding.loginButton.setOnClickListener(v-> loginView(AuthenticationProcess.Action.Login,  v));
        binding.registerButton.setOnClickListener(v->loginView(AuthenticationProcess.Action.Register,  v));

        // if logged in -> display main view
        if(Authentication.getInstance().isEmailVerified()){
            loggedinView();
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // request permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, Permissions.LOCATION_SERVICE.ordinal());
        }

    }

    public void loginView(AuthenticationProcess.Action action, View view){
        Intent intent = new Intent(view.getContext(), AuthenticationProcess.class);
        intent.putExtra(AuthenticationProcess.AUTHENTIFICATION_ACTION, action);
        startActivity(intent);
        finish();
    }

    private void loggedinView(){
        Intent intent = new Intent(this, LoggedInScreen.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {

    }
}