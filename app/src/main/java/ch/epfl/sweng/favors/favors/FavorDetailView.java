package ch.epfl.sweng.favors.favors;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.authentication.Authentication;
import ch.epfl.sweng.favors.database.Database;
import ch.epfl.sweng.favors.database.DatabaseEntity;
import ch.epfl.sweng.favors.database.Favor;
import ch.epfl.sweng.favors.database.User;
import ch.epfl.sweng.favors.database.UserRequest;
import ch.epfl.sweng.favors.database.storage.FirebaseStorageDispatcher;
import ch.epfl.sweng.favors.database.storage.StorageCategories;
import ch.epfl.sweng.favors.databinding.FragmentFavorDetailViewBinding;
import ch.epfl.sweng.favors.location.LocationHandler;
import ch.epfl.sweng.favors.utils.email.Email;
import ch.epfl.sweng.favors.utils.email.EmailUtils;

import static ch.epfl.sweng.favors.utils.Utils.getIconPathFromCategory;


/**
 * FavorDetailView
 * when you click on a Favor in the ListAdapter
 * fragment_favor_detail_view.xml
 */
public class FavorDetailView extends android.support.v4.app.Fragment  {
    private static final String TAG = "FAVOR_DETAIL_FRAGMENT";


    public ObservableField<String> title;
    public ObservableField<String> description;
    public ObservableField<String> location;
    public ObservableField<String> category;
    public ObservableField<Object> geo;
    public ObservableField<String> distance = new ObservableField<>();
    public ObservableField<String> ownerEmail;
    public ObservableField<String> posterName;
    public ObservableField<String> user;

    public ObservableField<Long> tokenPerPers;
    public ObservableField<Long> nbPers;

    public ObservableBoolean isItsOwn = new ObservableBoolean(false);
    public ObservableBoolean buttonsEnabled = new ObservableBoolean(true);
    public ObservableBoolean isInterested = new ObservableBoolean(false);
    public ObservableField<String> pictureRef;

    private Favor localFavor;

    private ArrayList<String> interestedPeople = new ArrayList<>();
    private ArrayList<String> selectedUsers = new ArrayList<>();
    // Map K: uid, V: name
    private Map<String, User> userNames;

    FragmentFavorDetailViewBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String FAVOR_ID = "favor_id";
    public static final String ENABLE_BUTTONS = "enable_buttons";
    private ArrayList<String> bubblesResult;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userNames = new HashMap<>();
        selectedUsers = new ArrayList<>();
        bubblesResult = new ArrayList<>();

        Bundle arguments = getArguments();
        SharedViewFavor model = ViewModelProviders.of(getActivity()).get(SharedViewFavor.class);
        if (arguments != null && getArguments().containsKey(ENABLE_BUTTONS)) {
            buttonsEnabled.set(arguments.getBoolean(ENABLE_BUTTONS));
        }
        if (arguments != null && getArguments().containsKey(FAVOR_ID)) {
            localFavor = new Favor(arguments.getString(FAVOR_ID));
            setFields(localFavor);
        } else {
            model.getFavor().observe(this, newFavor -> {
                localFavor = newFavor;
                setFields(newFavor);
                //TODO add token cost binding with new database implementation
            });
        }
    }

    public void setFields(Favor favor) {
        title = favor.getObservableObject(Favor.StringFields.title);
        description = favor.getObservableObject(Favor.StringFields.description);
        category = favor.getObservableObject(Favor.StringFields.category);
        location = favor.getObservableObject(Favor.StringFields.locationCity);
        geo = favor.getObservableObject(Favor.ObjectFields.location);
        ownerEmail = favor.getObservableObject(Favor.StringFields.ownerEmail);
        distance.set(LocationHandler.distanceBetween((GeoPoint) geo.get()));
        tokenPerPers = favor.getObservableObject(Favor.LongFields.tokenPerPerson);
        nbPers = favor.getObservableObject(Favor.LongFields.nbPerson);
        isItsOwn.set(favor.get(Favor.StringFields.ownerID).equals(User.getMain().getId()));
        pictureRef = favor.getObservableObject(Favor.StringFields.pictureReference);
        //user.set();

        FirebaseStorageDispatcher.getInstance().displayImage(pictureRef, binding.imageView, StorageCategories.FAVOR);



        if (favor.get(Favor.ObjectFields.interested) != null && favor.get(Favor.ObjectFields.interested) instanceof ArrayList) {
            interestedPeople = (ArrayList<String>) favor.get(Favor.ObjectFields.interested);
            if(isItsOwn.get()) {
                for (String uid : interestedPeople) {
                    User u = new User();
                    u.addOnPropertyChangedCallback(userInfosCb);
                    UserRequest.getWithId(u, uid);
                }
            }
            else if (interestedPeople.contains(User.getMain().getId()))
                isInterested.set(true);
        }

        User favorCreationUser = new User();
        UserRequest.getWithId(favorCreationUser, favor.get(Favor.StringFields.ownerID));
        posterName = favorCreationUser.getObservableObject(User.StringFields.firstName);
    }

    Boolean buttonEnabled = true;

    Observable.OnPropertyChangedCallback userInfosCb = new Observable.OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable sender, int propertyId) {
            if(propertyId == DatabaseEntity.UpdateType.FROM_REQUEST.ordinal()) {
                String fn = ((User) sender).get(User.StringFields.firstName);
                String ln = ((User) sender).get(User.StringFields.lastName);
                String key = fn + " " + ln;
                userNames.put(key,((User) sender));
                sender.removeOnPropertyChangedCallback(this);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favor_detail_view, container, false);
        binding.setElements(this);

        binding.favReportAbusiveAdd.setOnClickListener((l)->
                EmailUtils.sendEmail(
                        new Email(Authentication.getInstance().getEmail(), "report@myfavors.xyz", "Abusive favors : " + title.get(), "The abusive favor is : title" + title.get() + "\ndescription : " + description.get()), getActivity(),
                "issue has been reported! Sorry for the inconvenience",
                "Sorry an error occured, try again later..."));

        binding.interestedButton.setOnClickListener((l) -> {
            if (isItsOwn.get()) {
                FavorCreateFragment mFrag = new FavorCreateFragment();
                Bundle bundle = new Bundle();
                bundle.putString(FavorCreateFragment.KEY_FRAGMENT_ID, localFavor.getId());
                mFrag.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        mFrag).addToBackStack(null).commit();
            }else{
                //return if the timer is not over yet
                if(!buttonEnabled) return;
                //disable the button for preventing non-determinism
                buttonEnabled = false;
                //if user is in the list -> remove him from the list
                if(interestedPeople.contains(User.getMain().getId())) {
                    interestedPeople.remove(User.getMain().getId());
                    isInterested.set(false);
                }
                else{
                    interestedPeople.add(User.getMain().getId());
                    isInterested.set(true);
                    EmailUtils.sendEmail(
                            new Email(Authentication.getInstance().getEmail(), ownerEmail.get(), "Someone is interested in: " + title.get(), "Hi ! I am interested to help you with your favor. Please answer directly to this email."), getActivity(),
                            "We will inform the poster of the add that you are interested to help!",
                            "Sorry an error occurred, try again later...");

                }

                if (localFavor != null) {
                    localFavor.set(Favor.ObjectFields.interested, interestedPeople);
                    Database.getInstance().updateOnDb(localFavor);
                }


                new Handler().postDelayed(() -> {
                    // This method will be executed once the timer is over
                    buttonEnabled = true;
                },5000);
            }
        });


        binding.deleteButton.setOnClickListener((l)->{
            long newUserTokens = User.getMain().get(User.LongFields.tokens) + 1;
            User.getMain().set(User.LongFields.tokens, newUserTokens);
            Database.getInstance().updateOnDb(User.getMain());
            Database.getInstance().deleteFromDatabase(localFavor);
            FirebaseStorageDispatcher.getInstance().deleteImageFromStorage(pictureRef, StorageCategories.FAVOR);

            Toast.makeText(this.getContext(), "Favor deleted successfully", Toast.LENGTH_LONG).show();
            getActivity().onBackPressed();
        });

        binding.favorPosterDetailViewAccess.setOnClickListener(v -> {
            FavorPosterDetailView mFrag = new FavorPosterDetailView();
            Bundle bundle = new Bundle();
            bundle.putString(FavorPosterDetailView.OWNER_ID, localFavor.get(Favor.StringFields.ownerID));
            mFrag.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                    mFrag).addToBackStack("interested").commit();
        });

        binding.payButton.setOnClickListener(v -> {
           paySelectedPeople();
        });

        return binding.getRoot();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @BindingAdapter("android:src")
    public static void setImageUri(ImageView view, String imageName) {
        if (imageName == null) {
            view.setImageURI(null);
        } else {
            view.setImageURI(Uri.parse(getIconPathFromCategory(imageName)));
        }
    }

    public void paySelectedPeople() {

        long tokenPerPerson = localFavor.get(Favor.LongFields.tokenPerPerson);

        if(selectedUsers.size() == 0){
            Toast.makeText(getContext(), "No user selected.", Toast.LENGTH_SHORT).show();
        }

        for (String selName : selectedUsers) {
            User toUpdate = userNames.get(selName);
            toUpdate.set(User.LongFields.tokens, toUpdate.get(User.LongFields.tokens) + tokenPerPerson);
            Database.getInstance().updateOnDb(toUpdate);

            toUpdate.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(Observable sender, int propertyId) {
                    if(propertyId == User.UpdateType.FROM_DB.ordinal()){
                        EmailUtils.sendEmail(
                                new Email(localFavor.get(Favor.StringFields.ownerEmail), ((User) sender).get(User.StringFields.email), "You have been paid for the favor " + title.get() + "!", "Thank you for helping me with my favor named :" + title.get() + ". You have been paid for it."), getActivity(),"Users have been successfully paid.","");
                        sender.removeOnPropertyChangedCallback(this);
                    }
                }
            });
            localFavor.set(Favor.LongFields.nbPerson, localFavor.get(Favor.LongFields.nbPerson)-1);
            localFavor.set(Favor.ObjectFields.interested, interestedPeople.remove(toUpdate.getId()));
        }
        localFavor.set(Favor.ObjectFields.selectedPeople, new ArrayList<>());
        Database.getInstance().updateOnDb(localFavor);

        if(localFavor.get(Favor.LongFields.nbPerson) > 0)
            Toast.makeText(getContext(), "Users have been successfully paid. Reaming : " + localFavor.get(Favor.LongFields.nbPerson).toString(), Toast.LENGTH_SHORT).show();
        else{
            Toast.makeText(getContext(), "No token are reaming for this favor", Toast.LENGTH_LONG).show();

        }


    }
}


