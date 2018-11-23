package ch.epfl.sweng.favors.favors;

import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;

import com.igalata.bubblepicker.BubblePickerListener;
import com.igalata.bubblepicker.adapter.BubblePickerAdapter;
import com.igalata.bubblepicker.model.BubbleGradient;
import com.igalata.bubblepicker.model.PickerItem;

import ch.epfl.sweng.favors.R;

public class InterestedUsersBubbles extends android.support.v4.app.Fragment {

    final String[] titles = getResources().getStringArray(R.array.countries);
    final TypedArray colors = getResources().obtainTypedArray(R.array.colors);
    final TypedArray images = getResources().obtainTypedArray(R.array.images);

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
