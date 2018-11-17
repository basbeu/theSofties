package ch.epfl.sweng.favors.favors;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.firestore.GeoPoint;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.authentication.Authentication;
import ch.epfl.sweng.favors.database.Database;
import ch.epfl.sweng.favors.database.Favor;
import ch.epfl.sweng.favors.databinding.FragmentFavorDetailViewBinding;
import ch.epfl.sweng.favors.location.LocationHandler;
import ch.epfl.sweng.favors.utils.ExecutionMode;
import ch.epfl.sweng.favors.utils.email.EmailUtils;


/**
 * FavorDetailView
 * when you click on a Favor in the ListAdapter
 * fragment_favor_detail_view.xml
 */
public class FavorDetailView extends android.support.v4.app.Fragment  {

    public ObservableField<String> title;
    public ObservableField<String> description;
    public ObservableField<String> location;
    public ObservableField<String> category;
    public ObservableField<Object> geo;
    public ObservableField<String> distance = new ObservableField<>();
    public ObservableField<String> ownerEmail;
    public ObservableField<String> user;

    private Favor localFavor;

    FragmentFavorDetailViewBinding binding;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String FAVOR_ID = "favorID";
    private String currentFavorID;




    public FavorDetailView() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedViewFavor model = ViewModelProviders.of(getActivity()).get(SharedViewFavor.class);
        if(savedInstanceState != null) {
            currentFavorID = savedInstanceState.getString(FAVOR_ID);
            localFavor = new Favor(currentFavorID);
            setFields(localFavor);
        }
        else {
            model.getFavor().observe(this, newFavor -> {
                localFavor = newFavor;
                setFields(newFavor);
                //TODO add token cost binding with new database implementation
            });
        }

        if(ExecutionMode.getInstance().isTest()){
            localFavor = new Favor("F1");
            localFavor.set(Favor.StringFields.ownerID, "U3");
            localFavor.set(Favor.StringFields.category, "Hand help");
            localFavor.set(Favor.StringFields.deadline, "12.12.20");
            localFavor.set(Favor.StringFields.description, "I need help to get rid of an old friend.");
            localFavor.set(Favor.StringFields.title, "KILL THE BATMAN");
            localFavor.set(Favor.StringFields.locationCity, "Gotham City");
            localFavor.set(Favor.StringFields.ownerEmail, "toto.tata@pipi.com");
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
        //user.set();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favor_detail_view,container,false);
        binding.setElements(this);
        binding.favorPosterDetailViewAccess.setOnClickListener(v ->getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FavorPosterDetailView()).addToBackStack(null).commit()
        );
        binding.favReportAbusiveAdd.setOnClickListener((l)->{
            EmailUtils.sendEmail(Authentication.getInstance().getEmail(), "report@myfavors.xyz",
                    "Abusive favors : " + title.get(),
                    "The abusive favor is : title"+title.get()+"\ndescription : "+description.get(),
                    getActivity(),
                    "issue has been reported! Sorry for the inconvenience",
                    "Sorry an error occured, try again later...");
        });
        binding.favIntrestedButton.setOnClickListener((l)->{
            Log.d("SENDTO", "Clicked");
            EmailUtils.sendEmail(Authentication.getInstance().getEmail(), ownerEmail.get(),
                    "Someone is interested in: "+title.get(),
                    "Hi ! I am interested to help you with your favor. Please answer directly to this email.",
                    getActivity(),
                    "We will inform the poster of the add that you are interested to help!",
                    "Sorry an error occured, try again later...");
        });
        return binding.getRoot();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        currentFavorID = null;
    }

    @BindingAdapter("android:src")
    public static void setImageUri(ImageView view, String imageName) {
        if (imageName == null) {
            view.setImageURI(null);
        } else {
            view.setImageURI(Uri.parse("android.resource://ch.epfl.sweng.favors/drawable/"+imageName.toLowerCase().replaceAll("\\s","")));
        }
    }

}
