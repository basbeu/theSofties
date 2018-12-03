package ch.epfl.sweng.favors.authentication;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.widget.RadioGroup;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.database.Database;
import ch.epfl.sweng.favors.database.User;
import ch.epfl.sweng.favors.databinding.ActivitySetUserInfoBinding;
import ch.epfl.sweng.favors.location.LocationHandler;
import ch.epfl.sweng.favors.main.FavorsMain;
import ch.epfl.sweng.favors.utils.ExecutionMode;
import ch.epfl.sweng.favors.utils.TextWatcherCustom;

public class SetUserInfo extends AppCompatActivity {


    private static final String TAG = "INIT_PROFILE_FRAGMENT";

    private User user = new User(Authentication.getInstance().getUid());

    public ObservableField<String> firstName = user.getObservableObject(User.StringFields.firstName);
    public ObservableField<String> lastName = user.getObservableObject(User.StringFields.lastName);
    public ObservableField<String> baseCity = user.getObservableObject(User.StringFields.city); // LocationHandler.getHandler().locationCity;


    ActivitySetUserInfoBinding binding;
    private TextWatcherCustom firstNameWatcher = new TextWatcherCustom() {
        @Override
        public void afterTextChanged(Editable editable) {
            user.set(User.StringFields.firstName, editable.toString());
        }
    };
    private TextWatcherCustom lastNameWatcher = new TextWatcherCustom() {
        @Override
        public void afterTextChanged(Editable editable) {
            user.set(User.StringFields.lastName, editable.toString());
        }
    };

    private TextWatcherCustom basedLocationWatcher = new TextWatcherCustom() {
        @Override
        public void afterTextChanged(Editable editable) {
            user.set(User.StringFields.city, editable.toString());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user.set(User.StringFields.email, Authentication.getInstance().getEmail());
        user.set(User.LongFields.tokens, User.DEFAULT_TOKENS_NUMBER);
        baseCity.set(LocationHandler.getHandler().locationCity.get());

        binding = DataBindingUtil.setContentView(this, R.layout.activity_set_user_info);
        binding.setElements(this);

        bindUi();
    }

    private void bindUi(){
        binding.userFirstNameEdit.addTextChangedListener(firstNameWatcher);

        binding.userLastNameEdit.addTextChangedListener(lastNameWatcher);

        binding.userCityEdit.addTextChangedListener(basedLocationWatcher);

        binding.profGenderEdit.setOnCheckedChangeListener((RadioGroup group, int checkedId) ->{
            switch (checkedId){
                case R.id.profGenderMEdit:
                    User.UserGender.setGender(user,User.UserGender.M);
                    break;
                case  R.id.profGenderFEdit:
                    User.UserGender.setGender(user, User.UserGender.F);
                    break;
                default:
                    Log.e(TAG, "RadioButton clicked for sex change unidentified");
            }
        });

        binding.submit.setOnClickListener(v->{
            Database.getInstance().updateOnDb(user);
            Intent intent = new Intent(this, ConfirmationSent.class);
            startActivity(intent);
        });
    }

    /*
    Explicitly calls the FavorMain because the back button will not work to go back to FavorMain.
    This behavior is wanted because we don't want to accidentally have a user reach the login screen when he is logged in
     */
    @Override
    public void onBackPressed() {
        Authentication.getInstance().delete();
        Authentication.getInstance().signOut();
        Intent intent = new Intent(this, FavorsMain.class);
        startActivity(intent);
        finish();
    }
}