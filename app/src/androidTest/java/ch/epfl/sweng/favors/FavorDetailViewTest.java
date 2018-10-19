package ch.epfl.sweng.favors;


import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import ch.epfl.sweng.favors.database.Favor;

import static android.support.test.espresso.Espresso.onView;
import static org.mockito.Mockito.when;

import static android.support.test.espresso.action.ViewActions.click;

import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;

import static android.support.test.espresso.action.ViewActions.scrollTo;

import static android.support.test.espresso.action.ViewActions.typeText;

import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class FavorDetailViewTest {

    private static String mCity = "Lausanne";
    private static String mTitile = "This is amazin";
    private static String mDescription = "This is a nice descritpion. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent non efficitur ex. Sed sagittis dolor eu nunc tempor, sit amet pharetra elit volutpat. Donec at dapibus ante. Duis at aliquam nunc, eget pretium urna. Aenean dapibus nisl consectetur, posuere nulla vitae, sagittis nulla. Class aptent taciti sociosqu ad litora torquent. ";
    private static String mCategory = "Shopping";


    @Rule
    public FragmentTestRule<FavorDetailView> mFragmentTestRule = new FragmentTestRule<>(FavorDetailView.class);
    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();


    @Test
    public void get_instance_test(){
        assertNotNull("Newly created object should not be null",FavorDetailView.newInstance("123456"));
    }

    @Mock
    private Favor fvMock = Mockito.mock(Favor.class);

    @Mock
    SharedViewFavor mSV = Mockito.mock(SharedViewFavor.class);

    @Test
    public void fields_are_displayed_properly(){

//        Mockito.when(fvMock.getObservableObject(Favor.StringFields.description)).thenReturn(new ObservableField<>(mDescription));
//        Mockito.when(fvMock.getObservableObject(Favor.StringFields.locationCity)).thenReturn(new ObservableField<>(mCity));
//        Mockito.when(fvMock.getObservableObject(Favor.StringFields.category)).thenReturn(new ObservableField<>(mCategory));
//        Mockito.when(fvMock.getObservableObject(Favor.StringFields.title)).thenReturn(new ObservableField<>(mTitile));
//
//
//        when(mSV.getFavor()).thenReturn(new LiveData<Favor>() {
//                                            @Override
//                                            public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<Favor> observer) {
//                                                super.observe(owner, observer);
//                                            }
//                                        });

                mFragmentTestRule.launchActivity(null);
        //onView(withId(R.id.favTitle)).check(matches(withContentDescription(mTitile)));
//        onView(withId(R.id.favDescription)).check(matches(withContentDescription(mDescription)));
//        onView(withId(R.id.favCity)).check(matches(withContentDescription(mCity)));
//        onView(withId(R.id.favCategory)).check(matches(withContentDescription(mCategory)));
    }
}
