package ch.epfl.sweng.favors.favors;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.database.Favor;
import ch.epfl.sweng.favors.database.FavorRequest;
import ch.epfl.sweng.favors.databinding.FragmentFavorsBinding;

// FIXME what does this class do
public class FavorsFragment extends android.support.v4.app.Fragment implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "FAVOR_FRAGMENT";

    FragmentFavorsBinding binding;
    ObservableArrayList<Favor> favorList;
    FavorListAdapter listAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favors,container,false);
        binding.setElements(this);

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

        return binding.getRoot();
    }

    /**
     *
     * Sorts the favors list according to sort criteria
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                favorList = FavorRequest.all(null, null);
            case 1: //location
                favorList = FavorRequest.all(null, Favor.StringFields.locationCity);
                break;
            case 2: //recent
                favorList = FavorRequest.all(null, null);
                break;
            case 3: //category
                favorList = FavorRequest.all(null, Favor.StringFields.category);
                break;
            default: break;
        }
        favorList.addOnListChangedCallback(listCallBack);
    }

    /**
     *
     * Takes the favors list from the data base
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    private void updateList(ObservableList<Favor> list){
        listAdapter = new FavorListAdapter(this.getActivity(), (ObservableArrayList)list);
        binding.favorsList.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
    }

    ObservableList.OnListChangedCallback listCallBack = new ObservableList.OnListChangedCallback<ObservableList<Favor>>() {
        @Override
        public void onChanged(ObservableList<Favor> sender) {}

        @Override
        public void onItemRangeChanged(ObservableList<Favor> sender, int positionStart, int itemCount) {}

        @Override
        public void onItemRangeInserted(ObservableList<Favor> sender, int positionStart, int itemCount) {
            updateList(sender);
        }

        @Override
        public void onItemRangeMoved(ObservableList<Favor> sender, int fromPosition, int toPosition, int itemCount) {}

        @Override
        public void onItemRangeRemoved(ObservableList<Favor> sender, int positionStart, int itemCount) {}
    };
}