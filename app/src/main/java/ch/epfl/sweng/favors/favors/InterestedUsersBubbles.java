package ch.epfl.sweng.favors.favors;

import com.igalata.bubblepicker.rendering.BubblePicker;

public class InterestedUsersBubbles extends android.support.v4.app.Fragment {

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
