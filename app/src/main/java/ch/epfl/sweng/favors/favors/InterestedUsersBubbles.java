package ch.epfl.sweng.favors.favors;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.igalata.bubblepicker.BubblePickerListener;
import com.igalata.bubblepicker.adapter.BubblePickerAdapter;
import com.igalata.bubblepicker.model.BubbleGradient;
import com.igalata.bubblepicker.rendering.BubblePicker;

import ch.epfl.sweng.favors.database.Database;
import ch.epfl.sweng.favors.database.Favor;
import ch.epfl.sweng.favors.database.User;
import ch.epfl.sweng.favors.database.UserRequest;
import ch.epfl.sweng.favors.databinding.BubblesBinding;

import android.arch.lifecycle.ViewModelProviders;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ObservableArrayList;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.igalata.bubblepicker.model.PickerItem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.favors.R;

public class InterestedUsersBubbles extends android.support.v4.app.Fragment {
    private static final String TAG = "BUBBLES_FRAGMENT";

    BubblesBinding binding;
    BubblePicker picker;

    private String[] titles;
    private TypedArray colors;
//    final TypedArray images = getResources().obtainTypedArray(R.array.images);
    private ObservableArrayList<String> userNames;
    private ObservableArrayList<String> selectedUsers;
    private ObservableArrayList<String> interestedPeople;
    private Favor localFavor;
    private Task iplist;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        colors = getResources().obtainTypedArray(R.array.colors);
        userNames = new ObservableArrayList<>();
        userNames.addAll(getArguments().getStringArrayList("userNames"));
        interestedPeople = new ObservableArrayList<>();
        interestedPeople.addAll(getArguments().getStringArrayList("interestedPeople"));
        selectedUsers = new ObservableArrayList<>();

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.bubbles,container,false);
        binding.setElements(this);

        picker = binding.picker;
        picker.setCenterImmediately(true);

        picker.setAdapter(new BubblePickerAdapter() {
            @Override
            public int getTotalCount() {
                return userNames.size();
            }

            @NotNull
            @Override
            public PickerItem getItem(int position) {
                PickerItem item = new PickerItem();
                item.setTitle(userNames.get(position));
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
                int index = userNames.indexOf(item.getTitle());
                // check sanity - that it does not fail to get
                selectedUsers.add(interestedPeople.get(index));
                //Log.d("bubbles add", selectedUsers.toString());
                item.setGradient(new BubbleGradient(12,4));
            }

            @Override
            public void onBubbleDeselected(@NotNull PickerItem item) {
                int index = userNames.indexOf(item.getTitle());
                // check sanity - that it does not fail to get
                selectedUsers.remove(interestedPeople.get(index));
                //Log.d("bubbles remove", selectedUsers.toString());
                item.setGradient(new BubbleGradient(12,4));
            }
        });

        binding.buttonDone.setOnClickListener((l)->{
            FavorDetailView mFrag = new FavorDetailView();
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("selectedUsers", selectedUsers);
            Log.d("bubbles selected final", selectedUsers.toString());
            mFrag.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    mFrag).addToBackStack(null).commit();
        });

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        picker.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        picker.onPause();
    }


}
