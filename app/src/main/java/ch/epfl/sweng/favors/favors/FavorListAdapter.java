package ch.epfl.sweng.favors.favors;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.GeoPoint;

import java.util.Date;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.database.Favor;
import ch.epfl.sweng.favors.location.LocationHandler;
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
        public TextView title, timestamp, location, description, distance;
        public ImageView iconCategory;
        public FavorListAdapter adapter;
        public Favor selectedFavor = null;

        public FavorViewHolder(View itemView, FavorListAdapter adapter) {
            super(itemView);
            //initialize TextViews
            title = itemView.findViewById(R.id.title);
            timestamp = itemView.findViewById(R.id.timestamp);
            location = itemView.findViewById(R.id.location);
            description = itemView.findViewById(R.id.description);
            distance = itemView.findViewById(R.id.distance);
            iconCategory = itemView.findViewById(R.id.iconCategory);
            this.adapter = adapter;
        }

        public void setFavor(Favor f) {
            this.selectedFavor = f;
        }

        @Override
        public void onClick(View v) {}

        public void bind(final Favor item, final OnItemClickListener listener){
            Favor favor = item;
            setTitleAndDescription(favor);
            setTimestamp(favor);
            setLocation(favor);
            setIconCategory(favor);
            itemView.setOnClickListener(v -> listener.onItemClick(item));
        }

        private void setLocation(Favor favor) {
            if(favor.get(Favor.StringFields.locationCity) != null)
                location.setText(favor.get(Favor.StringFields.locationCity));
            if(favor.get(Favor.ObjectFields.location) != null) {
                ObservableField<Object> geo = favor.getObservableObject(Favor.ObjectFields.location);
                distance.setText(LocationHandler.distanceBetween((GeoPoint)geo.get()));
            } else { distance.setText("--"); }
        }

        private void setTitleAndDescription(Favor favor) {
            if(favor.get(Favor.StringFields.title) != null)
                title.setText(favor.get(Favor.StringFields.title));
            if(favor.get(Favor.StringFields.description) != null)
                description.setText(favor.get(Favor.StringFields.description));
            setLocation(favor);
        }

        private void setTimestamp(Favor favor) {
            if(favor.get(Favor.ObjectFields.expirationTimestamp) != null) {
                Date d = (Date)favor.get(Favor.ObjectFields.expirationTimestamp);
                timestamp.setText(Utils.getFavorDate(d));
            } else { timestamp.setText("--"); }
        }

        private void setIconCategory(Favor favor){
            if(favor.get(Favor.StringFields.category) != null){
                iconCategory.setImageURI(Uri.parse("android.resource://ch.epfl.sweng.favors/drawable/"+favor.get(Favor.StringFields.category).toLowerCase().replaceAll("\\s","")));
            }
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
