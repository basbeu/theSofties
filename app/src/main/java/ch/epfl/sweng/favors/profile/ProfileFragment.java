package ch.epfl.sweng.favors.profile;

import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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

    public AuthenticationProcess.Action action;

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
            Authentication auth = Authentication.getInstance();
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
                            Log.e("TAG", "onComplete: authentication complete");
                            user.delete()
                                    .addOnCompleteListener (task12 -> {
                                        if (task12.isSuccessful()) {
                                            Log.i("TAG", "User account deleted.");
                                            Utils.logout(getContext(), auth);
                                            Toast.makeText(getContext(), R.string.userDeletionSuccessful,
                                                    Toast.LENGTH_SHORT).show();
                                            //clean/delete Cloudstore documents related to that
                                            //deleted user
                                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                                            //update "users" Firestore collection
                                            db.collection("users").document(userID).delete().addOnCompleteListener(task1 -> {
                                                if(task1.isSuccessful()){
                                            Log.i("TAG", "User document successfully deleted.");
                                                } else {
                                                    Log.e("TAG", "User document deletion failed.");

                                                }
                                            });
                                            //update "favors" Firestore collection
                                            //1 - remove user's favors
                                            Query favorsQuery = db.collection("favors").whereEqualTo("ownerID", userID);
                                            favorsQuery.get().addOnSuccessListener(queryDocumentSnapshots -> {
                                                for(QueryDocumentSnapshot favor : queryDocumentSnapshots){
                                                    favor.getReference().delete();
                                                }
                                                Log.i("TAG", "All user's favors have been deleted.");

                                            });
                                            //2 - remove the user from all interested and selected
                                                //people lists from the other favors
                                            ReentrantLock lock = new ReentrantLock();
                                            synchronized (lock) {
                                                //2.1 - interested people
                                                ObservableArrayList<Favor> interestingFavorsList = new ObservableArrayList<>();
                                                Map<DatabaseField, Object> interestedPeopleUserId = new HashMap<>();
                                                FavorRequest.getList(interestingFavorsList, interestedPeopleUserId,
                                                        null, null, null, null);
                                                interestingFavorsList.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
                                                    @Override
                                                    public void onPropertyChanged(Observable sender, int propertyId) {
                                                        interestedPeopleUserId.put(Favor.ObjectFields.interested, userID);
                                                        for (Favor f : interestingFavorsList) {
                                                            ArrayList<String> interestedPeople;
                                                            interestedPeople = (ArrayList<String>) f.get(Favor.ObjectFields.interested);
                                                            interestedPeople.remove(userID);
                                                            f.set(Favor.ObjectFields.interested, interestedPeople);
                                                            Database.getInstance().updateOnDb(f);
                                                        }

                                                        Log.i("TAG", "All occurrences of the user have been deleted in the favors he was interested in.");
                                                        //2.2 - selectedPeople
                                                        ObservableArrayList<Favor> selectedFavorsList = new ObservableArrayList<>();
                                                        Map<DatabaseField, Object> selectedPeopleUserId = new HashMap<>();
                                                        FavorRequest.getList(selectedFavorsList, selectedPeopleUserId,
                                                                null, null, null, null);
                                                        selectedFavorsList.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
                                                            @Override
                                                            public void onPropertyChanged(Observable sender, int propertyId) {
                                                                selectedPeopleUserId.put(Favor.ObjectFields.selectedPeople, userID);
                                                                for (Favor f : selectedFavorsList) {
                                                                    ArrayList<String> selectedPeople;
                                                                    selectedPeople = (ArrayList<String>) f.get(Favor.ObjectFields.selectedPeople);
                                                                    selectedPeople.remove(userID);
                                                                    f.set(Favor.ObjectFields.selectedPeople, selectedPeople);
                                                                    Database.getInstance().updateOnDb(f);
                                                                }

                                                                Log.i("TAG", "All occurrences of the user have been deleted in the favors for which he was selected.");

                                                            }

                                                        });
                                                    }

                                                });

                                            }
                                        } else {
                                            Log.e("TAG", "User account deletion unsuccessful.");
                                            Toast.makeText(getContext(), R.string.userDeletionFail,
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(getContext(), R.string.wrongPassword,
                                    Toast.LENGTH_SHORT).show();
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
}
