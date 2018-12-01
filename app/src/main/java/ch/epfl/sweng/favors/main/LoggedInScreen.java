package ch.epfl.sweng.favors.main;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ObservableField;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.Timestamp;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.authentication.Authentication;
import ch.epfl.sweng.favors.database.Database;
import ch.epfl.sweng.favors.database.DatabaseEntity;
import ch.epfl.sweng.favors.database.Favor;
import ch.epfl.sweng.favors.database.FavorRequest;
import ch.epfl.sweng.favors.database.ObservableArrayList;
import ch.epfl.sweng.favors.database.User;
import ch.epfl.sweng.favors.database.fields.DatabaseField;
import ch.epfl.sweng.favors.database.storage.FirebaseStorageDispatcher;
import ch.epfl.sweng.favors.databinding.ActivityLoggedInScreenBinding;
import ch.epfl.sweng.favors.databinding.NavHeaderBinding;
import ch.epfl.sweng.favors.favors.MyFavorsFragment;
import ch.epfl.sweng.favors.location.LocationHandler;
import ch.epfl.sweng.favors.profile.ProfileFragment;
import ch.epfl.sweng.favors.settings.SettingsFragment;
import ch.epfl.sweng.favors.utils.ExecutionMode;
import ch.epfl.sweng.favors.utils.Utils;

public class LoggedInScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public ObservableField<String> firstName = User.getMain().getObservableObject(User.StringFields.firstName);
    public ObservableField<String> lastName = User.getMain().getObservableObject(User.StringFields.lastName);
    public ObservableField<String> location = LocationHandler.getHandler().locationCity;
    public final String TAG = "LOGGED_IN_SCREEN";
    private final static int GET_FROM_GALLERY = 66;
    private Uri selectedImage = ExecutionMode.getInstance().isTest() ? Uri.parse("test/picture") : null;
    private String storageRef;
    private ObservableField<String> profilePictureRef;
    private FirebaseStorageDispatcher storage = FirebaseStorageDispatcher.getInstance();

    ActivityLoggedInScreenBinding binding;
    NavHeaderBinding headerBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        User.updateMain();
        User.getMain().addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if(propertyId == User.UpdateType.FROM_DB.ordinal()){
                    // Must be remove when fake requests with multiple queries will be implemented
                    if(!ExecutionMode.getInstance().isTest()) reimburseExpiredFavors();
                    sender.removeOnPropertyChangedCallback(this);
                }
            }
        });

        binding = DataBindingUtil.setContentView(this, R.layout.activity_logged_in__screen);
        binding.setElements(this);

        binding.navView.setNavigationItemSelectedListener(this);
        headerBinding = DataBindingUtil.inflate(getLayoutInflater(),
                R.layout.nav_header, binding.navView, false);
        headerBinding.setElements(this);
        binding.navView.addHeaderView(headerBinding.getRoot());

        profilePictureRef = User.getMain().getObservableObject(User.StringFields.profilePicRef);
        Database.getInstance().updateFromDb(User.getMain()).addOnSuccessListener(v -> {
            if(profilePictureRef != null && profilePictureRef.get() != null){
                storage.displayImage(profilePictureRef, headerBinding.profilePicture, "profile");
            }
        });




        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout,
                binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Home()).commit();
            binding.navView.setCheckedItem(R.id.favors);
        }


       headerBinding.uploadProfilePicture.setOnClickListener(v-> startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY));
        headerBinding.deleteProfilePicture.setOnClickListener(v -> {
            Database.getInstance().updateFromDb(User.getMain()).addOnSuccessListener(t -> {
                if(profilePictureRef != null && profilePictureRef.get() != null){
                    storage.deleteImageFromStorage(profilePictureRef, "profile");
                    User.getMain().set(User.StringFields.profilePicRef, null);
                    headerBinding.profilePicture.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher));
                }
            });

        });
    }

    @Override
    public void onBackPressed() {
        if(binding.drawerLayout.isDrawerOpen(GravityCompat.START)){
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }

    }

    /**
     * This method is called on the result of method startActivityOnResult
     * Inspired from this tutorial : https://code.tutsplus.com/tutorials/image-upload-to-firebase-in-android-application--cms-29934
     * @param requestCode 66 if the activity is getting a picture from the gallery
     * @param resultCode -1 if OK
     * @param data the data corresponding to the picture
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                headerBinding.profilePicture.setImageBitmap(bitmap);
                storageRef = storage.uploadImage(FirebaseStorageDispatcher.getInstance().getReference(), this, selectedImage, "profile");
                ObservableField<String> oldRef = User.getMain().getObservableObject(User.StringFields.profilePicRef);
                if(oldRef != null && oldRef.get() != null){
                    storage.deleteImageFromStorage(oldRef, "profile");
                }
                User.getMain().set(User.StringFields.profilePicRef, storageRef);
                Database.getInstance().updateOnDb(User.getMain());

            } catch (FileNotFoundException e) { e.printStackTrace(); }
            catch (IOException e) { e.printStackTrace(); }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId())
        {
            case R.id.profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).addToBackStack(null).commit();
                break;
            case R.id.favors:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Home()).addToBackStack(null).commit();
                break;
            case R.id.myfavors:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MyFavorsFragment()).addToBackStack(null).commit();
                break;
            case R.id.settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).addToBackStack(null).commit();
                break;
            case R.id.logout:
                Utils.logout(this, Authentication.getInstance());
                break;
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    void reimburseExpiredFavors(){
        Log.d(TAG, "Start of reimbusrsment");
        ObservableArrayList<Favor> listFavors = new ObservableArrayList<>();
        Map<DatabaseField, Object> querryLess = new HashMap<>();
        Map<DatabaseField, Object> querryEqual = new HashMap<>();
        Map<DatabaseField, Object> querryGreater = new HashMap<>();


        querryEqual.put(Favor.StringFields.ownerID, Authentication.getInstance().getUid());
        querryLess.put(Favor.ObjectFields.expirationTimestamp,new Timestamp(new Date()));
        querryGreater.put(Favor.LongFields.nbPerson,0);

        FavorRequest.getList(listFavors,querryEqual,querryLess,null,null,null);
        Database.getInstance().updateFromDb(User.getMain()).addOnCompleteListener(task ->
                listFavors.addOnPropertyChangedCallback(
                        new Observable.OnPropertyChangedCallback() {
                            @Override
                            public void onPropertyChanged(Observable sender, int propertyId) {
                                long toReimburseTotal = 0;
                                Log.d(TAG, "We have received " + listFavors.size());
                                for (Favor f : listFavors) {
                                    Log.d(TAG, "The tokensPerPerson are: " + f.get(Favor.LongFields.tokenPerPerson));
                                    if(f.get(Favor.ObjectFields.interested) == null || ((ArrayList<String>)f.get(Favor.ObjectFields.interested)).isEmpty()){
                                        Log.d(TAG, "This favor is being treated: "+f.get(Favor.StringFields.title));
                                        long nbPersonneRemaining = f.get(Favor.LongFields.nbPerson);
                                        long tokenPerPerson = f.get(Favor.LongFields.tokenPerPerson);   //TODO change this once tokens are Integers
                                        f.set(Favor.LongFields.nbPerson,0L);
                                        //f.set(Favor.LongFields.tokenPerPerson,0L);
                                        toReimburseTotal += nbPersonneRemaining * tokenPerPerson;
                                        Database.getInstance().updateOnDb(f);
                                    }
                                    else{Log.d(TAG, "This favor is being not being treated: "+f.get(Favor.StringFields.title));}
                                }
                                long currentUserTokense = User.getMain().get(User.LongFields.tokens);
                                currentUserTokense += toReimburseTotal;
                                User.getMain().set(User.LongFields.tokens,currentUserTokense);
                                Database.getInstance().updateOnDb(User.getMain());
                            }
                        })
        );

    }
}
