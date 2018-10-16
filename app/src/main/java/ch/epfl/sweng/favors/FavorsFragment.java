package ch.epfl.sweng.favors;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.util.Log;
import android.view.View;

import android.app.DatePickerDialog;
import android.databinding.DataBindingUtil;
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
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Calendar;

import ch.epfl.sweng.favors.database.Favor;
import ch.epfl.sweng.favors.database.Interest;
import ch.epfl.sweng.favors.database.InterestRequest;
import ch.epfl.sweng.favors.databinding.FavorsLayoutBinding;

public class FavorsFragment extends Fragment {

    private static final String TAG = "FAVOR_FRAGMENT";
    private final int MIN_STRING_SIZE = 5;

    public ObservableBoolean titleValid = new ObservableBoolean(false);

    public boolean isStringValid(String s) {
        return ( s != null && s.length() > MIN_STRING_SIZE ) ;
    }

    public void createFavorIfValid(Favor newFavor) {
        if (titleValid.get()) { // && check other fields
            newFavor.set(Favor.StringFields.title, binding.titleFavor.getText().toString());
            newFavor.set(Favor.StringFields.category, binding.categoryFavor.getSelectedItem().toString());
            //set otherfields
            newFavor.set(Favor.StringFields.ownerID, FirebaseAuth.getInstance().getUid());
            newFavor.updateOnDb();
            launchToast("Favor created successfully");
        }
    }
    FavorsLayoutBinding binding;

    public ObservableField<String> favorTitle;
    public ObservableField<String> favorDescription;
    public ObservableField<String> locationCity;
    public ObservableField<String> deadline;
    public ObservableList<Interest> interestsList;

    public ObservableField<String> validationButtonText = new ObservableField<>("--");
    public ObservableField<String> fragmentTitle = new ObservableField<>("--");
    public ObservableField<String> validationText = new ObservableField<>("--");

    public final String KEY_FRAGMENT_ID = "fragment_id";
    ArrayAdapter<String> adapter = null;

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
        locationCity = newFavor.getObservableObject(Favor.StringFields.locationCity);
        deadline = newFavor.getObservableObject(Favor.StringFields.deadline);


        binding.titleFavor.addTextChangedListener(new TextWatcherCustom() {
            @Override
            public void afterTextChanged(Editable s) {
                titleValid.set(isStringValid(s.toString()));
            }
        });
        binding.descriptionFavor.addTextChangedListener(new TextWatcherCustom() {
            @Override
            public void afterTextChanged(Editable editable) {
                newFavor.set(Favor.StringFields.description, editable.toString());
            }
        });
        binding.locationFavor.addTextChangedListener(new TextWatcherCustom() {
            @Override
            public void afterTextChanged(Editable editable) {
                newFavor.set(Favor.StringFields.locationCity, editable.toString());
            }
        });

        binding.deadlineFavor.addTextChangedListener(new TextWatcherCustom() {
            @Override
            public void afterTextChanged(Editable editable) {
                newFavor.set(Favor.StringFields.deadline, editable.toString());
            }
        });

       /* binding.categoryFavor.addTextChangedListener(new TextWatcherCustom() {
            @Override
            public void afterTextChanged(Editable editable) { newFavor.set(Favor.StringFields.category, editable.toString()); }
        });*/
        binding.addFavor.setOnClickListener(v-> createFavorIfValid(newFavor));

        Spinner spinner = binding.categoryFavor;

        interestsList = InterestRequest.all(null, null);
        interestsList.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<Interest>>() {
            @Override
            public void onChanged(ObservableList sender) {

            }

            @Override
            public void onItemRangeChanged(ObservableList sender, int positionStart, int itemCount) {
            }

            @Override
            public void onItemRangeInserted(ObservableList sender, int positionStart, int itemCount) {
                if(sender != null  && !(sender.isEmpty())){
                    ArrayList interestsTitles = new ArrayList();
                    for(Interest interest : interestsList) {
                        interestsTitles.add(interest.get(Interest.StringFields.title));
                    }
                    if (adapter == null) {

                        adapter = new ArrayAdapter<String>((FavorsFragment.this).getActivity(), android.R.layout.simple_spinner_item, interestsTitles);
                        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                        spinner.setAdapter(adapter);
                    } else {

                        adapter.clear();
                        adapter.addAll(interestsTitles);
                    }
                }
            }

            @Override
            public void onItemRangeMoved(ObservableList sender, int fromPosition, int toPosition, int itemCount) {

            }

            @Override
            public void onItemRangeRemoved(ObservableList sender, int positionStart, int itemCount) {

            }
        });

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
