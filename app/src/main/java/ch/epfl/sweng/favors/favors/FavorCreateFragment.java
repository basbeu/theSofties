package ch.epfl.sweng.favors.favors;

import android.app.DatePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.authentication.Authentication;
import ch.epfl.sweng.favors.database.Database;
import ch.epfl.sweng.favors.database.Favor;
import ch.epfl.sweng.favors.database.Interest;
import ch.epfl.sweng.favors.database.InterestRequest;
import ch.epfl.sweng.favors.databinding.FavorsLayoutBinding;
import ch.epfl.sweng.favors.location.Location;
import ch.epfl.sweng.favors.location.LocationHandler;
import ch.epfl.sweng.favors.main.FavorsMain;
import ch.epfl.sweng.favors.utils.DatePickerFragment;
import ch.epfl.sweng.favors.utils.ExecutionMode;
import ch.epfl.sweng.favors.utils.TextWatcherCustom;
import com.google.firebase.Timestamp;

/**
 * Favor Create Fragment
 * favors_layout.xml is the corresponding view
 */
public class FavorCreateFragment extends android.support.v4.app.Fragment {

    private static final String TAG = "FAVOR_FRAGMENT";
    private static final int MIN_STRING_SIZE = 1;

    private DatePickerFragment date = new DatePickerFragment();

    public ObservableBoolean titleValid = new ObservableBoolean(false);
    public ObservableBoolean descriptionValid = new ObservableBoolean(false);
    public ObservableBoolean locationCityValid = new ObservableBoolean(false);
    public ObservableBoolean deadlineValid = new ObservableBoolean(false);

    public static boolean isStringValid(String s) {
        return ( s != null && s.length() > MIN_STRING_SIZE ) ;
    }

    public boolean allFavorFieldsValid(){
        return (titleValid.get() && descriptionValid.get() && locationCityValid.get() && deadlineValid.get());
    }
    public void createFavorIfValid(Favor newFavor) {
        if (allFavorFieldsValid()) {
            newFavor.set(Favor.StringFields.title, binding.titleFavor.getText().toString());
            newFavor.set(Favor.StringFields.description, binding.descriptionFavor.getText().toString());
            newFavor.set(Favor.StringFields.locationCity, binding.locationFavor.getText().toString());
            newFavor.set(Favor.StringFields.category, binding.categoryFavor.getSelectedItem().toString());

            newFavor.set(Favor.ObjectFields.creationTimestamp, new Timestamp(new Date()));
            newFavor.set(Favor.ObjectFields.expirationTimestamp, date.getDate());

            newFavor.set(Favor.ObjectFields.location, LocationHandler.getHandler().locationPoint.get());
            newFavor.set(Favor.StringFields.ownerEmail, Authentication.getInstance().getEmail());
            newFavor.set(Favor.StringFields.ownerID, Authentication.getInstance().getUid());
            Log.d("Database: Favor", "Favor pushed to database");
            Database.getInstance().updateOnDb(newFavor);
            sharedViewFavor.select(newFavor);
            launchToast("Favor created successfully");
            updateUI(true);
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

    //TEST CODE FOR DETAIL FRAGMENT
    private SharedViewFavor sharedViewFavor;

    private Favor newFavor;
    private String strtext;

    private Spinner spinner;
    private ObservableList.OnListChangedCallback<ObservableList<Interest>> callbackInterestList = new ObservableList.OnListChangedCallback<ObservableList<Interest>>() {
        @Override
        public void onChanged(ObservableList sender) {}

        @Override
        public void onItemRangeChanged(ObservableList sender, int positionStart, int itemCount) {}

        @Override
        public void onItemRangeInserted(ObservableList sender, int positionStart, int itemCount) {
            if(sender != null  && !(sender.isEmpty())){
                ArrayList interestsTitles = new ArrayList();
                for(Interest interest : interestsList) {
                    interestsTitles.add(interest.get(Interest.StringFields.title));
                }
                if (adapter == null) {

                    adapter = new ArrayAdapter<String>(FavorsMain.getContext(), android.R.layout.simple_spinner_item, interestsTitles);
                    adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    spinner.setAdapter(adapter);
                } else {

                    adapter.clear();
                    adapter.addAll(interestsTitles);
                }
            }
        }

        @Override
        public void onItemRangeMoved(ObservableList sender, int fromPosition, int toPosition, int itemCount) {}

        @Override
        public void onItemRangeRemoved(ObservableList sender, int positionStart, int itemCount) {}
    };

    private TextWatcherCustom titleFavorTextWatcher = new TextWatcherCustom() {
        @Override
        public void afterTextChanged(Editable s) {
            titleValid.set(isStringValid(s.toString()));
        }
    };
    private TextWatcherCustom descriptionFavorTextWatcher = new TextWatcherCustom() {
        @Override
        public void afterTextChanged(Editable editable) {
            descriptionValid.set(isStringValid(editable.toString()));
        }
    };
    private TextWatcherCustom locationFavorTextWatcher = new TextWatcherCustom() {
        @Override
        public void afterTextChanged(Editable editable) {
            locationCityValid.set(isStringValid(editable.toString()));
        }
    };
    private TextWatcherCustom deadlineFavorTextWatcher = new TextWatcherCustom() {
        @Override
        public void afterTextChanged(Editable editable) {
            deadlineValid.set(isStringValid(editable.toString()));
        }
    };



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedViewFavor = ViewModelProviders.of(getActivity()).get(SharedViewFavor.class);

    }
    //*************************END OF TEST  *******************

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

        locationCity.set(LocationHandler.getHandler().locationCity.get());

        binding.titleFavor.addTextChangedListener(titleFavorTextWatcher);
        binding.descriptionFavor.addTextChangedListener(descriptionFavorTextWatcher);
        binding.locationFavor.addTextChangedListener(locationFavorTextWatcher);

        binding.deadlineFavor.addTextChangedListener(deadlineFavorTextWatcher);
        binding.addFavor.setOnClickListener(v-> createFavorIfValid(newFavor));

        spinner =  binding.categoryFavor;

        interestsList = InterestRequest.all(null, null);
        interestsList.addOnListChangedCallback(callbackInterestList);

        // TESTING LINE FOR BINDING
        binding.testFavorDetailButton.setOnClickListener(v->{ getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FavorDetailView()).commit();});

        return binding.getRoot();
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.deadlineFavor.setOnClickListener(v -> {
            showDatePicker();
        });
    }

    private void showDatePicker() {
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
        TextView textView = binding.deadlineFavor;
        date.setDate(dayOfMonth, monthOfYear, year);
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