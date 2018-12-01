package ch.epfl.sweng.favors.favors;

import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.database.Database;
import ch.epfl.sweng.favors.database.UserRequest;
import ch.epfl.sweng.favors.database.User;
import ch.epfl.sweng.favors.database.storage.Storage;
import ch.epfl.sweng.favors.databinding.FragmentFavorPosterDetailViewBinding;

public class FavorPosterDetailView extends android.support.v4.app.Fragment {

    private static String TAG = "FAVORS_POSTER_DETAIL_VIEW";
    public static final String OWNER_ID = "ownerID";


    public ObservableField<String> firstName = new ObservableField<>();
    public ObservableField<String> lastName = new ObservableField<>();
    public ObservableField<String> sex = new ObservableField<>();
    public ObservableField<String> profilePicRef = new ObservableField<>();

    private String ownerEmail;
    private User favorCreatorUser;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if(getArguments() != null && getArguments().getString(OWNER_ID) != null){
            ownerEmail = getArguments().getString(OWNER_ID);
        }
        else if(getActivity() != null && getActivity().getIntent() != null &&
                getActivity().getIntent().getStringExtra(OWNER_ID) != null){
            ownerEmail = getActivity().getIntent().getStringExtra(OWNER_ID);
        }
        else{
            Log.e(TAG, "Trying to intent a the fragment without the email of the favor owner");
        }

        favorCreatorUser = new User();
        UserRequest.getWithId(favorCreatorUser, ownerEmail);

        firstName = favorCreatorUser.getObservableObject(User.StringFields.firstName);
        lastName = favorCreatorUser.getObservableObject(User.StringFields.lastName);
        sex = favorCreatorUser.getObservableObject(User.StringFields.sex);
        profilePicRef = favorCreatorUser.getObservableObject(User.StringFields.profilePicRef);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentFavorPosterDetailViewBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favor_poster_detail_view, container, false);
        binding.setPosterElements(this);
        binding.okButton.setOnClickListener((View v) -> getActivity().onBackPressed());
        profilePicRef.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                Storage.getInstance().displayImage(profilePicRef, binding.profilePic, "profile");
            }
        });

        return binding.getRoot();
    }


}
