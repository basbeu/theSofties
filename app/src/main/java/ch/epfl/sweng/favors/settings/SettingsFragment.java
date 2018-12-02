package ch.epfl.sweng.favors.settings;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.database.internal_db.LocalPreferences;
import ch.epfl.sweng.favors.databinding.SettingsLayoutBinding;

public class SettingsFragment extends Fragment {

    private static final String TAG = "SETTINGS_FRAGMENT";

    SettingsLayoutBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.settings_layout,container,false);

        binding.emailNotifToggle.setChecked(LocalPreferences.getInstance().isEmailNofifEnabled());
        binding.appNotifToggle.setChecked(LocalPreferences.getInstance().isAppNotifEnabled());


        binding.emailNotifToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            LocalPreferences.getInstance().setEmailNofifEnabled(isChecked);
        });

        binding.appNotifToggle.setOnCheckedChangeListener((buttonView, isChecked) ->{
            LocalPreferences.getInstance().setAppNotifEnabled(isChecked);
        } );

        return binding.getRoot();
    }
}
