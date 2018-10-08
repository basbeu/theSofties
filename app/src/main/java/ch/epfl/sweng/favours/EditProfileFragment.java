package ch.epfl.sweng.favours;

import android.content.Context;import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ch.epfl.sweng.favours.databinding.FragmentEditProfileBinding;


import javax.annotation.Nonnull;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProfileFragment extends Fragment {

    private static final String LOG_TAG = "EDIT_PROFILE_FRAGMENT";

    public ObservableField<String> firstName = User.getMain().getObservableStringObject(User.StringFields.firstName);
    public ObservableField<String> lastName = User.getMain().getObservableStringObject(User.StringFields.lastName);
    public ObservableField<String> baseCity = User.getMain().getObservableStringObject(User.StringFields.basedLocation);
    public ObservableField<String> sexe = User.getMain().getObservableStringObject(User.StringFields.sex);


    FragmentEditProfileBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_profile,container,false);
         binding.setElements(this);

        binding.profFirstNameEdit.addTextChangedListener(new TextWatcher() {
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

        binding.profLastNameEdit.addTextChangedListener(new TextWatcher() {
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

        binding.profCityEdit.addTextChangedListener(new TextWatcher() {
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

        binding.profSexEdit.addTextChangedListener(new TextWatcher() {
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

        binding.commitChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User.getMain().updateUserDataOnServer();
            }
        });

         return binding.getRoot();
    }
    
}
