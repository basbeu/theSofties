package ch.epfl.sweng.favours;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import com.google.firebase.auth.FirebaseAuth;

import ch.epfl.sweng.favours.database.User;
import ch.epfl.sweng.favours.databinding.ActivitySetUserInfoBinding;

import ch.epfl.sweng.favours.database.User;

public class SetUserInfo extends AppCompatActivity {

    private static final String TAG = "INIT_PROFILE_FRAGMENT";

    public ObservableField<String> firstName = User.getMain().getObservableStringObject(User.StringFields.firstName);
    public ObservableField<String> lastName = User.getMain().getObservableStringObject(User.StringFields.lastName);
    public ObservableField<String> baseCity = User.getMain().getObservableStringObject(User.StringFields.basedLocation);
    public ObservableField<String> sexe = User.getMain().getObservableStringObject(User.StringFields.sex);

    ActivitySetUserInfoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        User.getMain().setMainUser(FirebaseAuth.getInstance().getCurrentUser().getUid());

        binding = DataBindingUtil.setContentView(this, R.layout.activity_set_user_info);
        binding.setElements(this);

        User.getMain().set(User.StringFields.email, FirebaseAuth.getInstance().getCurrentUser().getEmail());

        binding.userFirstNameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                User.getMain().set(User.StringFields.firstName, s.toString());
            }
        });

        binding.userLastNameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                User.getMain().set(User.StringFields.lastName, s.toString());
            }
        });

        binding.userCityEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                User.getMain().set(User.StringFields.basedLocation, s.toString());
            }
        });

        binding.userSexEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                User.getMain().set(User.StringFields.sex, s.toString());
            }
        });
    }
}
