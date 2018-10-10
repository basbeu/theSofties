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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import ch.epfl.sweng.favours.database.Favor;
import ch.epfl.sweng.favours.databinding.FavoursLayoutBinding;

public class FavoursFragment extends Fragment {
    private static final String TAG = "FAVOR_FRAGMENT";
    FavoursLayoutBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final Favor newFavor = new Favor();

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
                if (newFavor.get(Favor.StringFields.title).isEmpty() || newFavor.get(Favor.StringFields.description).isEmpty()) {
                    Log.d(TAG, "Failure to check favor input");
                    launchToast("There is an empty field left");
                } else {
                    //int timestamp = (int)System.currentTimeMillis() /1000;
                    Log.d(TAG, "Succes to check favor input");
                    newFavor.set(Favor.StringFields.ownerID, FirebaseAuth.getInstance().getUid());
                    newFavor.updateOnDb();
                }
            }
        });

        return binding.getRoot();
    }

    private void launchToast(String text) {
        Toast.makeText(this.getContext(), text, Toast.LENGTH_LONG).show();
    }
}
