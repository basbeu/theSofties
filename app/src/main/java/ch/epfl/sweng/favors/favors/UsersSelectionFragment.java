package ch.epfl.sweng.favors.favors;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Map;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.database.Database;
import ch.epfl.sweng.favors.database.Favor;
import ch.epfl.sweng.favors.database.ObservableArrayList;
import ch.epfl.sweng.favors.database.User;
import ch.epfl.sweng.favors.databinding.FragmentUsersSelectionBinding;

public class UsersSelectionFragment extends android.support.v4.app.Fragment {
    private static final String TAG = "USERS_SELECTION_FRAG";

    FragmentUsersSelectionBinding binding;
    ArrayList<User> interestedUsers = new ArrayList<>();
    ArrayList<String> selectedUsers = new ArrayList<>();
    private Long maxToSelect;

    public void setUserNames(ArrayList<User> interestedUsers) {
        this.interestedUsers = interestedUsers;
    }
    public void setSelectedUsers(ArrayList<String> selectedUsers) {
        this.selectedUsers = selectedUsers;
    }

    public void setMaxToSelect(Long maxToSelect) {
        this.maxToSelect = maxToSelect;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_users_selection,container,false);
        binding.setElements(this);

        binding.selectionDone.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });

        binding.interestedPeopleList.setLayoutManager(new LinearLayoutManager(getContext()));

        UsersSelectionListAdapter adapter = new UsersSelectionListAdapter(this.getActivity(), interestedUsers, selectedUsers, maxToSelect);
        binding.interestedPeopleList.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(interestedUsers == null || selectedUsers == null || maxToSelect == null) {
            Log.e(TAG, "The fragment can't be intent, data are missing");
            (UsersSelectionFragment.this).getFragmentManager().beginTransaction().remove(this).commit();
        }
    }


}
