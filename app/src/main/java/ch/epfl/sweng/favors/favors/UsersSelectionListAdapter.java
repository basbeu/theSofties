package ch.epfl.sweng.favors.favors;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.database.Favor;
import ch.epfl.sweng.favors.database.User;
import ch.epfl.sweng.favors.databinding.FragmentUsersSelectionBinding;
import ch.epfl.sweng.favors.databinding.UsersSelectionItemBinding;

public class UsersSelectionListAdapter extends RecyclerView.Adapter<UsersSelectionListAdapter.UsersSelectionViewHolder>{
    private static final String TAG = "SELECTION_LIST_ADAPTER";
    ArrayList<User>  interestedPeople;
    private ArrayList<String> selectedPeople;

    private FragmentActivity fragmentActivity;
    private Long maxNumber;


    public interface OnItemClickListener {
        void onItemClick(String item);
    }

    public class UsersSelectionViewHolder extends RecyclerView.ViewHolder{
        public ImageView profilePicture;
        public UsersSelectionItemBinding binding;
        public ObservableBoolean selected;
        private User user;
        private FragmentActivity activity;

        public UsersSelectionViewHolder(UsersSelectionItemBinding binding, FragmentActivity activity, Long maxNumber) {
            super(binding.getRoot());

            this.binding = binding;
            this.activity = activity;

            this.binding.selected.setOnClickListener(v -> {
                if(this.selected.get()) {
                    selectedPeople.remove(user.getId());
                } else {
                    if(selectedPeople.size() > maxNumber){
                        Toast.makeText(activity.getApplicationContext(), "You can't select more people", Toast.LENGTH_LONG).show();
                    }else{
                        selectedPeople.add(user.getId());
                    }
                }
                this.selected.set(!this.selected.get());
            });

            //TODO: bind the buttons to see user's profile
            //TODO: bind the button to do sth when user is selected
        }

        public void bind(final User user, final UsersSelectionListAdapter.OnItemClickListener listener){
            this.user = user;
            this.selected = new ObservableBoolean(selectedPeople.contains(user.getId()));
            this.binding.setElements(this);
            this.setFields(user);
            binding.profileViewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FavorPosterDetailView mFrag = new FavorPosterDetailView();
                    mFrag.setUser(user);
                    activity.getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                            mFrag).addToBackStack("interested").commit();
                }
            });
        }

        private void setFields(User user) {
            binding.username.setText(user.get(User.StringFields.firstName) + user.get(User.StringFields.lastName));
            //TODO: set profile picture
        }

    }

    public UsersSelectionListAdapter(FragmentActivity fragActivity, ArrayList<User> interestedPeopleList, ArrayList<String> selectedPeopleList, Long maxNumber) {
        this.interestedPeople = interestedPeopleList;
        this.selectedPeople = selectedPeopleList;
        this.fragmentActivity = fragActivity;
        this.maxNumber = maxNumber;
    }


    @Override
    public UsersSelectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        UsersSelectionItemBinding itemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.users_selection_item, parent, false);
        return new UsersSelectionViewHolder(itemBinding, fragmentActivity, maxNumber);
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
