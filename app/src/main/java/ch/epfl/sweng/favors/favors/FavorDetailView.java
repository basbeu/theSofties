package ch.epfl.sweng.favors.favors;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableArrayMap;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableMap;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.authentication.Authentication;
import ch.epfl.sweng.favors.database.Database;
import ch.epfl.sweng.favors.database.Favor;
import ch.epfl.sweng.favors.database.User;
import ch.epfl.sweng.favors.database.UserRequest;
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
    public ObservableBoolean hasInterestedPeople = new ObservableBoolean(false);
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
        if(arguments != null && getArguments().containsKey(ENABLE_BUTTONS)){
            buttonsEnabled.set(arguments.getBoolean(ENABLE_BUTTONS));
        }
        if(arguments != null && getArguments().containsKey(FAVOR_ID)){
            localFavor = new Favor(arguments.getString(FAVOR_ID));
            setFields(localFavor);
        }
        else {
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
        distance.set(LocationHandler.distanceBetween((GeoPoint)geo.get()));
        tokens = favor.getObservableObject(Favor.StringFields.tokens);
        isItsOwn.set(favor.get(Favor.StringFields.ownerID).equals(User.getMain().getId()));
        pictureRef = favor.getObservableObject(Favor.StringFields.pictureReference);
        //user.set();
        if(pictureRef != null){
            Log.d("PICTAG", "101");
            storageReference = FirebaseStorage.getInstance().getReference();
            StorageReference ref = storageReference.child("images/"+ pictureRef.get());
            ref.getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Log.d("PICTAG", Integer.toString(bytes.length));
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    binding.imageView.setImageBitmap(bmp);
                    }});

        }

        if(favor.getId() == null){binding.deleteButton.setEnabled(false);binding.interestedButton.setEnabled(false);}

        if(favor.get(Favor.ObjectFields.interested) != null && favor.get(Favor.ObjectFields.interested) instanceof ArrayList) {
            interestedPeople = (ArrayList<String>) localFavor.get(Favor.ObjectFields.interested);
            hasInterestedPeople.set(true);
        }
        else interestedPeople = new ArrayList<>();
        if(interestedPeople.contains(User.getMain().getId())) isInterested.set(true);

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
                if(newSelectionOfUsers) {
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favor_detail_view,container,false);
        binding.setElements(this);

        // bindings
        binding.favReportAbusiveAdd.setOnClickListener((l)->
                EmailUtils.sendEmail(Authentication.getInstance().getEmail(), "report@myfavors.xyz",
                "Abusive favors : "+title.get(),
                "The abusive favor is : title"+title.get()+"\ndescription : "+description.get(),
                getActivity(),
                "issue has been reported! Sorry for the inconvenience",
                "Sorry an error occured, try again later..."));

        binding.interestedButton.setOnClickListener((l)->{
            if(isItsOwn.get()) {
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
                    if(interestedPeople.isEmpty()){
                        hasInterestedPeople.set(false);
                    }
                }
                else{
                    if(interestedPeople.isEmpty()){
                        hasInterestedPeople.set(true);
                    }
                    interestedPeople.add(User.getMain().getId());
                    isInterested.set(true);
                    EmailUtils.sendEmail(Authentication.getInstance().getEmail(), ownerEmail.get(),
                            "Someone is interested in: " + title.get(),
                            "Hi ! I am interested to help you with your favor. Please answer directly to this email.",
                            getActivity(),
                            "We will inform the poster of the add that you are interested to help!",
                            "Sorry an error occurred, try again later...");

                }

                if(localFavor != null){
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
            int newUserTokens = Integer.parseInt(User.getMain().get(User.StringFields.tokens)) + 1;
            User.getMain().set(User.StringFields.tokens, Integer.toString(newUserTokens));
            Database.getInstance().updateOnDb(User.getMain());
            Database.getInstance().deleteFromDatabase(localFavor);
            Toast.makeText(this.getContext(), "Favor deleted successfully", Toast.LENGTH_LONG).show();
            getActivity().onBackPressed();
        });

        binding.interestedUsers.setOnClickListener((l)->{
            if (!hasInterestedPeople.get()){
                Toast.makeText(getContext(), "Currently no interested people available.", Toast.LENGTH_LONG).show();
            }
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
        });

        binding.favorPosterDetailViewAccess.setOnClickListener(v ->{
            FavorPosterDetailView mFrag = new FavorPosterDetailView();
            Bundle bundle = new Bundle();
            bundle.putString(FavorPosterDetailView.OWNER_EMAIL, localFavor.get(Favor.StringFields.ownerEmail));
            mFrag.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    mFrag).addToBackStack(null).commit();
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
}
