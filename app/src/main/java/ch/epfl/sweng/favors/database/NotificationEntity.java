package ch.epfl.sweng.favors.database;

import ch.epfl.sweng.favors.database.fields.DatabaseBooleanField;
import ch.epfl.sweng.favors.database.fields.DatabaseLongField;
import ch.epfl.sweng.favors.database.fields.DatabaseObjectField;
import ch.epfl.sweng.favors.database.fields.DatabaseStringField;

public class NotificationEntity extends DatabaseEntity{
    private static final String TAG = "DB_NOTIFICATION";

    public enum StringFields implements DatabaseStringField {message}
    public enum LongFields implements DatabaseLongField {}
    public enum ObjectFields implements DatabaseObjectField {}
    public enum BooleanFields implements DatabaseBooleanField {}


    public NotificationEntity(String userId){
        this(null, userId);
    }

    public NotificationEntity(String id,String userId){
        super(StringFields.values(), LongFields.values(), BooleanFields.values(), ObjectFields.values(), getCollection(userId), id);

        if(db != null)
            db.updateFromDb(this);
    }


    @Override
    public DatabaseEntity copy() {
        return null;
    }

    public static String getCollection(String userId){
        return "users/" + userId + "/notifications";
    }
}
