package ch.epfl.sweng.favors.favors;

import com.google.android.gms.tasks.Task;
import com.igalata.bubblepicker.BubblePickerListener;
import com.igalata.bubblepicker.adapter.BubblePickerAdapter;
import com.igalata.bubblepicker.model.BubbleGradient;
import com.igalata.bubblepicker.rendering.BubblePicker;

import ch.epfl.sweng.favors.databinding.BubblesBinding;

import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.os.Bundle;

import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.igalata.bubblepicker.model.PickerItem;

import org.jetbrains.annotations.NotNull;

import ch.epfl.sweng.favors.R;

public class InterestedUsersBubbles extends android.support.v4.app.Fragment {
    private static final String TAG = "BUBBLES_FRAGMENT";
    public static final String INTERESTED_USERS = "interestedUsers";
    public static final String SELECTED_USERS = "selectedUsers";

    BubblesBinding binding;
    BubblePicker picker;

    private TypedArray colors;
    //for future profile pictures
//    final TypedArray images = getResources().obtainTypedArray(R.array.images);
    private ObservableArrayList<String> userNames;
    private ObservableArrayList<String> selectedUsers;

    private final String BUTTON_STATE_D = "Done";
    private final String BUTTON_STATE_C = "Cancel";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        colors = getResources().obtainTypedArray(R.array.colors);
        userNames = new ObservableArrayList<>();
        selectedUsers = new ObservableArrayList<>();

        if (getArguments() != null) {
            userNames.addAll(getArguments().getStringArrayList(INTERESTED_USERS));
            selectedUsers.addAll(getArguments().getStringArrayList(SELECTED_USERS));
        } else {
            Log.d(TAG, "no arguments received");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.bubbles,container,false);
        binding.setElements(this);

        binding.buttonDone.setText(BUTTON_STATE_C);

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
                item.setTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
                if(selectedUsers.contains(name)) {
                    item.setSelected(true);
                }
                return item;
            }
        });

        picker.setListener(new BubblePickerListener() {
            @Override
            public void onBubbleSelected(@NotNull PickerItem item) {
                selectedUsers.add(item.getTitle());
                Log.d(TAG, "added " + item.getTitle() + " to selected users");
                // set button active (again)
                binding.buttonDone.setText(BUTTON_STATE_D);
                binding.buttonDone.setEnabled(true);
            }

            @Override
            public void onBubbleDeselected(@NotNull PickerItem item) {
                selectedUsers.remove(item.getTitle());
                Log.d(TAG, "removed " + item.getTitle() + " from selected users");
                if(selectedUsers.isEmpty()) {
                    Toast.makeText(getContext(), "Please select at least one person in order to continue", Toast.LENGTH_LONG).show();
                    binding.buttonDone.setEnabled(false);
                }
            }
        });

        binding.buttonDone.setOnClickListener((l)->{
            // Button Logic:
            // - can cancel before selecting first time (user clicks by error)
            // - cannot deselect all people from favor (less revenue for us -> why would you want to do that anyway)
            // - can select, change, add more and less (as long as at least one)
            Button b = binding.buttonDone;

            if(b.isEnabled()) { // this is redundant I think
                FavorDetailView mFrag = new FavorDetailView();
                if(b.getText() == BUTTON_STATE_D || b.getText() == BUTTON_STATE_D.toUpperCase()) {
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList(SELECTED_USERS, selectedUsers);
                    Log.d("bubbles selected final", selectedUsers.toString());
                    mFrag.setArguments(bundle);
                // proceed w/o sending a selection -> same as never been there
                } else if (binding.buttonDone.getText() == BUTTON_STATE_C || b.getText() == BUTTON_STATE_C.toUpperCase() && selectedUsers.isEmpty()) {
                    Toast.makeText(getContext(), "No selection made, don't forget to though!", Toast.LENGTH_SHORT).show();
                } else {
                    // Default case should not be triggered unless somebody renames buttons
                    Toast.makeText(getContext(), "Error: There was an unexpected problem with the selection!", Toast.LENGTH_LONG).show();
                }
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        mFrag).addToBackStack(null).commit();
            }
        });

        return binding.getRoot();
    }

}
