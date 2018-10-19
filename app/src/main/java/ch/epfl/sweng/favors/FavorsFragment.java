package ch.epfl.sweng.favors;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;

import ch.epfl.sweng.favors.database.Favor;
import ch.epfl.sweng.favors.database.FavorRequest;
import ch.epfl.sweng.favors.databinding.FragmentFavorsBinding;


public class FavorsFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "FAVOR_FRAGMENT";

    FragmentFavorsBinding binding;
    ObservableArrayList<Favor> favorList;
    FavorListAdapter listAdapter;

    //RecyclerView favorsListView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favors,container,false);
        binding.setElements(this);

        //TODO: add text "sort by" to Spinner

        //Spinner for sorting criteria
        Spinner sortBySpinner = binding.sortBySpinner;

        //create Spinner Adapter from resource list
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(), R.array.sortBy, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortBySpinner.setAdapter(adapter);
        sortBySpinner.setOnItemSelectedListener(this);

        //button redirects to creating favor page
        binding.addNewFavor.setOnClickListener(v -> getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FavorCreateFragment()).commit());

        binding.favorsList.setLayoutManager(new LinearLayoutManager(getContext()));
        favorList = FavorRequest.all(null, null);
        favorList.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<Favor>>() {

            @Override
            public void onChanged(ObservableList<Favor> sender) {

            }

            @Override
            public void onItemRangeChanged(ObservableList<Favor> sender, int positionStart, int itemCount) {
            }

            @Override
            public void onItemRangeInserted(ObservableList<Favor> sender, int positionStart, int itemCount) {
                favorList.clear();
                for (Favor fav : sender) {
                    favorList.add(fav);
                }
                listAdapter = new FavorListAdapter(getContext(), favorList);
                binding.favorsList.setAdapter(listAdapter);
                listAdapter.notifyDataSetChanged();
                Log.d(TAG, "list charged " + favorList.size());
            }

            @Override
            public void onItemRangeMoved(ObservableList<Favor> sender, int fromPosition, int toPosition, int itemCount) {
            }

            @Override
            public void onItemRangeRemoved(ObservableList<Favor> sender, int positionStart, int itemCount) {
            }
        });

        binding.searchFavor.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ObservableArrayList<Favor> filteredList = filter(favorList, newText);
                listAdapter = new FavorListAdapter(getContext(), filteredList);
                binding.favorsList.setAdapter(listAdapter);
                listAdapter.notifyDataSetChanged();
                Log.d(TAG, "filtered list with size " + filteredList.size());
                return false;
            }
        });

        return binding.getRoot();
    }

    private ObservableArrayList<Favor> filter(ObservableList<Favor> favors, String query) {
        final String lowerCaseQuery = query.toLowerCase();

        final ObservableList<Favor> filteredModelList = new ObservableArrayList<>();
        for (Favor f : favors) {
            final String title = f.get(Favor.StringFields.title).toLowerCase();
            if (title.contains(lowerCaseQuery)) {
                filteredModelList.add(f);
            }
        }
        return (ObservableArrayList)filteredModelList;
    }

    /**
     *
     * Sorts the favors list according to sort criteria
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
            case 1: //sort list by location
                Log.d(TAG, "item " + position + " from the spinner is selected");
                //favorList = FavorRequest.all(null, Favor.ObjectFields.location);
                break;
            case 2: //sort list by recent
                Log.d(TAG, "item " + position + " from the spinner is selected");
                //favorList = FavorRequest.all(null, Favor.IntegerFields.creationTimestamp);
                break;
            case 3: //TODO: sort by category
                Log.d(TAG, "item " + position + " from the spinner is selected");
                //favorList = FavorRequest.all(null, Favor.StringFields.category);
                break;
            default: break;
        }
    }

    /**
     *
     * Takes the favors list from the data base
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        favorList = FavorRequest.all(null, null);
        Log.d(TAG, "no item from the spinner is selected");
    }
}
