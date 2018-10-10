package ch.epfl.sweng.favors;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;

import com.google.firebase.auth.FirebaseAuth;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.database.User;
import ch.epfl.sweng.favors.databinding.ActivitySetUserInfoBinding;

public class SetUserInfo extends AppCompatActivity {

    private static final String TAG = "INIT_PROFILE_FRAGMENT";

    public ObservableField<String> firstName = User.getMain().getObservableStringObject(User.StringFields.firstName);
    public ObservableField<String> lastName = User.getMain().getObservableStringObject(User.StringFields.lastName);
    public ObservableField<String> baseCity = User.getMain().getObservableStringObject(User.StringFields.basedLocation);
    public ObservableField<String> sexe = User.getMain().getObservableStringObject(User.StringFields.sex);

    ActivitySetUserInfoBinding binding;
    private TextWatcherCustom firstNameWatcher = new TextWatcherCustom() {
        @Override
        public void afterTextChanged(Editable editable) {
            User.getMain().set(User.StringFields.firstName, editable.toString());
        }
    };
    private TextWatcherCustom lastNameWatcher = new TextWatcherCustom() {
        @Override
        public void afterTextChanged(Editable editable) {
            User.getMain().set(User.StringFields.lastName, editable.toString());
        }
    };

    private TextWatcherCustom basedLocationWatcher = new TextWatcherCustom() {
        @Override
        public void afterTextChanged(Editable editable) {
            User.getMain().set(User.StringFields.basedLocation, editable.toString());
        }
    };

    private TextWatcherCustom sexWatcher = new TextWatcherCustom() {
        @Override
        public void afterTextChanged(Editable editable) {
            User.getMain().set(User.StringFields.sex, editable.toString());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        User.getMain().setMain(FirebaseAuth.getInstance().getCurrentUser().getUid());

        binding = DataBindingUtil.setContentView(this, R.layout.activity_set_user_info);
        binding.setElements(this);

        User.getMain().set(User.StringFields.email, FirebaseAuth.getInstance().getCurrentUser().getEmail());
        bindUi();
    }

    private void bindUi(){
        binding.userFirstNameEdit.addTextChangedListener(firstNameWatcher);

        binding.userLastNameEdit.addTextChangedListener(lastNameWatcher);

        binding.userCityEdit.addTextChangedListener(basedLocationWatcher);

        binding.userSexEdit.addTextChangedListener(sexWatcher);

        binding.submit.setOnClickListener(v->{
            User.getMain().updateOnDb();
            Intent intent = new Intent(this, ConfirmationSent.class);
            startActivity(intent);
        });
    }
}