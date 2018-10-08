package ch.epfl.sweng.favours;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.support.design.widget.NavigationView;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import ch.epfl.sweng.favours.databinding.ActivityLoggedInScreenBinding;
import ch.epfl.sweng.favours.databinding.NavHeaderBinding;


public class Logged_in_Screen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public ObservableField<String> firstName = User.getMain().getObservableStringObject(User.StringFields.firstName);
    public ObservableField<String> lastName = User.getMain().getObservableStringObject(User.StringFields.lastName);
    public ObservableField<String> email = User.getMain().getObservableStringObject(User.StringFields.email);

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
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                break;
            case R.id.favours:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FavoursFragment()).commit();
                break;
            case R.id.settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
                break;
            case R.id.logout:
                Toast.makeText(this, "See you soon !", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, FavoursMain.class);
                intent.putExtra(FavoursMain.LOGGED_OUT, FavoursMain.Status.Disconnect);
                RuntimeEnvironment.getInstance().isConnected.set(false);
                FirebaseAuth.getInstance().signOut();
                startActivity(intent);
                break;
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
