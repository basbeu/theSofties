package ch.epfl.sweng.favors.main;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.authentication.Authentication;
import ch.epfl.sweng.favors.database.Favor;
import ch.epfl.sweng.favors.database.FavorRequest;
import ch.epfl.sweng.favors.database.User;
import ch.epfl.sweng.favors.databinding.HomeBinding;
import ch.epfl.sweng.favors.favors.FavorDetailView;
import ch.epfl.sweng.favors.favors.FavorListAdapter;
import ch.epfl.sweng.favors.favors.FavorsFragment;

public class Home extends android.support.v4.app.Fragment  {
    private ObservableArrayList<Favor> favorList;
    FavorListAdapter listAdapter;
    HomeBinding binding;

    public ObservableField<String> userName = User.getMain().getObservableObject(User.StringFields.firstName);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.home, container,false);
        binding.setElements(this);

        binding.button.setOnClickListener(v ->
                this.getFragmentManager().beginTransaction().replace(R.id.fragment_container, new FavorsFragment()).addToBackStack(null).commit());

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
