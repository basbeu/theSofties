package ch.epfl.sweng.favors;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.databinding.ActivityMainBinding;

/**
 * This is the main class of the Favors application
 * the entry point for the actual app
 */
public class FavorsMain extends AppCompatActivity {

    public static final String TAG = "FavorsApp";
    public ObservableField<String> appName = new ObservableField<>("Favors");

    /**-----------------**/
    /**      Login      **/
    /**-----------------**/
    public enum Status{Register, Login, LoggedIn, Disconnect, Reset};
    public static String AUTHENTIFICATION_ACTION = "AUTHENTIFICATION_ACTION";
    public ActivityMainBinding binding;

    public ObservableBoolean isConnected;
    public static final String LOGGED_IN = "Logged in successfully";
    public static final String LOGGED_OUT = "Disconnected successfully";

    /**-----------------**/
    /**     Context     **/
    /**-----------------**/
    private RuntimeEnvironment runtimeEnvironment;

    private static Context context;
    public static Context getContext(){
        return context;
    }

    /**-----------------**/
    /**     Location    **/
    /**-----------------**/
    // client used for location
    private FusedLocationProviderClient mFusedLocationClient;
    // permissions
    public enum Permissions{LOCATION_REQUEST};
    // location
    private Location lastLocation;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    // decides continuous location updates
    private boolean isContinue = false;

    /**
     * onCreate is where you initialize your activity
     * Most importantly, here you will usually call setContentView(int) with a layout resource defining your UI,
     * and using findViewById(int) to retrieve the widgets in that UI that you need to interact with programmatically.
     * @param savedInstanceState of type Bundle - contains must recent data after shutdown or null
     */
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

        // if logged in -> display main view
        if(RuntimeEnvironment.getInstance().isConnected.get() && FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()){
            loggedinView(Status.LoggedIn);
        }

        // check for GPS localization permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // request permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    Permissions.LOCATION_REQUEST.ordinal());
        } else {
            // permission already granted
        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // set periodic updates of location
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(60 * 1000); // 60 seconds
        locationRequest.setFastestInterval(30 * 1000); // 30 seconds
        // callback methods
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Log.d("debugRemove", "enters callback");
                if (locationResult == null) {
                    return;
                }
                for (Location l : locationResult.getLocations()) {
                    if (l != null) { lastLocation = l; debugLogs(); }
                }
            }
        };

        getLocation();
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

    /**
     * this method checks if we have the permission to access a user's location
     * @return if we are allowed (boolean)
     */
    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // ask for permissions
            requestPermissions();
            Log.d("location", "code:1001 - location services have not been granted yet, asking now");
            return false;
        } else {
            // Permission has already been granted
            Log.d("location", "code:1000 - location services already granted");
            return true;
        }
    }

    /**
     * GPS localization permission
     * this method requests the permission of the user to access his location
     */
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                Permissions.LOCATION_REQUEST.ordinal());
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
        // get request code
        Permissions p = Permissions.values()[requestCode];
        switch (p) {
            case LOCATION_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("debugRemove", "enters onRequestPermissionsResult");
                    // request permission to access location, if granted assign to lastLocation
                    getLocation();
                } else {
                    // permission denied
                    Toast.makeText(this, "The app requires you to enable Location services!", Toast.LENGTH_SHORT).show();
                    Log.e("location", "code:404 - location service where not granted");
                }
                return;
            }
        }
    }

    /**
     * Request Location Updates
     * Helper method that invokes the callback when the permissions allow it
     * @param callback for Location
     */
    private void requestUpdatesHelper(LocationCallback callback) {
        if (checkPermissions()) {
            mFusedLocationClient.requestLocationUpdates(locationRequest, callback, null);
        }
    }

    /**
     * Get Location
     * Helper method for obtaining a location
     * adds a listener to LocationClient and if there is no location invokes a callback
     * @param callback for Location
     * @return the location value that was obtained (value can be ignored)
     */
    private Location getLocationHelper(LocationCallback callback) {
        if(checkPermissions()) {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, l -> {
                if (l != null) { lastLocation = l; debugLogs(); }
                else { requestUpdatesHelper(callback); }
            });
        }
        return lastLocation;
    }

    /**
     * This method call handles all the location requests.
     * It updates the location when it is stale or when there is none
     */
    private void getLocation() {
        if (isContinue) { requestUpdatesHelper(locationCallback); }
        else { getLocationHelper(locationCallback); }
    }

    /**
     * Debug logs that will only be printed to console in non-release mode
     */
    private void debugLogs() {
        Log.d("location", "code:1002 - we have a location: (" + lastLocation.getLatitude() + ", " + lastLocation.getLongitude()+(")"));
    }
}

