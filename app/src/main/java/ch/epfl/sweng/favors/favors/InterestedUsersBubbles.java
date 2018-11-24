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
import java.util.Optional;

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
        selectedUsers.addAll(getArguments().getStringArrayList("selectedUsers"));

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
                String name = userNames.get(position);
                item.setTitle(name);
                item.setGradient(new BubbleGradient(colors.getColor((position * 2) % 8, 0),
                        colors.getColor((position * 2) % 8 + 1, 0), BubbleGradient.VERTICAL));
//                        item.setTypeface(Typeface.BOLD);
                item.setTextColor(ContextCompat.getColor(getContext(), android.R.color.white));

                Optional<String> uid = getFrom(userNames, name, selectedUsers);
                if(uid.isPresent()) {
                    item.setSelected(true);
                }

//                item.setBackgroundImage(ContextCompat.getDrawable(getContext(), images.getResourceId(position, 0)));
                return item;
            }
        });

        picker.setListener(new BubblePickerListener() {
            @Override
            public void onBubbleSelected(@NotNull PickerItem item) {
                Optional<String> newUser = getFrom(userNames, item.getTitle(), interestedPeople);
                if(newUser.isPresent())
                    selectedUsers.add(newUser.get());
                Log.d("bubbles add", selectedUsers.toString());
            }

            @Override
            public void onBubbleDeselected(@NotNull PickerItem item) {
                Optional<String> newUser = getFrom(userNames, item.getTitle(), interestedPeople);
                if(newUser.isPresent())
                    selectedUsers.remove(newUser.get());
                Log.d("bubbles remove", selectedUsers.toString());
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

    protected static Optional<String> getFrom(List<String> givesIndex, String s, List<String> from) {
        //Log.d("bubbles print", givesIndex.toString() + s + from.toString());
        int index = givesIndex.indexOf(s);
        // check sanity - get user with uid and check that names match
        if(from.size() > index) {
            return Optional.of(from.get(index));
        } else {
            return Optional.empty();
        }
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
