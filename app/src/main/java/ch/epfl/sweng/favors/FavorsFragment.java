package ch.epfl.sweng.favors;

import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.database.Favor;
import ch.epfl.sweng.favors.database.FavorRequest;
import ch.epfl.sweng.favors.databinding.FavorsLayoutBinding;

public class FavorsFragment extends Fragment {
    private static final String TAG = "FAVOR_FRAGMENT";
    FavorsLayoutBinding binding;
    ObservableArrayList<Favor> favorList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final Favor newFavor = new Favor();

        binding = DataBindingUtil.inflate(inflater, R.layout.favors_layout,container,false);
        binding.setElements(this);

        binding.titleFavor.addTextChangedListener(new TextWatcherCustom() {
            @Override
            public void afterTextChanged(Editable s) { newFavor.set(Favor.StringFields.title,s.toString()); }
        });

        binding.descriptionFavor.addTextChangedListener(new TextWatcherCustom() {
            @Override
            public void afterTextChanged(Editable editable) { newFavor.set(Favor.StringFields.description, editable.toString()); }
        });

        binding.addFavor.setOnClickListener(v->{
                favorList = FavorRequest.getList(Favor.StringFields.title, newFavor.get(Favor.StringFields.title), null, null);
                favorList.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<Favor>>() {
                    @Override
                    public void onChanged(ObservableList<Favor> sender) {

                    }

                    @Override
                    public void onItemRangeChanged(ObservableList<Favor> sender, int positionStart, int itemCount) {

                    }

                    @Override
                    public void onItemRangeInserted(ObservableList<Favor> sender, int positionStart, int itemCount) {
                        Log.d(TAG ,"favor list changed");
                        for(Favor favor: favorList){
                            Log.d(TAG + " desc:", favor.get(Favor.StringFields.description));
                        }
                    }

                    @Override
                    public void onItemRangeMoved(ObservableList<Favor> sender, int fromPosition, int toPosition, int itemCount) {

                    }

                    @Override
                    public void onItemRangeRemoved(ObservableList<Favor> sender, int positionStart, int itemCount) {

                    }
                });
                if (newFavor.get(Favor.StringFields.title) == null || newFavor.get(Favor.StringFields.title).isEmpty()){
                    launchToast("Please add a title to the favor");
                } else if( newFavor.get(Favor.StringFields.description) == null || newFavor.get(Favor.StringFields.description).isEmpty()){
                    launchToast("Please add a description to the favor");
                }
                else {
                    newFavor.set(Favor.StringFields.ownerID, FirebaseAuth.getInstance().getUid());
                    newFavor.updateOnDb();
                    launchToast("Favor created successfully");
                }
        });
        return binding.getRoot();
    }

    private void launchToast(String text) {
        Toast.makeText(this.getContext(), text, Toast.LENGTH_LONG).show();
    }
}
