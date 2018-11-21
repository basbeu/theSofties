package ch.epfl.sweng.favors.favors;

import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;

import java.util.Collections;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.database.Favor;
import ch.epfl.sweng.favors.database.FavorRequest;
import ch.epfl.sweng.favors.database.ObservableArrayList;
import ch.epfl.sweng.favors.databinding.FavorsListBinding;
import ch.epfl.sweng.favors.location.LocationHandler;
import ch.epfl.sweng.favors.location.SortLocations;

/**
 * Fragment that displays the list of favor and allows User to sort it and to search in it
 */
public class FavorsList extends android.support.v4.app.Fragment implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "FAVORS_FRAGMENT";

    FavorsListBinding binding;
    ObservableArrayList<Favor> favorList = new ObservableArrayList<>();
    FavorListAdapter listAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.favors_list,container,false);
        binding.setElements(this);

        //Spinner for sorting criteria
        Spinner sortBySpinner = binding.sortBySpinner;

        //create Spinner Adapter from resource list
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(), R.array.sortBy, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortBySpinner.setAdapter(adapter);
        sortBySpinner.setOnItemSelectedListener(this);

        //define input behaviour for SearchView
        SearchView searchFavor = binding.searchFavor;
        searchFavor.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                listAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                listAdapter.getFilter().filter(query);
                return false;
            }
        });

        //button redirects to creating favor page
        binding.addNewFavor.setOnClickListener(v -> getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FavorCreateFragment()).commit());

        binding.favorsList.setLayoutManager(new LinearLayoutManager(getContext()));

        return binding.getRoot();
    }


    Observable.OnPropertyChangedCallback locationSortingCb = new Observable.OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable sender, int propertyId) {
            Collections.sort((ObservableArrayList) sender, new SortLocations(LocationHandler.getHandler().locationPoint.get()));
            updateList((ObservableArrayList) sender);
        }
    };
    Observable.OnPropertyChangedCallback otherSortingCb = new Observable.OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable sender, int propertyId) {
            updateList((ObservableArrayList) sender);
        }
    };
    /**
     *
     * Sorts the favors list according to sort criteria
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        favorList.changeOnPropertyChangedCallback(otherSortingCb);
        switch (position) {
            case 0:
                FavorRequest.all(favorList, null, null);
            case 1: //location
                favorList.changeOnPropertyChangedCallback(locationSortingCb);
                FavorRequest.all(favorList, null, null);
                break;
            case 2: //recent
                FavorRequest.all(favorList, null, null);
                break;
            case 3: //category
                FavorRequest.all(favorList, null, Favor.StringFields.category);
                break;
            default: break;
        }
    }

    /**
     *
     * Takes the favors list from the data base
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    private void updateList(ObservableArrayList<Favor> list){
        listAdapter = new FavorListAdapter(this.getActivity(), (ObservableArrayList)list);
        binding.favorsList.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
    }

    Observable.OnPropertyChangedCallback listCallBack = new Observable.OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable sender, int propertyId) {
            updateList((ObservableArrayList<Favor>) sender);
        }
    };
}