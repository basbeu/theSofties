package ch.epfl.sweng.favors.favors;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
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
import ch.epfl.sweng.favors.database.Favor;
import ch.epfl.sweng.favors.database.User;
import ch.epfl.sweng.favors.databinding.FragmentUsersSelectionBinding;
import ch.epfl.sweng.favors.databinding.UsersSelectionItemBinding;

public class UsersSelectionListAdapter extends RecyclerView.Adapter<UsersSelectionListAdapter.UsersSelectionViewHolder>{
    private static final String TAG = "SELECTION_LIST_ADAPTER";
    private ArrayList<String> interestedPeople;
    private ArrayList<String> selectedPeople;

    private Activity fragmentActivity;

    public interface OnItemClickListener {
        void onItemClick(String item);
    }

    public class UsersSelectionViewHolder extends RecyclerView.ViewHolder{
        public TextView username;
        public ImageView profilePicture;
        public UsersSelectionItemBinding binding;
        public ObservableBoolean selected;
        private String id;

        public UsersSelectionViewHolder(UsersSelectionItemBinding binding) {
            super(binding.getRoot());

            this.binding = binding;

            this.binding.selected.setOnClickListener(v -> {
                if(this.selected.get()) {
                    selectedPeople.remove(id);
                } else {
                    selectedPeople.add(id);
                }
                this.selected.set(!this.selected.get());
            });

            username = itemView.findViewById(R.id.username);
            profilePicture = itemView.findViewById(R.id.profilePicture);
            //TODO: bind the buttons to see user's profile
            //TODO: bind the button to do sth when user is selected
        }

        public void bind(final String userId, final UsersSelectionListAdapter.OnItemClickListener listener){
            this.id = userId;
            this.selected = new ObservableBoolean(selectedPeople.contains(id));
            this.binding.setElements(this);
            this.setFields(userId);
        }

        private void setFields(String id) {
            User u = new User(id);
            //this.selected = selectedPeople.contains(id);
            u.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(Observable sender, int propertyId) {
                    String fn = ((User)sender).get(User.StringFields.firstName);
                    String ln = ((User)sender).get(User.StringFields.lastName);
                    username.setText(fn + ln);
                }
            });
            //TODO: set profile picture
        }

    }

    public UsersSelectionListAdapter(FragmentActivity fragActivity, ArrayList<String> interestedPeopleList, ArrayList<String> selectedPeopleList) {
        this.interestedPeople = interestedPeopleList;
        this.selectedPeople = selectedPeopleList;
        this.fragmentActivity = fragActivity;
    }


    @Override
    public UsersSelectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        UsersSelectionItemBinding itemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.users_selection_item, parent, false);
        return new UsersSelectionViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(UsersSelectionViewHolder holder, int position) {
        holder.bind(interestedPeople.get(position), selectedListener);
    }

    @Override
    public int getItemCount() {
        return interestedPeople.size();
    }

    private OnItemClickListener selectedListener = new OnItemClickListener() {
        @Override
        public void onItemClick(String item) {
            /*if (selectedPeople.contains(item)) {
                selectedPeople.remove(item);

            } else {
                selectedPeople.add(item);
            }*/
            //TODO: add a limit of selected people
        }
    };
}
