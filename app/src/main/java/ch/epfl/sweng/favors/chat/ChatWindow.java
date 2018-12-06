package ch.epfl.sweng.favors.chat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Collections;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.authentication.Authentication;
import ch.epfl.sweng.favors.database.ChatInformations;
import ch.epfl.sweng.favors.database.ChatMessage;
import ch.epfl.sweng.favors.database.ChatRequest;
import ch.epfl.sweng.favors.database.Database;
import ch.epfl.sweng.favors.database.DatabaseEntity;
import ch.epfl.sweng.favors.database.ObservableArrayList;
import ch.epfl.sweng.favors.databinding.ChatConversationBinding;
import ch.epfl.sweng.favors.utils.TextWatcherCustom;

public class ChatWindow extends android.support.v4.app.Fragment {
    private static final String TAG = "CHAT_CONVERSATION";

    ChatConversationBinding binding;
    public ChatInformations chatsInformations;
    public ObservableArrayList<ChatMessage> messages = new ObservableArrayList<>();

    public void setChatContent(ChatInformations informations){
        chatsInformations = informations;
        ChatRequest.chatContentWithUpdates(messages, informations.getId(), 20);
        messages.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if(propertyId != ObservableArrayList.ContentChangeType.Update.ordinal()){ return; }
                for(ChatMessage message: messages){
                    message.updateWriterName(chatsInformations);
                }
            }
        });
    }

    public ObservableBoolean isEditingTitle = new ObservableBoolean(false);

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.chat_conversation, container,false);
        binding.setChatWindow(this);
        binding.sendButton.setEnabled(false);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        //llm.setStackFromEnd(true);
        llm.setReverseLayout(true);
        binding.chatConversationList.setLayoutManager(llm);

        messages.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                updateList((ObservableArrayList<ChatMessage>) sender);
            }
        });
        binding.sendButton.setEnabled(true);

        binding.chatMessageText.addTextChangedListener(new TextWatcherCustom() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                super.onTextChanged(charSequence, i, i1, i2);
                if(charSequence.length() < 1) binding.sendButton.setEnabled(false);
                else binding.sendButton.setEnabled(true);
            }
        });
        binding.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = binding.chatMessageText.getText().toString();
                if(value.length() < 1) return;
                chatsInformations.addMessageToConversation(value);
                binding.sendButton.setEnabled(false);
                binding.chatMessageText.setText("");
                mustLoadMore = true;
            }
        });

        binding.editTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEditingTitle.get() == false) isEditingTitle.set(true);
                else{
                    if(binding.editTitleText.getText().toString().length() > 2){
                        chatsInformations.set(ChatInformations.StringFields.title, binding.editTitleText.getText().toString());
                        Database.getInstance().updateOnDb(chatsInformations);
                    }
                    isEditingTitle.set(false);
                }
            }
        });

        return binding.getRoot();
    }

    boolean mustLoadMore = true;

    private void updateList(ObservableArrayList<ChatMessage> list){
        if(this.getActivity() == null) return; // Callback
        ChatBubbleAdapter listAdapter = new ChatBubbleAdapter(this, list);
        listAdapter.setOnTopReachedListener(new OnTopReachedListener() {
            @Override
            public void onTopReached() {
                if(!mustLoadMore) return;
                Toast.makeText(getContext(), "Older message loading", Toast.LENGTH_SHORT).show();
                ChatRequest.chatContentWithId(messages, chatsInformations.getId(), null);
                mustLoadMore = false;
            }
        });
        binding.chatConversationList.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
    }
    public void deleteMessage(ChatMessage message){
        AlertDialog alertDialog = new AlertDialog.Builder(this.getContext()).create();
        alertDialog.setTitle("Message deletion !");
        alertDialog.setMessage("Are you sure you want to delete the message ?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        message.set(ChatMessage.StringFields.messageContent, "Message deleted");
                        dialog.dismiss();

                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

}