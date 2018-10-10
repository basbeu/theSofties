package ch.epfl.sweng.favors;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ch.epfl.sweng.favors.database.User;
import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.databinding.FragmentProfileLayoutBinding;

public class ProfileFragment extends Fragment {

    private static final String LOG_TAG = "PROFILE_FRAGMENT";
    public ObservableField<String> firstName = User.getMain().getObservableStringObject(User.StringFields.firstName);
    public ObservableField<String> lastName = User.getMain().getObservableStringObject(User.StringFields.lastName);
    public ObservableField<String> baseCity = User.getMain().getObservableStringObject(User.StringFields.basedLocation);
    public ObservableField<String> sex = User.getMain().getObservableStringObject(User.StringFields.sex);
    public ObservableField<String> email = User.getMain().getObservableStringObject(User.StringFields.email);

    FragmentProfileLayoutBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_layout,container,false);
        binding.setElements(this);

        binding.editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                fragment = new EditProfileFragment();
                replaceFragment(fragment);
            }
        });

        return binding.getRoot();
    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
