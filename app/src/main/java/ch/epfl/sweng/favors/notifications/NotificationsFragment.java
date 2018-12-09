package ch.epfl.sweng.favors.notifications;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.database.Database;
import ch.epfl.sweng.favors.database.NotificationEntity;
import ch.epfl.sweng.favors.database.ObservableArrayList;
import ch.epfl.sweng.favors.database.User;
import ch.epfl.sweng.favors.databinding.FragmentNotificationsBinding;
import ch.epfl.sweng.favors.notifications.NotificationListAdapter;

/**
 * The fragment which displays the notification of the current user authenticated.
 */
public class NotificationsFragment extends Fragment {
    private static final String TAG = "NOTIFICATIONS_LIST";

    FragmentNotificationsBinding binding;
    ArrayList<String> notificationsList = new ObservableArrayList<>();
    NotificationListAdapter listAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notifications,container,false);
        binding.setElements(this);

        notificationsList = new ArrayList<>();
        listAdapter = new NotificationListAdapter(notificationsList);

        binding.notificationsList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.notificationsList.setAdapter(listAdapter);

        String currUserId = User.getMain().getId();

        Database.getInstance().addSnapshotListener(getActivity(), NotificationEntity.getCollection(currUserId),(queryDocumentSnapshots, e) -> {
            for (DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()) {

                String notificationMsg = doc.getDocument().getData().get("message").toString();
                notificationsList.add(notificationMsg);

                listAdapter.notifyDataSetChanged();
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
