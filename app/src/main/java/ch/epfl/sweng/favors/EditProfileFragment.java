package ch.epfl.sweng.favors;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.database.User;
import ch.epfl.sweng.favors.databinding.FragmentEditProfileBinding;


public class EditProfileFragment extends Fragment {

    private static final String TAG = "EDIT_PROFILE_FRAGMENT";

    public ObservableField<String> firstName = User.getMain().getObservableStringObject(User.StringFields.firstName);
    public ObservableField<String> lastName = User.getMain().getObservableStringObject(User.StringFields.lastName);
    public ObservableField<String> baseCity = User.getMain().getObservableStringObject(User.StringFields.location);
    public ObservableField<String> sex = User.getMain().getObservableStringObject(User.StringFields.sex);


    FragmentEditProfileBinding binding;

    private TextWatcherCustom profFirstNameEditWatcher = new TextWatcherCustom() {
        @Override
        public void afterTextChanged(Editable editable) {
            User.getMain().set(User.StringFields.firstName, editable.toString());
        }
    };

    private TextWatcherCustom profLastNameEditWatcher = new TextWatcherCustom() {
        @Override
        public void afterTextChanged(Editable editable) {
            User.getMain().set(User.StringFields.lastName, editable.toString());
        }
    };


    private TextWatcherCustom profCityEditWatcher = new TextWatcherCustom() {
        @Override
        public void afterTextChanged(Editable editable) {
            User.getMain().set(User.StringFields.location, editable.toString());
        }
    };

    private TextWatcherCustom profSexEditWatcher = new TextWatcherCustom() {
        @Override
        public void afterTextChanged(Editable editable) {
            User.getMain().set(User.StringFields.sex, editable.toString());
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


         binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_profile,container,false);
         binding.setElements(this);

         User.getMain().set(User.StringFields.email, FirebaseAuth.getInstance().getCurrentUser().getEmail());

         binding.profFirstNameEdit.addTextChangedListener(profFirstNameEditWatcher);

         binding.profLastNameEdit.addTextChangedListener(profLastNameEditWatcher);

         binding.profCityEdit.addTextChangedListener(profCityEditWatcher);

         binding.profSexEdit.addTextChangedListener(profSexEditWatcher);

         binding.commitChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User.getMain().updateOnDb();
                EditProfileFragment.this.getActivity().getSupportFragmentManager().popBackStack();
            }
        });

         return binding.getRoot();
    }

}
