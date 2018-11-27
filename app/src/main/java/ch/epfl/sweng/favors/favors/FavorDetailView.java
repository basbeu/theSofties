package ch.epfl.sweng.favors.favors;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayMap;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableMap;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.authentication.Authentication;
import ch.epfl.sweng.favors.database.Database;
import ch.epfl.sweng.favors.database.Favor;
import ch.epfl.sweng.favors.database.User;
import ch.epfl.sweng.favors.database.UserRequest;
import ch.epfl.sweng.favors.database.storage.FirebaseStorageDispatcher;
import ch.epfl.sweng.favors.databinding.FragmentFavorDetailViewBinding;
import ch.epfl.sweng.favors.location.LocationHandler;
import ch.epfl.sweng.favors.utils.email.EmailUtils;

import static ch.epfl.sweng.favors.utils.Utils.getIconPathFromCategory;


/**
 * FavorDetailView
 * when you click on a Favor in the ListAdapter
 * fragment_favor_detail_view.xml
 */
public class FavorDetailView extends android.support.v4.app.Fragment  {
    private static final String TAG = "FAVOR_DETAIL_FRAGMENT";

    private StorageReference storageReference;

    public ObservableField<String> title;
    public ObservableField<String> description;
    public ObservableField<String> location;
    public ObservableField<String> category;
    public ObservableField<Object> geo;
    public ObservableField<String> distance = new ObservableField<>();
    public ObservableField<String> ownerEmail;
    public ObservableField<String> posterName;
    public ObservableField<String> user;
    public ObservableField<String> tokens;
    public ObservableBoolean isItsOwn = new ObservableBoolean(false);
    public ObservableBoolean buttonsEnabled = new ObservableBoolean(true);
    public ObservableBoolean isInterested = new ObservableBoolean(false);
    public ObservableField<String> pictureRef;

    private Favor localFavor;
    private ArrayList<String> interestedPeople;

    // Map K: name, V: uid
    private ObservableMap<String, String> userNames;
    // Map K: uid, V: name
    private ObservableMap<String, String> selectedUsers;

    private Task userListTask;

    FragmentFavorDetailViewBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String FAVOR_ID = "favor_id";
    public static final String ENABLE_BUTTONS = "enable_buttons";
    private ArrayList<String> bubblesResult;
    private boolean newSelectionOfUsers;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userNames = new ObservableArrayMap<>();
        selectedUsers = new ObservableArrayMap<>();
        bubblesResult = new ArrayList<>();

        Bundle arguments = getArguments();
        if(arguments != null && getArguments().containsKey(InterestedUsersBubbles.SELECTED_USERS)) {
            bubblesResult = new ArrayList<>(getArguments().getStringArrayList(InterestedUsersBubbles.SELECTED_USERS));
            newSelectionOfUsers = true;
        } else {
            newSelectionOfUsers = false;
        }

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
        tokens = favor.getObservableObject(Favor.StringFields.tokens);
        isItsOwn.set(favor.get(Favor.StringFields.ownerID).equals(User.getMain().getId()));
        pictureRef = favor.getObservableObject(Favor.StringFields.pictureReference);
        //user.set();

        FirebaseStorageDispatcher.getInstance().displayImage(pictureRef, binding.imageView);

        if(favor.getId() == null){
            binding.deleteButton.setEnabled(false);
            binding.interestedButton.setEnabled(false);
        }

        interestedPeople = new ArrayList<>();

        if(favor.get(Favor.ObjectFields.interested) != null){
            ArrayList<String> interestedPeopleL = (ArrayList<String>)favor.get(Favor.ObjectFields.interested);
            if (!interestedPeopleL.isEmpty()) {
                interestedPeople.addAll(interestedPeopleL);
            }
        }

        if (interestedPeople.contains(User.getMain().getId())) isInterested.set(true);

        // tmp list with db result of people selected
        final ArrayList<String> dbSelectionResult;
        if (favor.get(Favor.ObjectFields.selectedPeople) != null && favor.get(Favor.ObjectFields.interested) instanceof ArrayList)
            dbSelectionResult = (ArrayList<String>) localFavor.get(Favor.ObjectFields.selectedPeople);
        else
            dbSelectionResult = new ArrayList<>();
        for (String uid : interestedPeople) {
            setSelectionOfPeople(uid, dbSelectionResult);
        }

        User favorCreationUser = new User();
        UserRequest.getWithEmail(favorCreationUser, ownerEmail.get());
        posterName = favorCreationUser.getObservableObject(User.StringFields.firstName);
    }

    /**
     * set the important List of selected people
     *
     * @param uid
     * @param dbSelectionResult
     */
    private void setSelectionOfPeople(String uid, ArrayList<String> dbSelectionResult) {
        User u = new User(uid);

        UserRequest.getWithId(u, uid);

        userListTask = Database.getInstance().updateFromDb(u).addOnSuccessListener(o2 -> {
            if (u != null) {
                String fn = u.get(User.StringFields.firstName);
                String ln = u.get(User.StringFields.lastName);
                String key = fn + " " + ln;
                userNames.put(key, uid);

                // logic for determining which selection to use
                // if in bubbles selected and the order was bubbleView -> FavorDetailView
                if (newSelectionOfUsers) {
                    if (bubblesResult.contains(key)) {
                        selectedUsers.put(uid, key);
                        localFavor.set(Favor.ObjectFields.selectedPeople, new ArrayList<>(selectedUsers.keySet()));
                        Database.getInstance().updateOnDb(localFavor);
                    }
                    // if the order was list -> FavorDetailView (we want to get the selection from db
                } else {
                    if (dbSelectionResult.contains(uid)) {
                        selectedUsers.put(uid, key);
                    }
                }
            };
        });
    }

    Boolean buttonEnabled = true;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favor_detail_view, container, false);
        binding.setElements(this);

        binding.favReportAbusiveAdd.setOnClickListener((l)->
                EmailUtils.sendEmail(Authentication.getInstance().getEmail(), "report@myfavors.xyz",
                "Abusive favors : "+title.get(),
                "The abusive favor is : title"+title.get()+"\ndescription : "+description.get(),
                getActivity(),
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
                    EmailUtils.sendEmail(Authentication.getInstance().getEmail(), ownerEmail.get(),
                            "Someone is interested in: " + title.get(),
                            "Hi ! I am interested to help you with your favor. Please answer directly to this email.",
                            getActivity(),
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
            if(pictureRef != null && pictureRef.get() != null){
                StorageReference ref = FirebaseStorageDispatcher.getInstance().getReference().child("images/"+ pictureRef.get());
                ref.delete();
            }

            Toast.makeText(this.getContext(), "Favor deleted successfully", Toast.LENGTH_LONG).show();
            getActivity().onBackPressed();
        });
        binding.interestedUsers.setOnClickListener((l)->{
            if (interestedPeople.isEmpty()){
                Toast.makeText(getContext(), "Currently no interested people available.", Toast.LENGTH_LONG).show();
            } else {
                // opens bubble
                userListTask.addOnSuccessListener(o -> {
                    InterestedUsersBubbles mFrag = new InterestedUsersBubbles();
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList(InterestedUsersBubbles.INTERESTED_USERS, new ArrayList<>(userNames.keySet()));
                    Log.d(TAG, selectedUsers.toString());
                    // Map K: uid, V: name
                    bundle.putStringArrayList(InterestedUsersBubbles.SELECTED_USERS, new ArrayList<>(selectedUsers.values()));
                    mFrag.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            mFrag).addToBackStack(null).commit();
                });
            }
        });

        binding.favorPosterDetailViewAccess.setOnClickListener(v -> {
            FavorPosterDetailView mFrag = new FavorPosterDetailView();
            Bundle bundle = new Bundle();
            bundle.putString(FavorPosterDetailView.OWNER_EMAIL, localFavor.get(Favor.StringFields.ownerEmail));
            mFrag.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    mFrag).addToBackStack(null).commit();
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
        ArrayList<String> uidsSelected = (ArrayList<String>) localFavor.get(Favor.ObjectFields.selectedPeople);
        ArrayList<String> uidsIntrested = (ArrayList<String>) localFavor.get(Favor.ObjectFields.interested);
        AtomicLong nbPerson = new AtomicLong(localFavor.get(Favor.LongFields.nbPerson));
        long tokenPerPerson = localFavor.get(Favor.LongFields.tokenPerPerson);

        if (!uidsSelected.isEmpty()) {
            if (uidsSelected.size() <= nbPerson.get()) {
                for (String selUID : uidsSelected) {
                    User u = new User(selUID);
                    Database.getInstance().updateFromDb(u).addOnSuccessListener(task -> {
                        long currentTok = u.get(User.LongFields.tokens); //TODO fix this with longs
                        u.set(User.LongFields.tokens, currentTok - tokenPerPerson);
                        Database.getInstance().updateOnDb(u);
                        nbPerson.getAndDecrement();
                        synchronized (uidsIntrested) {
                            uidsIntrested.remove(selUID);
                        }
                        EmailUtils.sendEmail(localFavor.get(Favor.StringFields.ownerEmail), u.get(User.StringFields.email),
                                "You have been paid for the favor " + title.get()+ "!",
                                "Thank you for helping me with my favor named :" + title.get() + ". You have been paid for it.",
                                getActivity(),"Users have been successfully paid.","");
                    }).addOnFailureListener(t -> {
                        Toast.makeText(getContext(), "Could not pay all users", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Failed to update user with uid: " + selUID + " from the DB");
                    });
                }
                uidsSelected = new ArrayList<>();
                localFavor.set(Favor.ObjectFields.interested, uidsIntrested);
                localFavor.set(Favor.ObjectFields.selectedPeople, uidsSelected);
                localFavor.set(Favor.LongFields.nbPerson, nbPerson.longValue());
                Database.getInstance().updateOnDb(localFavor);
                Toast.makeText(getContext(), "Users have been successfully paid.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Too many people are selected", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(getContext(), "Please select the people that you want to pay", Toast.LENGTH_LONG).show();
        }
    }
}
