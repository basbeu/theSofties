package ch.epfl.sweng.favors.favors;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.database.ObservableArrayList;
import ch.epfl.sweng.favors.database.User;
import ch.epfl.sweng.favors.databinding.FragmentUsersSelectionBinding;

public class UsersSelectionFragment extends android.support.v4.app.Fragment {
    private static final String TAG = "USERS_SELECTION_FRAGMENT";

    FragmentUsersSelectionBinding binding;
    ObservableArrayList<User> favorList = new ObservableArrayList<>();
    UsersSelectionListAdapter listAdapter;
    private SharedViewFavor sharedViewFavor;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_users_selection,container,false);
        binding.setElements(this);

        //button redirects to creating favor page
        binding.interestedPeopleList.setLayoutManager(new LinearLayoutManager(getContext()));

        return binding.getRoot();
    }


}
