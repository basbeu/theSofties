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

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.database.Database;
import ch.epfl.sweng.favors.database.Favor;
import ch.epfl.sweng.favors.database.ObservableArrayList;
import ch.epfl.sweng.favors.database.User;
import ch.epfl.sweng.favors.databinding.FragmentUsersSelectionBinding;

public class UsersSelectionFragment extends android.support.v4.app.Fragment {
    private static final String TAG = "USERS_SELECTION_FRAG";
    protected static final String INTERESTED_PEOPLE = "INTERESTED_PEOPLE";
    protected static final String SELECTED_PEOPLE = "SELECTED_PEOPLE";
    protected static final String FAVOR = "FAVOR";

    FragmentUsersSelectionBinding binding;
    ArrayList<String> interestedUsers = new ArrayList<>();
    ArrayList<String> selectedUsers = new ArrayList<>();
    Favor localFavor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_users_selection,container,false);
        binding.setElements(this);

        binding.selectionDone.setOnClickListener(v -> {
            localFavor.set(Favor.ObjectFields.selectedPeople, selectedUsers);
            Database.getInstance().updateOnDb(localFavor);
            getActivity().onBackPressed();
        });

        binding.interestedPeopleList.setLayoutManager(new LinearLayoutManager(getContext()));

        UsersSelectionListAdapter adapter = new UsersSelectionListAdapter(this.getActivity(), interestedUsers, selectedUsers);
        binding.interestedPeopleList.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();

        if (arguments != null) {
            if(arguments.containsKey(INTERESTED_PEOPLE)) {
                ArrayList<String> interested = arguments.getStringArrayList(INTERESTED_PEOPLE);
                interestedUsers.addAll(interested);
            }
            if(arguments.containsKey(SELECTED_PEOPLE)) {
                ArrayList<String> selected = arguments.getStringArrayList(SELECTED_PEOPLE);
                Log.d(TAG, ""+selected.size());
                selectedUsers.addAll(selected);
            }
            if(arguments.containsKey(FAVOR)) {
                String favorId = arguments.getString(FAVOR);
                localFavor = new Favor(favorId);
            }
        }

    }


}
