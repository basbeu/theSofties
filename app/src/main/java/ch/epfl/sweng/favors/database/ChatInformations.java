package ch.epfl.sweng.favors.database;


import android.databinding.Observable;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.epfl.sweng.favors.authentication.Authentication;
import ch.epfl.sweng.favors.database.fields.DatabaseBooleanField;
import ch.epfl.sweng.favors.database.fields.DatabaseLongField;
import ch.epfl.sweng.favors.database.fields.DatabaseObjectField;
import ch.epfl.sweng.favors.database.fields.DatabaseStringField;

public class ChatInformations extends DatabaseEntity{

    private static final String TAG = "DB_CHAT_INFORMATIONS";
    private static final String COLLECTION = "chatInformations";

    public enum StringFields implements DatabaseStringField {title}
    public enum LongFields implements DatabaseLongField {lastMessageTime, creationTime}
    public enum ObjectFields implements DatabaseObjectField {participants, opened}
    public enum BooleanFields implements DatabaseBooleanField {}

    public ObservableArrayList<User> participantsInfos = new ObservableArrayList<>();
    public ObservableField<String> allParticipants = new ObservableField<>();

    public void updateAllParticipantsNames(){
        String out = "";
        for(int i=0; i < participantsInfos.size();){
            String userName = participantsInfos.get(i).get(User.StringFields.firstName);
            i++;
            if(userName == null || userName.isEmpty()) continue;
            out = out.concat(participantsInfos.get(i-1).getId().equals(Authentication.getInstance().getUid()) ? "You" : userName);
            if(i < participantsInfos.size() - 1){ out = out.concat(", ");}
            else if(i == participantsInfos.size() - 1){ out = out.concat(" and ");}
        }
        allParticipants.set(out);

    }

    public ChatInformations(){
        super(StringFields.values(), LongFields.values(), BooleanFields.values(),
                ObjectFields.values(), COLLECTION, null);
        requestUserInformations();
    }

    public ChatInformations(String id){
        super(StringFields.values(), LongFields.values(), BooleanFields.values(),
                ObjectFields.values(), COLLECTION,id);
        if(db != null) db.updateFromDb(this);
        requestUserInformations();
    }

    // Reload user informations each time user list is reloaded
    void requestUserInformations(){
        this.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if(propertyId == UpdateType.FROM_DB.ordinal()){
                    participantsInfos.clear();
                    for(String id: (List<String>) ChatInformations.this.get(ObjectFields.participants)){
                        User tempUser = new User(id);
                        tempUser.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
                            @Override
                            public void onPropertyChanged(Observable sender, int propertyId) {
                                if(propertyId == UpdateType.FROM_DB.ordinal()) updateAllParticipantsNames();
                            }
                        });
                        participantsInfos.add(tempUser);
                    }
                }
            }
        });
    }

    public void markAsRead(){
        Database.getInstance().updateFromDb(this).addOnCompleteListener(task -> {
            ArrayList<String> temp = (ArrayList<String>) ChatInformations.this.get(ObjectFields.opened);
            if(temp == null) temp = new ArrayList<>();
            if(!temp.contains(Authentication.getInstance().getUid())){
                temp.add(Authentication.getInstance().getUid());
                ChatInformations.this.set(ObjectFields.opened, temp);
                Database.getInstance().updateOnDb(ChatInformations.this);
            }
        });
    }

    public boolean isRead(){
        ArrayList<String> temp = (ArrayList<String>) ChatInformations.this.get(ObjectFields.opened);
        return temp != null && temp.contains(Authentication.getInstance().getUid());
    }


    public boolean addMessageToConversation(String messageContent){
        if(this.getId() == null) return false;

        ChatMessage message = new ChatMessage();
        message.set(ChatMessage.StringFields.conversationId, this.getId());
        message.set(ChatMessage.LongFields.messageDate, new Date().getTime());
        message.set(ChatMessage.StringFields.writerId, Authentication.getInstance().getUid());
        message.set(ChatMessage.StringFields.messageContent, messageContent);

        ArrayList<String> temp = new ArrayList<>();
        temp.add(Authentication.getInstance().getUid());

        this.set(ObjectFields.opened, temp);
        this.set(LongFields.lastMessageTime, new Date().getTime());
        Database.getInstance().updateOnDb(this);

        Database.getInstance().updateOnDb(message);
        return true;

    }

    @Override
    public DatabaseEntity copy() {
        ChatInformations u = new ChatInformations();
        u.set(this.documentID, this.getEncapsulatedObjectOfMaps());
        return u;
    }
}

