package ch.epfl.sweng.favors;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ch.epfl.sweng.favors.database.Favor;

public class FavorListAdapter extends RecyclerView.Adapter<FavorListAdapter.FavorViewHolder>  {
    private ObservableArrayList<Favor> favorList;
    private SharedViewFavor sharedViewFavor;
    private FragmentActivity fragmentActivity;
    private OnItemClickListener listener;
    private static final String TAG = "FAVOR_ADAPTER_LIST";

    public interface OnItemClickListener {
        void onItemClick(Favor item);
    }

    public void remove(Favor f) {
        favorList.remove(f);
        notifyDataSetChanged();
    }

    public void filter(ObservableList<Favor> favors, String query) {
        final String lowerCaseQuery = query.toLowerCase();

        for (Favor f : favors) {
            final String title = f.get(Favor.StringFields.title).toLowerCase();
            final String description = f.get(Favor.StringFields.description).toLowerCase();
            if (title.contains(lowerCaseQuery) || description.contains(lowerCaseQuery)) {
            } else {
                remove(f);
            }
        }
    }

    public class FavorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title, timestamp, location, description;
        public FavorListAdapter adapter;
        public Favor selectedFavor = null;

        public FavorViewHolder(View itemView, FavorListAdapter adapter) {
            super(itemView);
            //initialize TextViews
            title = itemView.findViewById(R.id.title);
            timestamp = itemView.findViewById(R.id.timestamp);
            location = itemView.findViewById(R.id.location);
            description = itemView.findViewById(R.id.description);
            this.adapter = adapter;
        }

        public void setFavor(Favor f) {
            this.selectedFavor = f;
        }

        @Override
        public void onClick(View v) {}

        public void bind(final Favor item, final OnItemClickListener listener){
            Favor favor = item;
            if(favor.get(Favor.StringFields.title) != null)
                title.setText(favor.get(Favor.StringFields.title));
            if(favor.get(Favor.IntegerFields.creationTimestamp) != null)
                timestamp.setText(favor.get(Favor.IntegerFields.creationTimestamp));
            if(favor.get(Favor.StringFields.description) != null)
                description.setText(favor.get(Favor.StringFields.description));
            itemView.setOnClickListener(v -> listener.onItemClick(item));
        }
    }

    //constructor
    public FavorListAdapter(FragmentActivity fragActivity, ObservableArrayList<Favor> favorList) {
        this.favorList = favorList;
        this.sharedViewFavor = ViewModelProviders.of(fragActivity).get(SharedViewFavor.class);
        this.fragmentActivity = fragActivity;
        this.listener = (Favor item) -> {
            Log.d(TAG,"click recorded");
            this.sharedViewFavor.select(item);
            fragmentActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FavorDetailView()).addToBackStack(null).commit();
        };
    }


    @Override
    public FavorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //set layout to itemView using Layout inflater
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.favor_item, parent, false);
        return new FavorViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(FavorViewHolder holder, int position) {
        holder.bind(favorList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return favorList.size();
    }

}
