package ch.epfl.sweng.favors.favors;

import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.database.ObservableArrayList;
import ch.epfl.sweng.favors.database.User;
import ch.epfl.sweng.favors.databinding.FragmentNotificationsBinding;

/**
 * The fragment which displays the notification of the current user authenticated.
 */
public class Notifications extends Fragment {
    private static final String TAG = "NOTIFICATIONS_LIST";

    FragmentNotificationsBinding binding;
    ArrayList<String> notificationsList = new ObservableArrayList<>();
    NotificationListAdapter listAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notifications,container,false);
        binding.setElements(this);

        binding.notificationsList.setLayoutManager(new LinearLayoutManager(getContext()));

        notificationsList = (ArrayList<String>)(User.getMain().get(User.ObjectFields.notifications));
        try {
            Thread.sleep(5000);
        }catch (Exception e){}
        listAdapter = new NotificationListAdapter(notificationsList);
        binding.notificationsList.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();

        return binding.getRoot();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    Observable.OnPropertyChangedCallback listCB = new Observable.OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable sender, int propertyId) {
            updateList((ObservableArrayList)sender);
        }
    };

    private void updateList(ObservableArrayList<String> list){
        listAdapter = new NotificationListAdapter(list);
        binding.notificationsList.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
    }
}
