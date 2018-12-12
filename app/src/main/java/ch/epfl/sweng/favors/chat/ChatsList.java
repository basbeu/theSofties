package ch.epfl.sweng.favors.chat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.authentication.Authentication;
import ch.epfl.sweng.favors.database.ChatInformations;
import ch.epfl.sweng.favors.database.ChatMessage;
import ch.epfl.sweng.favors.database.ChatRequest;
import ch.epfl.sweng.favors.database.Database;
import ch.epfl.sweng.favors.database.DatabaseEntity;
import ch.epfl.sweng.favors.database.ObservableArrayList;
import ch.epfl.sweng.favors.databinding.ChatsListBinding;

public class ChatsList extends android.support.v4.app.Fragment {
    private static final String TAG = "CHATS_LIST";

    ChatsListBinding binding;
    ObservableArrayList<ChatInformations> chatsInformations = new ObservableArrayList<>();
    public ObservableBoolean isHomeScreen = new ObservableBoolean(false);
    public ObservableBoolean isUnreadMessages = new ObservableBoolean(false);
    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.chats_list,container,false);
        binding.setChatsList(this);
       
        binding.chatsListItems.setLayoutManager(new LinearLayoutManager(getContext()));

        ChatRequest.allChatsOf(chatsInformations, Authentication.getInstance().getUid());
        chatsInformations.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if(propertyId != ObservableArrayList.ContentChangeType.Update.ordinal()) return;
                if(isHomeScreen.get()){
                    ArrayList<ChatInformations> toRemove = new ArrayList<>();
                    for(ChatInformations message : (ObservableArrayList<ChatInformations>) sender){
                        ArrayList<String> opened = (ArrayList<String>) message.get(ChatInformations.ObjectFields.opened);
                        if(opened != null && opened.contains(Authentication.getInstance().getUid())){
                            toRemove.add(message);
                        }
                    }
                    ((ObservableArrayList<ChatInformations>) sender).removeAll(toRemove);
                    if(((ObservableArrayList<ChatInformations>) sender).size() > 0) isUnreadMessages.set(true);
                    else isUnreadMessages.set(false);

                }
                updateList((ObservableArrayList<ChatInformations>) sender);

            }
        });

        return binding.getRoot();
    }

    private void updateList(ObservableArrayList<ChatInformations> list){
        if(this.getActivity() == null) return; // Callback
        ChatInfosAdapter listAdapter = new ChatInfosAdapter(this, list);
        binding.chatsListItems.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
    }


    public void delete(ChatInformations element){
        AlertDialog alertDialog = new AlertDialog.Builder(this.getContext()).create();
        alertDialog.setTitle("Deletion !");
        alertDialog.setMessage("Are you sure you want to delete your conversation with "+ element.allParticipants.get() + ".");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Database.getInstance().deleteFromDatabase(element).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                ChatRequest.allChatsOf(chatsInformations, Authentication.getInstance().getUid());
                            }
                        });
                        dialog.dismiss();

                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public static void createChat(String title, String[] participantsIds, String firstMessageText){
        if(firstMessageText == null || firstMessageText.length() < 2 || participantsIds == null || participantsIds.length == 0) return;

        ChatInformations chatInformations = new ChatInformations();
        chatInformations.set(ChatInformations.LongFields.creationTime, new Date().getTime());
        ArrayList<String> participants = new ArrayList<>();
        participants.addAll(Arrays.asList(participantsIds));
        if(!participants.contains(Authentication.getInstance().getUid())) participants.add(Authentication.getInstance().getUid());
        chatInformations.set(ChatInformations.ObjectFields.participants, participants);

        if(title != null && title.length() > 2 ) chatInformations.set(ChatInformations.StringFields.title, title);
        chatInformations.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if(propertyId != DatabaseEntity.UpdateType.FROM_DB.ordinal()) return ;
                chatInformations.addMessageToConversation(firstMessageText);
                chatInformations.removeOnPropertyChangedCallback(this);
            }
        });
        Database.getInstance().updateOnDb(chatInformations);
    }

    public static void createChatAndOpen(String title, String[] participantsIds, FragmentManager fm){
        ChatInformations chatInformations = new ChatInformations();
        chatInformations.set(ChatInformations.LongFields.creationTime, new Date().getTime());
        ArrayList<String> participants = new ArrayList<>();
        participants.addAll(Arrays.asList(participantsIds));
        if(!participants.contains(Authentication.getInstance().getUid())) participants.add(Authentication.getInstance().getUid());
        chatInformations.set(ChatInformations.ObjectFields.participants, participants);

        if(title != null && title.length() > 2 ) chatInformations.set(ChatInformations.StringFields.title, title);
        chatInformations.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if(propertyId != DatabaseEntity.UpdateType.FROM_DB.ordinal()) return ;
                ChatsList.open(chatInformations, fm);
                chatInformations.removeOnPropertyChangedCallback(this);
            }
        });
        Database.getInstance().updateOnDb(chatInformations);
    }



    public void open(ChatInformations element){
        open(element, getFragmentManager());
    }

    public static void open(ChatInformations element, FragmentManager fm){
        ChatWindow chatWindow = new ChatWindow();
        chatWindow.setChatContent(element);
        fm.beginTransaction().replace(R.id.fragment_container, chatWindow).addToBackStack("chatWindow").commit();

    }
}