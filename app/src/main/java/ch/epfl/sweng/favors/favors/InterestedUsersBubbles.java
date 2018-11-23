package ch.epfl.sweng.favors.favors;

import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.igalata.bubblepicker.BubblePickerListener;
import com.igalata.bubblepicker.adapter.BubblePickerAdapter;
import com.igalata.bubblepicker.model.BubbleGradient;
import com.igalata.bubblepicker.model.PickerItem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.authentication.Authentication;
import ch.epfl.sweng.favors.database.Favor;
import ch.epfl.sweng.favors.database.FavorRequest;
import ch.epfl.sweng.favors.database.ObservableArrayList;
import ch.epfl.sweng.favors.databinding.FavorsBinding;

public class InterestedUsersBubbles extends android.support.v4.app.Fragment {

    FavorsBinding binding;

    final String[] titles = null;//getResources().getStringArray(R.array.countries);
    final TypedArray colors = null;//getResources().obtainTypedArray(R.array.colors);
    final TypedArray images = null; //getResources().obtainTypedArray(R.array.images);

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.bubbles,container,false);
        binding.setElements(this);

        return binding.getRoot();
    }

    picker.setAdapter(new BubblePickerAdapter() {
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
            item.setTypeface(mediumTypeface);
            item.setTextColor(ContextCompat.getColor(DemoActivity.this, android.R.color.white));
            item.setBackgroundImage(ContextCompat.getDrawable(DemoActivity.this, images.getResourceId(position, 0)));
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

    @Override
    protected void onResume() {
        super.onResume();
        picker.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        picker.onPause();
    }
}
