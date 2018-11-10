package ch.epfl.sweng.favors.database;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;

import ch.epfl.sweng.favors.database.fields.DatabaseField;
import ch.epfl.sweng.favors.database.fields.DatabaseStringField;

public final class UserRequest extends Request{
    private static final String TAG = "DB_USER_REQUEST";
    private static final String COLLECTION = "users";

    private UserRequest(){}
    public static ObservableArrayList<User> getList(DatabaseField element, String value, Integer limit, DatabaseStringField orderBy){
        return db.getList(User.class, COLLECTION, element, value, limit, orderBy);
    }

    public static ObservableField<User> getUserInformations(String value){
        return db.getWithId(User.class, COLLECTION, value);
    }
}
