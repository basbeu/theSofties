package ch.epfl.sweng.favors.favors;

import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.authentication.Authentication;
import ch.epfl.sweng.favors.chat.ChatsList;
import ch.epfl.sweng.favors.database.ChatInformations;
import ch.epfl.sweng.favors.database.ChatRequest;
import ch.epfl.sweng.favors.database.ObservableArrayList;
import ch.epfl.sweng.favors.database.UserRequest;
import ch.epfl.sweng.favors.database.User;
import ch.epfl.sweng.favors.database.storage.Storage;
import ch.epfl.sweng.favors.database.storage.StorageCategories;
import ch.epfl.sweng.favors.databinding.FragmentFavorPosterDetailViewBinding;

public class FavorPosterDetailView extends android.support.v4.app.Fragment {

    private static String TAG = "FAVORS_POSTER_DETAIL_VIEW";
    public static final String OWNER_ID = "ownerID";


    public ObservableField<String> firstName = new ObservableField<>();
    public ObservableField<String> lastName = new ObservableField<>();
    public ObservableField<String> sex = new ObservableField<>();
    public static ObservableField<String> profilePicRef = new ObservableField<>();
    public static FragmentFavorPosterDetailViewBinding binding;

    protected static Observable.OnPropertyChangedCallback pictureCallback = new Observable.OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable sender, int propertyId) {
            Storage.getInstance().displayImage(profilePicRef, binding.profilePic, StorageCategories.PROFILE);
        }
    };

    private String ownerEmail;
    private User favorCreatorUser;


    public void setUser(User user){
        favorCreatorUser = user;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if(getArguments() != null && getArguments().getString(OWNER_ID) != null){
            ownerEmail = getArguments().getString(OWNER_ID);

        }
        else if(getActivity() != null && getActivity().getIntent() != null &&
                getActivity().getIntent().getStringExtra(OWNER_ID) != null) {
            ownerEmail = getActivity().getIntent().getStringExtra(OWNER_ID);
        }
        else if(favorCreatorUser == null){
            Log.e(TAG, "Trying to intent a the fragment without the email of the favor owner");
            (FavorPosterDetailView.this).getFragmentManager().beginTransaction().remove(this).commit();
            return;
        }

        if(favorCreatorUser == null){
            favorCreatorUser = new User();
            UserRequest.getWithId(favorCreatorUser, ownerEmail);
        }

        firstName = favorCreatorUser.getObservableObject(User.StringFields.firstName);
        lastName = favorCreatorUser.getObservableObject(User.StringFields.lastName);
        sex = favorCreatorUser.getObservableObject(User.StringFields.sex);
        profilePicRef = favorCreatorUser.getObservableObject(User.StringFields.profilePicRef);



    }

    View.OnClickListener loadOrCreateConversation = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ObservableArrayList<ChatInformations> conversations = new ObservableArrayList<>();
            ChatRequest.allChatsOf(conversations, favorCreatorUser.getId());
            conversations.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(Observable sender, int propertyId) {
                    if(propertyId != ObservableArrayList.ContentChangeType.Update.ordinal()) return;
                    for(ChatInformations chat : conversations){
                        ArrayList<String> participantsId = (ArrayList<String>) chat.get(ChatInformations.ObjectFields.participants);
                        if(participantsId.contains(Authentication.getInstance().getUid()) && participantsId.size() == 2){
                            ChatsList.open(chat, getFragmentManager());
                            return;
                        }
                    }
                    ChatsList.createChatAndOpen(null, new String[]{favorCreatorUser.getId(), Authentication.getInstance().getUid()}, getFragmentManager());
                }
            });
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favor_poster_detail_view, container, false);
        binding.setPosterElements(this);
        binding.okButton.setOnClickListener((View v) -> getActivity().onBackPressed());
        profilePicRef.addOnPropertyChangedCallback(pictureCallback);

        binding.writeMessage.setOnClickListener(loadOrCreateConversation);



        return binding.getRoot();
    }


}
