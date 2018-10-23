package ch.epfl.sweng.favors;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.google.firebase.auth.FirebaseAuth;

import ch.epfl.sweng.favors.database.User;
import ch.epfl.sweng.favors.databinding.FragmentEditProfileBinding;


public class EditProfileFragment extends Fragment {

    private static final String TAG = "EDIT_PROFILE_FRAGMENT";

    public ObservableField<String> firstName = User.getMain().getObservableObject(User.StringFields.firstName);
    public ObservableField<String> lastName = User.getMain().getObservableObject(User.StringFields.lastName);
    public ObservableField<String> baseCity = User.getMain().getObservableObject(User.StringFields.city);
    public ObservableField<String> sex = User.getMain().getObservableObject(User.StringFields.sex);


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
            User.getMain().set(User.StringFields.city, editable.toString());
        }
    };

    private void displayGender(){
        //Log.d(TAG, UserGender.getGenderFromUser(User.getMain()).toString());
        User.UserGender gender = User.UserGender.getGenderFromUser(User.getMain());
        Log.d(TAG,gender.toString());
        switch (gender){
            case F: binding.profGenderEdit.check(R.id.profGenderFEdit); break;
            case M: binding.profGenderEdit.check(R.id.profGenderMEdit); break;
            case DEFAULT: Log.e(TAG,"Gender parsing issue.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

         binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_profile,container,false);
         binding.setElements(this);
         //Set the RadioGroup buttons to select the current sex
         displayGender();

         binding.profFirstNameEdit.addTextChangedListener(profFirstNameEditWatcher);

         binding.profLastNameEdit.addTextChangedListener(profLastNameEditWatcher);

         binding.profCityEdit.addTextChangedListener(profCityEditWatcher);

         binding.profGenderEdit.setOnCheckedChangeListener((RadioGroup group, int checkedId) ->{
             switch (checkedId){
                 case R.id.profGenderMEdit:
                     User.UserGender.setGender(User.getMain(),User.UserGender.M);
                     break;
                 case  R.id.profGenderFEdit:
                     User.UserGender.setGender(User.getMain(), User.UserGender.F);
                     break;
                 default:
                     Log.e(TAG, "RadioButton clicked for sex change unidentified");
             }
         });

         binding.commitChanges.setOnClickListener((v)-> {
                User.getMain().updateOnDb();
                EditProfileFragment.this.getActivity().getSupportFragmentManager().popBackStack();
        });
         return binding.getRoot();
    }
}
