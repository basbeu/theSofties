package ch.epfl.sweng.favors.main;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.authentication.Authentication;
import ch.epfl.sweng.favors.database.User;
import ch.epfl.sweng.favors.databinding.ActivityLoggedInScreenBinding;
import ch.epfl.sweng.favors.databinding.NavHeaderBinding;
import ch.epfl.sweng.favors.favors.FavorsFragment;
import ch.epfl.sweng.favors.profile.ProfileFragment;
import ch.epfl.sweng.favors.settings.SettingsFragment;
import ch.epfl.sweng.favors.utils.Utils;

public class LoggedInScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private User user = new User();
    public ObservableField<String> firstName = user.getObservableObject(User.StringFields.firstName);
    public ObservableField<String> lastName = user.getObservableObject(User.StringFields.lastName);
    public ObservableField<String> email = user.getObservableObject(User.StringFields.email);

    ActivityLoggedInScreenBinding binding;
    NavHeaderBinding headerBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
            binding.navView.setCheckedItem(R.id.profile);
        }

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
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FavorsFragment()).addToBackStack(null).commit();
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
}
