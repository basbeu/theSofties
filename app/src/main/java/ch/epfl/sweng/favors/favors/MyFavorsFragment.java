package ch.epfl.sweng.favors.favors;

import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.authentication.Authentication;
import ch.epfl.sweng.favors.database.Favor;
import ch.epfl.sweng.favors.database.FavorRequest;
import ch.epfl.sweng.favors.database.ObservableArrayList;
import ch.epfl.sweng.favors.databinding.MyFavorsBinding;

/**
 * Fragment that displays the list of favor and allows User to sort it and to search in it
 */
public class MyFavorsFragment extends android.support.v4.app.Fragment {
    private static final String TAG = "FAVORS_LIST";

    MyFavorsBinding binding;
    ObservableArrayList<Favor> favorList = new ObservableArrayList<>();
    FavorListAdapter listAdapter;

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.my_favors,container,false);
        binding.setElements(this);

        binding.addNewFavor.setOnClickListener(v -> getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FavorCreateFragment()).addToBackStack("newFavor").commit());

        binding.favorsList.setLayoutManager(new LinearLayoutManager(getContext()));

        FavorRequest.getList(favorList, Favor.StringFields.ownerID, Authentication.getInstance().getUid(), null, null);
        favorList.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                updateList((ObservableArrayList<Favor>) sender);
            }
        });

        return binding.getRoot();
    }

    private void updateList(ObservableArrayList<Favor> list){
        if(this.getActivity() == null) return; // Callback
        listAdapter = new FavorListAdapter(this.getActivity(), list);
        binding.favorsList.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
    }
}