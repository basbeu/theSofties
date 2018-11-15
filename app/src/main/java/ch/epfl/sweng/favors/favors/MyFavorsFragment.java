package ch.epfl.sweng.favors.favors;

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
import ch.epfl.sweng.favors.authentication.Authentication;
import ch.epfl.sweng.favors.database.Favor;
import ch.epfl.sweng.favors.database.FavorRequest;
import ch.epfl.sweng.favors.databinding.FragmentFavorsBinding;

/**
 * Fragment that displays the list of favor and allows User to sort it and to search in it
 */
public class MyFavorsFragment extends android.support.v4.app.Fragment {
    private static final String TAG = "FAVOR_FRAGMENT";

    FragmentFavorsBinding binding;
    ObservableArrayList<Favor> favorList;
    FavorListAdapter listAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.my_favors,container,false);
        binding.setElements(this);

        binding.addNewFavor.setOnClickListener(v -> getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FavorCreateFragment()).commit());

        binding.favorsList.setLayoutManager(new LinearLayoutManager(getContext()));
        favorList = FavorRequest.getList(Favor.StringFields.ownerID, Authentication.getInstance().getUid(), null, null);
        favorList.addOnListChangedCallback(listCallBack);

        return binding.getRoot();
    }

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