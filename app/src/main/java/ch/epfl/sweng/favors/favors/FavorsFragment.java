package ch.epfl.sweng.favors.favors;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.database.User;
import ch.epfl.sweng.favors.databinding.FavorsBinding;

/**
 * Fragment that displays the list of favor and allows User to sort it and to search in it
 */
public class FavorsFragment extends android.support.v4.app.Fragment {


    FavorsBinding binding;

    static String[] modes = {"List view", "Map view"};
    int currentMode = 0;
    public ObservableField<String> buttonDisplay = new ObservableField<>();
    public ObservableField<String> tokens = User.getMain().getObservableObject(User.StringFields.tokens);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.favors,container,false);
        binding.setElements(this);


        binding.modeSwitch.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  currentMode++;
                  if(currentMode>= modes.length){
                      currentMode = 0;
                  }
                  setView(currentMode);
              }
            }

        );

        setView(currentMode);

        return binding.getRoot();
    }

    void setView(int i){
        switch (i){
            case 0 :

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.favors_container, new FavorsList()).commit();
                buttonDisplay.set(modes[1]);
                break;
            case 1 :
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.favors_container, new FavorsMap()).commit();
                buttonDisplay.set(modes[0]);
                break;
            default:

        }
    }



}