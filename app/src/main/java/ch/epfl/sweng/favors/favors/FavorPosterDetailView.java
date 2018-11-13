package ch.epfl.sweng.favors.favors;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.database.Favor;
import ch.epfl.sweng.favors.database.UserRequest;
import ch.epfl.sweng.favors.databinding.FragmentFavorDetailViewBinding;

import ch.epfl.sweng.favors.database.Database;
import ch.epfl.sweng.favors.database.User;
import ch.epfl.sweng.favors.databinding.FragmentFavorPosterDetailViewBinding;
import ch.epfl.sweng.favors.utils.ExecutionMode;

public class FavorPosterDetailView extends android.support.v4.app.Fragment {
    FragmentFavorDetailViewBinding binding;

    public ObservableField<String> ownerID;
    private User user;
    public ObservableField<String> firstName = new ObservableField<>();
    public ObservableField<String> lastName = new ObservableField<>();
    public ObservableField<String> sex = new ObservableField<>();
    public FavorPosterDetailView() {
        // Required empty public constructor
    }

    private String ownerEmail;
    public static final String OWNER_EMAIL = "ownerEMAIL";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!ExecutionMode.getInstance().isTest()){

            ownerEmail = getArguments().getString(OWNER_EMAIL);
            Log.w("TAG",ownerEmail);

            User favorCreatorUser = UserRequest.getWithEmail(ownerEmail);
            firstName = favorCreatorUser.getObservableObject(User.StringFields.firstName);
            lastName = favorCreatorUser.getObservableObject(User.StringFields.lastName);
            sex = favorCreatorUser.getObservableObject(User.StringFields.sex);
        }

        else{
            firstName.set("Toto");
            lastName.set("Tata");
            sex.set("M");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentFavorPosterDetailViewBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favor_poster_detail_view, container, false);
        binding.setPosterElements(this);
        binding.okButton.setOnClickListener((View v) -> getActivity().onBackPressed());
        return binding.getRoot();
    }


}
