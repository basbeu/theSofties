package ch.epfl.sweng.favors.main;

import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.authentication.Authentication;
import ch.epfl.sweng.favors.database.Database;
import ch.epfl.sweng.favors.database.DatabaseEntity;
import ch.epfl.sweng.favors.database.FakeDatabase;
import ch.epfl.sweng.favors.database.Favor;
import ch.epfl.sweng.favors.database.FavorRequest;
import ch.epfl.sweng.favors.database.ObservableArrayList;
import ch.epfl.sweng.favors.database.User;
import ch.epfl.sweng.favors.database.fields.DatabaseField;
import ch.epfl.sweng.favors.databinding.ActivityLoggedInScreenBinding;
import ch.epfl.sweng.favors.databinding.NavHeaderBinding;
import ch.epfl.sweng.favors.favors.FavorsFragment;
import ch.epfl.sweng.favors.favors.MyFavorsFragment;
import ch.epfl.sweng.favors.location.LocationHandler;
import ch.epfl.sweng.favors.profile.ProfileFragment;
import ch.epfl.sweng.favors.settings.SettingsFragment;
import ch.epfl.sweng.favors.utils.Utils;

public class LoggedInScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public ObservableField<String> firstName = User.getMain().getObservableObject(User.StringFields.firstName);
    public ObservableField<String> lastName = User.getMain().getObservableObject(User.StringFields.lastName);
    public ObservableField<String> location = LocationHandler.getHandler().locationCity;
    public final String TAG = "LOGGED_IN_SCREEN";

    ActivityLoggedInScreenBinding binding;
    NavHeaderBinding headerBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Database.getInstance().updateFromDb(User.getMain());
        binding = DataBindingUtil.setContentView(this, R.layout.activity_logged_in__screen);
        binding.setElements(this);

        binding.navView.setNavigationItemSelectedListener(this);
        headerBinding = DataBindingUtil.inflate(getLayoutInflater(),
                R.layout.nav_header, binding.navView, false);
        headerBinding.setElements(this);
        binding.navView.addHeaderView(headerBinding.getRoot());

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout,
                binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Home()).commit();
            binding.navView.setCheckedItem(R.id.favors);
        }
        reimburseExpiredFavors();

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
        ObservableArrayList<Favor> listFavors = new ObservableArrayList<>();
        Map<DatabaseField, Object> querryLess = new HashMap<>();
        Map<DatabaseField, Object> querryEqual = new HashMap<>();
        Map<DatabaseField, Object> querryGreater = new HashMap<>();


        querryEqual.put(Favor.StringFields.ownerID, Authentication.getInstance().getUid());
        querryLess.put(Favor.ObjectFields.expirationTimestamp,new Timestamp(new Date()));
        querryGreater.put(Favor.IntegerFields.nbPerson,0);

        FavorRequest.getList(listFavors,querryEqual,querryLess,querryGreater,null,null);
        listFavors.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                int toReimburseTotal = 0;
                Log.d(TAG, "We have received " + listFavors.size());
                for (Favor f : listFavors) {
                    if(f.get(Favor.ObjectFields.interested) instanceof ArrayList && ((ArrayList<String>)f.get(Favor.ObjectFields.interested)).isEmpty()){
                        int nbPersonneRemaining = f.get(Favor.IntegerFields.nbPerson);
                        int tokenPerPerson = Integer.parseInt(f.get(Favor.StringFields.tokens));   //TODO change this once tokens are Integers
                        f.set(Favor.IntegerFields.nbPerson,0);
                        toReimburseTotal += nbPersonneRemaining * tokenPerPerson;
                    }
                }
                int currentUserTokense = Integer.parseInt(User.getMain().get(User.StringFields.tokens));
                currentUserTokense += toReimburseTotal;
                User.getMain().set(User.StringFields.tokens,currentUserTokense+"");
            }
        });
    }
}
