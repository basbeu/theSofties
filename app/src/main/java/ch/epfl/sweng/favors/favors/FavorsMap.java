package ch.epfl.sweng.favors.favors;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.databinding.FavorsMapBinding;

/**
 * Fragment that displays the list of favor and allows User to sort it and to search in it
 */
public class FavorsMap extends android.support.v4.app.Fragment {
    private static final String TAG = "FAVORS_MAP";

    FavorsMapBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.favors_map,container,false);
        binding.setElements(this);


        return binding.getRoot();
    }



}