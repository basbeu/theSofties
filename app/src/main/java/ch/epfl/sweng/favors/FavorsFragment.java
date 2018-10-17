package ch.epfl.sweng.favors;

import android.databinding.ObservableBoolean;
import android.view.View;
import android.app.DatePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
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


public class FavorsFragment extends android.support.v4.app.Fragment {

    private static final String TAG = "FAVOR_FRAGMENT";
    private final int MIN_STRING_SIZE = 1;

    public ObservableBoolean titleValid = new ObservableBoolean(false);
    public ObservableBoolean descriptionValid = new ObservableBoolean(false);
    public ObservableBoolean locationCityValid = new ObservableBoolean(false);
    public ObservableBoolean deadlineValid = new ObservableBoolean(false);


    public boolean isStringValid(String s) {
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

            newFavor.set(Favor.StringFields.ownerID, FirebaseAuth.getInstance().getUid());
            newFavor.updateOnDb();
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
                descriptionValid.set(isStringValid(editable.toString()));
            }
        });
        binding.locationFavor.addTextChangedListener(new TextWatcherCustom() {
            @Override
            public void afterTextChanged(Editable editable) {
                locationCityValid.set(isStringValid(editable.toString()));
            }
        });

        binding.deadlineFavor.addTextChangedListener(new TextWatcherCustom() {
            @Override
            public void afterTextChanged(Editable editable) {
                deadlineValid.set(isStringValid(editable.toString()));
            }
        });

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

        // TESTING LINE FOR BINDING
        binding.testFavorDetailButton.setOnClickListener(v->{ getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FavorDetailView()).commit();
        });

        return binding.getRoot();
    }
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.deadlineFavor.setOnClickListener(new View.OnClickListener() {
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
        TextView textView = binding.deadlineFavor;
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
