package ch.epfl.sweng.favors.favors;

import com.igalata.bubblepicker.BubblePickerListener;
import com.igalata.bubblepicker.adapter.BubblePickerAdapter;
import com.igalata.bubblepicker.model.BubbleGradient;
import com.igalata.bubblepicker.rendering.BubblePicker;

import ch.epfl.sweng.favors.database.Favor;
import ch.epfl.sweng.favors.databinding.BubblesBinding;

import android.arch.lifecycle.ViewModelProviders;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.igalata.bubblepicker.model.PickerItem;

import org.jetbrains.annotations.NotNull;

import ch.epfl.sweng.favors.R;
public class InterestedUsersBubbles extends android.support.v4.app.Fragment {
    private static final String TAG = "BUBBLES_FRAGMENT";

    BubblesBinding binding;
    BubblePicker picker;
    ObservableArrayList<String> userNames;

    String[] titles;
    TypedArray colors;
//    final TypedArray images = getResources().obtainTypedArray(R.array.images);

    @Override
    public void onCreate(Bundle savedInstanceState) {
         titles = getResources().getStringArray(R.array.countries);
         colors = getResources().obtainTypedArray(R.array.colors);
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
                return titles.length;
            }

            @NotNull
            @Override
            public PickerItem getItem(int position) {
                PickerItem item = new PickerItem();
                item.setTitle(titles[position]);
                item.setGradient(new BubbleGradient(colors.getColor((position * 2) % 8, 0),
                        colors.getColor((position * 2) % 8 + 1, 0), BubbleGradient.VERTICAL));
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
