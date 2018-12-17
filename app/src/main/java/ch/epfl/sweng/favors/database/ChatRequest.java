package ch.epfl.sweng.favors.database;

import java.util.HashMap;
import java.util.Map;

import ch.epfl.sweng.favors.database.fields.DatabaseField;

public class ChatRequest extends Request{

    private static final String TAG = "DB_CHAT_REQUEST";
    private static final String COLLECTION_INFORMATIONS = "chatInformations";
    private static final String COLLECTION_MESSAGES = "chatMessages";

    public static void allChatsOf(ObservableArrayList<ChatInformations> list, String ownerId){
        Map<DatabaseField, Object> map = new HashMap<>();
        map.put(ChatInformations.ObjectFields.participants, ownerId);
        db.getList(list, ChatInformations.class, COLLECTION_INFORMATIONS, null, null,
                null, map, null, null );
    }

    public static void chatContentWithId(ObservableArrayList<ChatMessage> list, String chatId, Integer limit){
        db.getList(list, ChatMessage.class, COLLECTION_MESSAGES, ChatMessage.StringFields.conversationId, chatId, limit, ChatMessage.LongFields.messageDate);
    }

    public static void chatContentWithUpdates(ObservableArrayList<ChatMessage> list, String chatId, Integer limit){
        db.getLiveList(list, ChatMessage.class, COLLECTION_MESSAGES, ChatMessage.StringFields.conversationId, chatId, limit, ChatMessage.LongFields.messageDate);
    }

}
