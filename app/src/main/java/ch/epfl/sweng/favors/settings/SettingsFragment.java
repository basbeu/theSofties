package ch.epfl.sweng.favors.settings;

import android.database.DataSetObserver;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.database.internal_db.LocalPreferences;
import ch.epfl.sweng.favors.databinding.SettingsLayoutBinding;
import ch.epfl.sweng.favors.main.LoggedInScreen;

public class SettingsFragment extends Fragment {

    private static final String TAG = "SETTINGS_FRAGMENT";

    private Map<String, Integer> colorsMap;

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

        colorsMap = new HashMap<>();
        colorsMap.put("Grey", R.color.colorToolbar);
        colorsMap.put("Orange", R.color.roseStart);
        colorsMap.put("Blue", R.color.blueEnd);
        colorsMap.put("Purple", R.color.purpleStart);

        ArrayAdapter adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, new ArrayList<>(colorsMap.keySet()));

        binding.colors.setAdapter(adapter);
        binding.colors.setSelection(new ArrayList<>(colorsMap.values()).indexOf(LocalPreferences.getInstance().getColor()));

        binding.colors.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String color = binding.colors.getItemAtPosition(position).toString();
                LocalPreferences.getInstance().setColor(colorsMap.get(color));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return binding.getRoot();
    }
}
