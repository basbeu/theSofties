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

    public ObservableField<String> favorTitle;
    public ObservableField<String> favorDescription;

    public ObservableField<String> validationButtonText = new ObservableField<>("--");
    public ObservableField<String> fragmentTitle = new ObservableField<>("--");
    public ObservableField<String> validationText = new ObservableField<>("--");

    public final String KEY_FRAGMENT_ID = "fragment_id";

    /**
     * Load a fragment with a view to edit a favor of the database or to add a new one
     * While creating the favor fragment, please indicate the favor ID of the favor you want to edit
     * with the KEY_FRAGMENT_ID key and it'll be loaded directly form the server and updated when
     * the validation button is clicked
     *
     * @param inflater Managed by the system
     * @param container Managed by the system
     * @param savedInstanceState Managed by the system
     * @return The view
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.favors_layout,container,false);
        binding.setElements(this);

        Favor newFavor;
        String strtext;
        if(getArguments() != null && (strtext = getArguments().getString(KEY_FRAGMENT_ID)) != null) {
            newFavor = new Favor(strtext);
            updateUI(true);
        }
        else{
            newFavor = new Favor();
            updateUI(false);
        }

        favorTitle = newFavor.getObservableObject(Favor.StringFields.title);
        favorDescription = newFavor.getObservableObject(Favor.StringFields.description);

        binding.titleFavor.addTextChangedListener(new TextWatcherCustom() {
            @Override
            public void afterTextChanged(Editable s) { newFavor.set(Favor.StringFields.title,s.toString()); }
        });

        binding.descriptionFavor.addTextChangedListener(new TextWatcherCustom() {
            @Override
            public void afterTextChanged(Editable editable) { newFavor.set(Favor.StringFields.description, editable.toString()); }
        });

        binding.addFavor.setOnClickListener(v->{
                favorList = FavorRequest.getList(Favor.StringFields.title, "test", null, null);
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

                    launchToast(validationText.get());

                    updateUI(true);
                }
        });
        return binding.getRoot();
    }

    private void updateUI(boolean isEditing){
        if(isEditing){
            validationButtonText.set("Edit the favor");
            fragmentTitle.set("Edit an existing favor");
            validationText.set("Favor edited successfully");
        }
        else{
            validationButtonText.set("Create the favor");
            fragmentTitle.set("Create a new favor");
            validationText.set("Favor created successfully");
        }
    }

    private void launchToast(String text) {
        Toast.makeText(this.getContext(), text, Toast.LENGTH_LONG).show();
    }
}
