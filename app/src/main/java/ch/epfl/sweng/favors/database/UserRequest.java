package ch.epfl.sweng.favors.database;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;

import java.util.ArrayList;

public final class UserRequest extends Request{
    private static final String TAG = "DB_USER_REQUEST";
    private static final String COLLECTION = "users";

    private UserRequest(){}
    public static ObservableArrayList<User> getList(DatabaseField element, String value, Integer limit, DatabaseStringField orderBy){
        return getList(User.class, COLLECTION, element, value, limit, orderBy);
    }

}
