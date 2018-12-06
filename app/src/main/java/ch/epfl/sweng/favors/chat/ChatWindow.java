package ch.epfl.sweng.favors.chat;

import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.authentication.Authentication;
import ch.epfl.sweng.favors.database.ChatInformations;
import ch.epfl.sweng.favors.database.ChatMessage;
import ch.epfl.sweng.favors.database.ChatRequest;
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
        ChatRequest.chatContentWithId(messages, informations.getId(), 100);
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


    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.chat_conversation, container,false);
        binding.setChatWindow(this);
        binding.sendButton.setEnabled(false);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setStackFromEnd(true);
        binding.chatConversationList.setLayoutManager(llm);

        messages.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if(propertyId != ObservableArrayList.ContentChangeType.Update.ordinal()){
                    return;
                }
                try { // To avoid a graphical bug
                    Thread.sleep(300);
                } catch (Exception e ){}
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
        ChatRequest.chatContentWithUpdates(messages, chatsInformations.getId(), 100);
        binding.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = binding.chatMessageText.getText().toString();
                if(value.length() < 1) return;
                chatsInformations.addMessageToConversation(value);
                binding.sendButton.setEnabled(false);
                binding.chatMessageText.setText("");
            }
        });

        return binding.getRoot();
    }

    private void updateList(ObservableArrayList<ChatMessage> list){
        if(this.getActivity() == null) return; // Callback
        ChatBubbleAdapter listAdapter = new ChatBubbleAdapter(this, list);
        binding.chatConversationList.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
    }

}