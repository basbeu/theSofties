package ch.epfl.sweng.favors.favors;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.authentication.Authentication;
import ch.epfl.sweng.favors.database.Favor;
import ch.epfl.sweng.favors.database.ObservableArrayList;
import ch.epfl.sweng.favors.database.User;

public class UsersSelectionListAdapter extends RecyclerView.Adapter<UsersSelectionListAdapter.NotificationViewHolder>{
    private static final String TAG = "USERS_SELECTION_LIST_ADAPTER";
    private ObservableArrayList<User> interestedPeople;

    public interface OnItemClickListener {
        void onItemClick(User item);
    }

    public class UsersSelectionViewHoleder extends RecyclerView.ViewHolder{
        public TextView username;
        public ImageView profilePicture;
        public UsersSelectionListAdapter adapter;

        public UsersSelectionViewHoleder(View itemView, UsersSelectionListAdapter adapter) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            profilePicture = itemView.findViewById(R.id.profilePicture);
            this.adapter = adapter;
        }

        public void bind(final User item, final UsersSelectionListAdapter.OnItemClickListener listener){
            setFields(item);
            itemView.setOnClickListener(v -> listener.onItemClick(item));
        }

        private void setFields(User item) {
            String fn = item.get(User.StringFields.firstName);
            String ln = item.get(User.StringFields.lastName);
            String un = fn +  " " + ln;
            username.setText(un);
            //TODO: set profile picture
        }
    }

    public UsersSelectionListAdapter(FragmentActivity fragActivity, ObservableArrayList<User> interestedPeopleList) {
        this.interestedPeople = interestedPeopleList;
        if(fragActivity != null) this.sharedViewFavor = ViewModelProviders.of(fragActivity).get(SharedViewFavor.class);

        this.fragmentActivity = fragActivity;

        this.listener = (Favor item) -> {
            Log.d(TAG,"click recorded");
            this.sharedViewFavor.select(item);
            if(item.get(Favor.StringFields.ownerID).equals(Authentication.getInstance().getUid()))
                fragmentActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FavorDetailView()).addToBackStack(null).commit();
            else
                fragmentActivity.getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new FavorDetailView()).addToBackStack(null).commit();

        };
    }

    @Override
    public UsersSelectionListAdapter.NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //set layout to itemView using Layout inflater
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
        return new UsersSelectionListAdapter.NotificationViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(NotificationViewHolder holder, int position) {
        holder.bind(notificationList.get(position));
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

}
