package ch.epfl.sweng.favors;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//import ch.epfl.sweng.favors.databinding.FragmentFavorsBinding;

public class FavorsFragment extends Fragment {
    private static final String TAG = "FAVOR_FRAGMENT";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favors, container, false);

    }
}
