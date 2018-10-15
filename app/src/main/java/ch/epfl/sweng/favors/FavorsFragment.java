package ch.epfl.sweng.favors;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.view.View;
import android.app.DialogFragment;
import android.app.DatePickerDialog;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import java.util.Calendar;

import ch.epfl.sweng.favors.database.Favor;
import ch.epfl.sweng.favors.databinding.FavorsLayoutBinding;

public class FavorsFragment extends Fragment {
    private static final String TAG = "FAVOR_FRAGMENT";


    FavorsLayoutBinding binding;

    public void createFavorIfValid(Favor newFavor) {
        if (newFavor.get(Favor.StringFields.title) == null || newFavor.get(Favor.StringFields.title).isEmpty()){
            launchToast("Please add a title to the favor");
        } else if( newFavor.get(Favor.StringFields.description) == null || newFavor.get(Favor.StringFields.description).isEmpty()){
            launchToast("Please add a description to the favor");
        } else if( newFavor.get(Favor.StringFields.location) == null || newFavor.get(Favor.StringFields.location).isEmpty()){
            launchToast("Please add a location to the favor");
        } else if( newFavor.get(Favor.StringFields.deadline) == null || newFavor.get(Favor.StringFields.location).isEmpty()){
            launchToast("Please add a location to the favor");
        }
        else {
            newFavor.set(Favor.StringFields.ownerID, FirebaseAuth.getInstance().getUid());
            newFavor.updateOnDb();
            launchToast("Favor created successfully");
        }
    }
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.favors_layout, container, false);
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
        binding.locationFavor.addTextChangedListener(new TextWatcherCustom() {
            @Override
            public void afterTextChanged(Editable editable) { newFavor.set(Favor.StringFields.location, editable.toString()); }
        });

        binding.deadlineFavor.addTextChangedListener(new TextWatcherCustom() {
            @Override
            public void afterTextChanged(Editable editable) { newFavor.set(Favor.StringFields.deadline, editable.toString()); }
        });

        binding.addFavor.setOnClickListener(v-> createFavorIfValid(newFavor));

        return binding.getRoot();
    }
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.deadlineFavor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDatePicker();
            }
        });
    }

    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(ondate);
        date.show(getFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate = (view, year, monthOfYear, dayOfMonth) -> {
        TextView textView = (TextView) getView().findViewById(R.id.deadlineFavor);
        textView.setText(String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear+1)
                + "-" + String.valueOf(year));

    };
    private void launchToast(String text) {
        Toast.makeText(this.getContext(), text, Toast.LENGTH_LONG).show();
    }

}
