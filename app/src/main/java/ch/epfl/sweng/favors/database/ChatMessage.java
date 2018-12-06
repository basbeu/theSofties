package ch.epfl.sweng.favors.database;


import android.databinding.Observable;
import android.databinding.ObservableField;

import java.util.List;

import ch.epfl.sweng.favors.authentication.Authentication;
import ch.epfl.sweng.favors.database.fields.DatabaseBooleanField;
import ch.epfl.sweng.favors.database.fields.DatabaseLongField;
import ch.epfl.sweng.favors.database.fields.DatabaseObjectField;
import ch.epfl.sweng.favors.database.fields.DatabaseStringField;

public class ChatMessage extends DatabaseEntity{

    private static final String TAG = "DB_CHAT_MESSAGE";
    private static final String COLLECTION = "chatMessages";

    public enum StringFields implements DatabaseStringField {writerId, messageContent, conversationId}
    public enum LongFields implements DatabaseLongField {messageDate}
    public enum ObjectFields implements DatabaseObjectField {}
    public enum BooleanFields implements DatabaseBooleanField {}

    public ObservableField<String> writerName = new ObservableField<>();

    public void updateWriterName(ChatInformations ci){
        for(User u : ci.participantsInfos){
            if(u.getId().equals(this.get(StringFields.writerId))){
                writerName.set(u.get(User.StringFields.firstName));
            }
        }
    }

    public ChatMessage(){
        super(StringFields.values(), LongFields.values(), BooleanFields.values(),
                ObjectFields.values(), COLLECTION, null);
    }

    public ChatMessage(String id){
        super(StringFields.values(), LongFields.values(), BooleanFields.values(),
                ObjectFields.values(), COLLECTION,id);
        if(db != null) db.updateFromDb(this);
    }

    @Override
    public DatabaseEntity copy() {
        ChatMessage u = new ChatMessage();
        u.set(this.documentID, this.getEncapsulatedObjectOfMaps());
        return u;
    }
}

