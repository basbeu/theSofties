package ch.epfl.sweng.favors.main;

import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.firestore.GeoPoint;

import java.util.Collections;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.authentication.Authentication;
import ch.epfl.sweng.favors.database.Favor;
import ch.epfl.sweng.favors.database.FavorRequest;
import ch.epfl.sweng.favors.database.ObservableArrayList;
import ch.epfl.sweng.favors.database.User;
import ch.epfl.sweng.favors.databinding.HomeBinding;
import ch.epfl.sweng.favors.favors.FavorListAdapter;
import ch.epfl.sweng.favors.favors.FavorsFragment;
import ch.epfl.sweng.favors.favors.FavorsList;
import ch.epfl.sweng.favors.favors.FavorsMap;
import ch.epfl.sweng.favors.location.Location;
import ch.epfl.sweng.favors.location.LocationHandler;
import ch.epfl.sweng.favors.location.SortLocations;

public class Home extends android.support.v4.app.Fragment  {
    private ObservableArrayList<Favor> favorList = new ObservableArrayList<>();
    FavorListAdapter listAdapter;
    HomeBinding binding;

    Observable.OnPropertyChangedCallback locationSortingCb = new Observable.OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable sender, int propertyId) {
            if(LocationHandler.getHandler().locationPoint.get() != null) Collections.sort((ObservableArrayList) sender, new SortLocations(LocationHandler.getHandler().locationPoint.get()));
            updateList((ObservableArrayList) sender);
        }
    };
    Observable.OnPropertyChangedCallback otherSortingCb = new Observable.OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable sender, int propertyId) {
            updateList((ObservableArrayList) sender);
        }
    };


    public ObservableField<String> userName = User.getMain().getObservableObject(User.StringFields.firstName);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.home, container,false);
        binding.setElements(this);

        binding.button.setOnClickListener(v ->
                this.getFragmentManager().beginTransaction().replace(R.id.fragment_container, new FavorsFragment()).addToBackStack(null).commit());

        setView();
        binding.switchList.setOnClickListener(v -> {currentMode++;setView();});
        binding.favorsList.setLayoutManager(new LinearLayoutManager(getContext()));

        return binding.getRoot();
    }


    private void updateList(ObservableArrayList<Favor> list){
        listAdapter = new FavorListAdapter(this.getActivity(), (ObservableArrayList)list);
        binding.favorsList.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
    }

    static String[] modes = {"Discover favors in your area...", "Recent favors..."};
    int currentMode = 0;
    public ObservableField<String> lastFavorsTitle = new ObservableField<>();

    void setView(){
        if(currentMode >= modes.length){currentMode=0;};
        switch (currentMode){
            case 0 :
                favorList.changeOnPropertyChangedCallback(locationSortingCb);
                FavorRequest.all(favorList, null, null);
                break;
            case 1 :
                favorList.changeOnPropertyChangedCallback(otherSortingCb);
                FavorRequest.all(favorList, 5, Favor.ObjectFields.creationTimestamp);
                break;
            default:

        }
        lastFavorsTitle.set(modes[currentMode]);
    }
}
