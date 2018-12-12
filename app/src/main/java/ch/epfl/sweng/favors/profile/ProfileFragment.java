package ch.epfl.sweng.favors.profile;

import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.authentication.Authentication;
import ch.epfl.sweng.favors.authentication.AuthenticationProcess;
import ch.epfl.sweng.favors.database.Database;
import ch.epfl.sweng.favors.database.User;
import ch.epfl.sweng.favors.databinding.FragmentProfileLayoutBinding;
import ch.epfl.sweng.favors.utils.TextWatcherCustom;
import ch.epfl.sweng.favors.utils.Utils;

public class ProfileFragment extends Fragment {

    private static final String LOG_TAG = "PROFILE_FRAGMENT";

    public ObservableField<String> firstName = User.getMain().getObservableObject(User.StringFields.firstName);
    public ObservableField<String> lastName = User.getMain().getObservableObject(User.StringFields.lastName);
    public ObservableField<String> baseCity = User.getMain().getObservableObject(User.StringFields.city);
    public ObservableField<String> sex = User.getMain().getObservableObject(User.StringFields.sex);
    public ObservableField<String> email = User.getMain().getObservableObject(User.StringFields.email);
    public ObservableField<String> profileName =  new ObservableField<>();
    public ObservableField<Long> tokens = User.getMain().getObservableObject(User.LongFields.tokens);

    public AuthenticationProcess.Action action;

    FragmentProfileLayoutBinding binding;

    public ObservableBoolean isPasswordCorrect = new ObservableBoolean(false);

    private TextWatcher passwordTextWatcher = new TextWatcherCustom() {
        @Override
        public void afterTextChanged(Editable editable) {
            isPasswordCorrect.set(Utils.passwordFitsRequirements(binding.passwordEntry.getText().toString()));
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        Database.getInstance().updateFromDb(User.getMain());

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_layout,container,false);
        binding.setElements(this);
        // Check if the password is correct each time a letter was added
        binding.passwordEntry.addTextChangedListener(passwordTextWatcher);
        binding.editProfileButton.setOnClickListener((v)-> {
            Fragment fragment = null;
            fragment = new EditProfileFragment();
            replaceFragment(fragment);
        });
        binding.delete.setOnClickListener((v)-> {
            getView().findViewById(R.id.reauthenticateMessage).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.passwordEntry).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.confirmDeletion).setVisibility(View.VISIBLE);


        });

        binding.confirmDeletion.setOnClickListener((v)-> {
            //toast if account has been correctly deleted or not
            Utils.logout(this.getContext(), Authentication.getInstance());
           // Authentication.getInstance().delete();

        });


        updateTitle();

        return binding.getRoot();
    }

    private void updateTitle(){
        firstName.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                profileName.set(firstName.get() + "'s Profile");
            }
        });
    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
