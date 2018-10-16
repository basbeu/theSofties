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

public class FavorAdapter extends RecyclerView.Adapter<FavorAdapter.MyViewHolder>  {
    private ObservableArrayList<Favor> favorList;
    public Context context;
    //DatabaseReference database;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, timestamp, location, description;


        public MyViewHolder(View view) {
            super(view);
            //initialize TextViews
            title = view.findViewById(R.id.title);
            timestamp = view.findViewById(R.id.timestamp);
            location = view.findViewById(R.id.location);
            description = view.findViewById(R.id.description);
        }
    }

    //constructor
    public FavorAdapter(Context mContext, ObservableArrayList<Favor> favorList) {
        this.favorList = favorList;
        this.context = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //set layout to itemView using Layout inflater
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.favor_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final Favor favor = favorList.get(position);
        holder.title.setText(favor.get(Favor.StringFields.title));
        holder.timestamp.setText(favor.get(Favor.IntegerFields.creationTimestamp));
        holder.description.setText(favor.get(Favor.StringFields.description));
        //holder.location.setText(favor.get(Favor.StringFields.location));
    }

    @Override
    public int getItemCount() {
        return favorList.size();
    }

}
