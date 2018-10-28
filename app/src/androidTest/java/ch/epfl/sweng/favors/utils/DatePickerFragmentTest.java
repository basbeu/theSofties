package ch.epfl.sweng.favors.utils;

import android.os.Bundle;

import org.junit.Rule;
import org.junit.Test;

public class DatePickerFragmentTest {
    @Rule public FragmentTestRule<DatePickerFragment> mFragmentTestRule = new FragmentTestRule<>(DatePickerFragment.class);

    @Test
    public void fragment_instantiated() {
        DatePickerFragment fragment = new DatePickerFragment();
        Bundle b = new Bundle(3);
        b.putInt(null, 22); b.putInt(null, 10); b.putInt(null, 1997);
        fragment.setArguments(b);
    }
}