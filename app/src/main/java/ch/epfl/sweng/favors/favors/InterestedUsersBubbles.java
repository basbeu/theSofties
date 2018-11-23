package ch.epfl.sweng.favors.favors;

import com.igalata.bubblepicker.BubblePickerListener;
import com.igalata.bubblepicker.adapter.BubblePickerAdapter;
import com.igalata.bubblepicker.model.BubbleGradient;
import com.igalata.bubblepicker.rendering.BubblePicker;

import ch.epfl.sweng.favors.database.Database;
import ch.epfl.sweng.favors.database.Favor;
import ch.epfl.sweng.favors.database.User;
import ch.epfl.sweng.favors.databinding.BubblesBinding;

import android.arch.lifecycle.ViewModelProviders;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.igalata.bubblepicker.model.PickerItem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import ch.epfl.sweng.favors.R;

public class InterestedUsersBubbles extends android.support.v4.app.Fragment {
    private static final String TAG = "BUBBLES_FRAGMENT";

    BubblesBinding binding;
    BubblePicker picker;
    ObservableArrayList<String> userNames;

    private String[] titles;
    private TypedArray colors;
//    final TypedArray images = getResources().obtainTypedArray(R.array.images);
    private ObservableArrayList<String> interestedPeople;
    private Favor localFavor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        titles = getResources().getStringArray(R.array.countries);
        colors = getResources().obtainTypedArray(R.array.colors);
        localFavor = new Favor(getArguments().getString(FavorCreateFragment.KEY_FRAGMENT_ID));
        interestedPeople = new ObservableArrayList<>();

        Database.getInstance().updateFromDb(localFavor).addOnCompleteListener(t->{
            Log.d("interestedPeople ID", getArguments().getString(FavorCreateFragment.KEY_FRAGMENT_ID));
            Log.d("interestedPeople ID", localFavor.get(Favor.StringFields.title));

            interestedPeople.addAll((ArrayList<String>) localFavor.get(Favor.ObjectFields.interested));
            if(interestedPeople != null) {
                Log.d("interestedPeople", interestedPeople.toString());
            } else {
                Log.d("interestedPeople", "nulllllll");
            }
        });
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.bubbles,container,false);
        binding.setElements(this);

        picker = binding.picker;

        picker.setAdapter( new BubblePickerAdapter() {
            @Override
            public int getTotalCount() {
//                return interestedPeople.size();
                return titles.length;
            }

            @NotNull
            @Override
            public PickerItem getItem(int position) {
                PickerItem item = new PickerItem();

//                Database.getInstance().updateFromDb(new User()).addOnCompleteListener(t->{
                Log.d("interestedPeopleALLGOOD", interestedPeople.toString());
//                item.setTitle(interestedPeople.get(position));
                item.setTitle(titles[position]);
                item.setGradient(new BubbleGradient(colors.getColor((position * 2) % 8, 0),
                        colors.getColor((position * 2) % 8 + 1, 0), BubbleGradient.VERTICAL));
//                });
//                        item.setTypeface(Typeface.BOLD);
                item.setTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
//                item.setBackgroundImage(ContextCompat.getDrawable(getContext(), images.getResourceId(position, 0)));
                return item;
            }
        });

        picker.setListener(new BubblePickerListener() {
            @Override
            public void onBubbleSelected(@NotNull PickerItem item) {

            }

            @Override
            public void onBubbleDeselected(@NotNull PickerItem item) {

            }
        });

        return binding.getRoot();
    }

}
