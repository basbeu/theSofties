package ch.epfl.sweng.favors;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.database.Favor;
import ch.epfl.sweng.favors.databinding.FavorsLayoutBinding;

public class FavorsFragment extends android.support.v4.app.Fragment {
    private static final String TAG = "FAVOR_FRAGMENT";
    FavorsLayoutBinding binding;

    //TEST CODE FOR DETAIL FRAGMENT
    private SharedViewFavor sharedViewFavor;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedViewFavor = ViewModelProviders.of(getActivity()).get(SharedViewFavor.class);
    }
    //*************************END OF TEST  *******************

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
                if (newFavor.get(Favor.StringFields.title) == null || newFavor.get(Favor.StringFields.title).isEmpty()){
                    launchToast("Please add a title to the favor");
                } else if( newFavor.get(Favor.StringFields.description) == null || newFavor.get(Favor.StringFields.description).isEmpty()){
                    launchToast("Please add a description to the favor");
                }
                else {
                    newFavor.set(Favor.StringFields.ownerID, FirebaseAuth.getInstance().getUid());
                    newFavor.updateOnDb();
                    launchToast("Favor created successfully");

                    //TEST ***********setting the favor that will be used to display details
                    sharedViewFavor.select(newFavor);
                }
        });

        // TESTING LINE FOR BINDING
        binding.testFavorDetailButton.setOnClickListener(v->{ getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FavorDetailView()).commit();
        });

        return binding.getRoot();
    }

    private void launchToast(String text) {
        Toast.makeText(this.getContext(), text, Toast.LENGTH_LONG).show();
    }
}
