package ch.epfl.sweng.favors.profile;

import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.authentication.Authentication;
import ch.epfl.sweng.favors.authentication.AuthenticationProcess;
import ch.epfl.sweng.favors.database.Database;
import ch.epfl.sweng.favors.database.Favor;
import ch.epfl.sweng.favors.database.FavorRequest;
import ch.epfl.sweng.favors.database.ObservableArrayList;
import ch.epfl.sweng.favors.database.User;
import ch.epfl.sweng.favors.database.fields.DatabaseField;
import ch.epfl.sweng.favors.databinding.FragmentProfileLayoutBinding;
import ch.epfl.sweng.favors.utils.TextWatcherCustom;
import ch.epfl.sweng.favors.utils.Utils;

public class ProfileFragment extends Fragment {

    private static final String LOG_TAG = "PROFILE_FRAGMENT";

    public ObservableField<String> firstName = User.getMain().getObservableObject(User.StringFields.firstName);
    public ObservableField<String> lastName = User.getMain().getObservableObject(User.StringFields.lastName);
    public ObservableField<String> baseCity = User.getMain().getObservableObject(User.StringFields.city);
    public ObservableField<String> sex = User.getMain().getObservableObject(User.StringFields.sex);
    public ObservableField<String> email = User.getMain().getObservableObject(User.StringFields.email);
    public ObservableField<String> profileName =  new ObservableField<>();
    public ObservableField<Long> tokens = User.getMain().getObservableObject(User.LongFields.tokens);
    public ObservableArrayList<Favor> interestingFavorsList = new ObservableArrayList<>();
    public Map<DatabaseField, Object> interestedPeopleUserId = new HashMap<>();
    public AuthenticationProcess.Action action;
    public Authentication auth = Authentication.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    FragmentProfileLayoutBinding binding;

    public ObservableBoolean isPasswordCorrect = new ObservableBoolean(false);

    private TextWatcher passwordTextWatcher = new TextWatcherCustom() {
        @Override
        public void afterTextChanged(Editable editable) {
            isPasswordCorrect.set(Utils.passwordFitsRequirements(binding.passwordEntry.getText().toString()));
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        Database.getInstance().updateFromDb(User.getMain());

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_layout,container,false);
        binding.setElements(this);
        // Check if the password is correct each time a letter was added
        binding.passwordEntry.addTextChangedListener(passwordTextWatcher);
        binding.editProfileButton.setOnClickListener((v)-> {
            Fragment fragment = null;
            fragment = new EditProfileFragment();
            replaceFragment(fragment);
        });
        binding.delete.setOnClickListener((v)-> {
            getView().findViewById(R.id.reauthenticateMessage).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.passwordEntry).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.confirmDeletion).setVisibility(View.VISIBLE);
        });

        binding.confirmDeletion.setOnClickListener((v)-> {
            AuthCredential credential = EmailAuthProvider.getCredential(auth.getEmail(),
                    binding.passwordEntry.getText().toString());
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String userID = user.getUid();
            //Reauthentication is necessary because the user session may be logged in for a long time.
            //Reauthenticate will update the user login session and prevent FirebaseException
            // (CREDENTIAL_TOO_OLD_LOGIN_AGAIN) on user.delete()
            user.reauthenticate(credential)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            user.delete()
                                    .addOnCompleteListener (task12 -> {
                                        if (task12.isSuccessful()) {
                                            Utils.logout(getContext(), auth);
                                            //clean/delete Cloudstore documents related to that
                                            db.collection("users").document(userID).delete();
                                            //update "favors" Firestore collection
                                            //1 - remove user's favors
                                            Query favorsQuery = db.collection("favors").whereEqualTo("ownerID", userID);
                                            favorsQuery.get().addOnSuccessListener(queryDocumentSnapshots -> {
                                                for(QueryDocumentSnapshot favor : queryDocumentSnapshots){
                                                    favor.getReference().delete();
                                                }
                                            });
                                            //2 - remove the user from all interested
                                                FavorRequest.getList(interestingFavorsList, interestedPeopleUserId,
                                                        null, null, null, null);
                                                interestingFavorsList.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
                                                    @Override
                                                    public void onPropertyChanged(Observable sender, int propertyId) {
                                                        removeUserFromInterestedPeopleInFavors(interestedPeopleUserId, interestingFavorsList,userID);
                                                    }
                                                });
                                            Toast.makeText(getContext(), R.string.userDeletionSuccessful, Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getContext(), R.string.userDeletionFail, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(getContext(), R.string.wrongPassword, Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        updateTitle();

        return binding.getRoot();
    }

    private void updateTitle(){
        firstName.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                profileName.set(firstName.get() + "'s Profile");
            }
        });
    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public static void removeUserFromInterestedPeopleInFavors(Map<DatabaseField, Object>
                                                               interestedPeopleUserId,
                                                       ObservableArrayList<Favor> interestingFavorsList,
                                                       String userID){
        interestedPeopleUserId.put(Favor.ObjectFields.interested, userID);
        for (Favor f : interestingFavorsList) {
            ArrayList<String> interestedPeople;
            interestedPeople = (ArrayList<String>) f.get(Favor.ObjectFields.interested);
            interestedPeople.remove(userID);
            f.set(Favor.ObjectFields.interested, interestedPeople);
            Database.getInstance().updateOnDb(f);
        }
    }

}
