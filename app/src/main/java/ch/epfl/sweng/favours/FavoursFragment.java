package ch.epfl.sweng.favours;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import ch.epfl.sweng.favours.database.Favor;
import ch.epfl.sweng.favours.databinding.FavoursLayoutBinding;

public class FavoursFragment extends Fragment {
    FavoursLayoutBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Favor newFavor = new Favor();
        newFavor.set(Favor.StringFields.ownerID, FirebaseAuth.getInstance().getUid());

        binding = DataBindingUtil.inflate(inflater, R.layout.favours_layout,container,false);
        binding.setElements(this);


        binding.titleFavor.addTextChangedListener(new TextWatcherCustom() {
            @Override
            public void afterTextChanged(Editable s) {
                newFavor.set(Favor.StringFields.title,s.toString());
            }
        });

        binding.descriptionFavor.addTextChangedListener(new TextWatcherCustom() {
            @Override
            public void afterTextChanged(Editable editable) {
                newFavor.set(Favor.StringFields.description, editable.toString());
            }
        });



        binding.addFavor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (title.get().isEmpty()) {

                    CharSequence text = "You cannot create favor with an empty title!";
                    int duration = Toast.LENGTH_SHORT;
                } else if (description.get().isEmpty()) {
                    //Error cannot create favor with empty description
                } else {
                    //create new favor
                }
            }
        });

        return inflater.inflate(R.layout.favours_layout, container, false);
    }


}
