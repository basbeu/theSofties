package ch.epfl.sweng.favors.favors;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.database.Favor;
import ch.epfl.sweng.favors.database.ObservableArrayList;
import ch.epfl.sweng.favors.databinding.FavorsListBinding;
import ch.epfl.sweng.favors.databinding.FragmentNotificationsBinding;

/**
 * The fragment which displays the notification of the current user authenticated.
 */
public class Notifications extends Fragment {
    FragmentNotificationsBinding binding;
    ObservableArrayList<Notification> notificationsList = new ObservableArrayList<>();
    NotificationListAdapter listAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }

}
