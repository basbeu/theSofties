package ch.epfl.sweng.favors;

import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import ch.epfl.sweng.favors.database.Favor;

import ch.epfl.sweng.favors.database.FavorRequest;

import static android.support.v4.content.ContextCompat.startActivity;

public class FavorListAdapter extends RecyclerView.Adapter<FavorListAdapter.FavorViewHolder>  {
    private ObservableArrayList<Favor> favorList;
    public Context context;
    private SharedViewFavor sharedViewFavor;


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
        public void onClick(View v) {
            sharedViewFavor.select(this.selectedFavor);
            Intent intent = new Intent(v.getContext(), FavorDetailView.class);
            context.startActivity(intent);
        }
    }

    //constructor
    public FavorListAdapter(Context context, ObservableArrayList<Favor> favorList) {
        this.favorList = favorList;
        this.context = context;
    }

    @Override
    public FavorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //set layout to itemView using Layout inflater
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.favor_item, parent, false);
        return new FavorViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(FavorViewHolder holder, int position) {
        Favor favor = favorList.get(position);
        holder.setFavor(favor);
        if(favor.get(Favor.StringFields.title) != null)
            holder.title.setText(favor.get(Favor.StringFields.title));
        if(favor.get(Favor.IntegerFields.creationTimestamp) != null)
            holder.timestamp.setText(favor.get(Favor.IntegerFields.creationTimestamp));
        if(favor.get(Favor.StringFields.description) != null)
            holder.description.setText(favor.get(Favor.StringFields.description));
    }

    @Override
    public int getItemCount() {
        return favorList.size();
    }

}
