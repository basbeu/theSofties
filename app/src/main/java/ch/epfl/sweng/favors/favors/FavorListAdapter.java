package ch.epfl.sweng.favors.favors;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.ObservableArrayList;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.database.Favor;
import ch.epfl.sweng.favors.utils.ExecutionMode;
import ch.epfl.sweng.favors.utils.Utils;

public class FavorListAdapter extends RecyclerView.Adapter<FavorListAdapter.FavorViewHolder>  {
    private ObservableArrayList<Favor> favorList;
    private SharedViewFavor sharedViewFavor;
    private FragmentActivity fragmentActivity;
    private OnItemClickListener listener;
    private static final String TAG = "FAVOR_ADAPTER_LIST";

    public interface OnItemClickListener {
        void onItemClick(Favor item);
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
            if(favor.get(Favor.ObjectFields.creationTimestamp) != null) {
                Date d = (Date)favor.get(Favor.ObjectFields.creationTimestamp);
                if (d != null) {
                    timestamp.setText(Utils.getFavorDate(d));
                } else {
                    timestamp.setText("--");
                }
            } else {
                timestamp.setText("18.Oct");
            }
            if(favor.get(Favor.StringFields.description) != null)
                description.setText(favor.get(Favor.StringFields.locationCity));
            if(favor.get(Favor.StringFields.locationCity) != null)
                location.setText(favor.get(Favor.StringFields.description));
//            if(favor.get(Favor.ObjectFields.location) != null)
//                location.setText(favor.get(Favor.ObjectFields.location).toString());
            itemView.setOnClickListener(v -> listener.onItemClick(item));
        }
    }

    //constructor
    public FavorListAdapter(FragmentActivity fragActivity, ObservableArrayList<Favor> favorList) {
        this.favorList = favorList;
        if(!ExecutionMode.getInstance().isTest()){
            this.sharedViewFavor = ViewModelProviders.of(fragActivity).get(SharedViewFavor.class);
        }

        this.fragmentActivity = fragActivity;
        if(!ExecutionMode.getInstance().isTest()){
            this.listener = (Favor item) -> {
                Log.d(TAG,"click recorded");
                this.sharedViewFavor.select(item);
                fragmentActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FavorDetailView()).addToBackStack(null).commit();
            };


        }

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
