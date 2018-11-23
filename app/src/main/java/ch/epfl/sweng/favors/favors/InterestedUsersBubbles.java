package ch.epfl.sweng.favors.favors;

import com.igalata.bubblepicker.rendering.BubblePicker;

import ch.epfl.sweng.favors.databinding.BubblesBinding;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
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

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.bubbles,container,false);
        binding.setElements(this);

        picker = binding.picker;

        return binding.getRoot();
    }

//    picker.setAdapter(
//            new BubblePickerAdapter() {
//        @Override
//        public int getTotalCount() {
//            //return titles.length;
//        }
//
//        @NotNull
//        @Override
//        public PickerItem getItem(int position) {
//            PickerItem item = new PickerItem();
//            item.setTitle(titles[position]);
//            item.setGradient(new BubbleGradient(colors.getColor((position * 2) % 8, 0),
//                    colors.getColor((position * 2) % 8 + 1, 0), BubbleGradient.VERTICAL));
//            item.setTypeface(mediumTypeface);
//            item.setTextColor(ContextCompat.getColor(DemoActivity.this, android.R.color.white));
//            item.setBackgroundImage(ContextCompat.getDrawable(DemoActivity.this, images.getResourceId(position, 0)));
//            return item;
//        }
//    });
//
//    picker.setListener(new BubblePickerListener() {
//        @Override
//        public void onBubbleSelected(@NotNull PickerItem item) {
//
//        }
//
//        @Override
//        public void onBubbleDeselected(@NotNull PickerItem item) {
//
//        }
//    }
//    );

}
