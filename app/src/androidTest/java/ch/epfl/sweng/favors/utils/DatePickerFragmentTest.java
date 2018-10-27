package ch.epfl.sweng.favors.utils;

import android.os.Bundle;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;


@RunWith(AndroidJUnit4.class)
public class DatePickerFragmentTest {
    @Rule
    public FragmentTestRule<DatePickerFragment> mFragmentTestRule = new FragmentTestRule<>(DatePickerFragment.class);

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Test
    public void fragment_instantiated() {
        DatePickerFragment fragment = new DatePickerFragment();
        Bundle b = new Bundle(3);
        b.putInt(null, 22); b.putInt(null, 10); b.putInt(null, 1997);
        fragment.setArguments(b);
    }
}