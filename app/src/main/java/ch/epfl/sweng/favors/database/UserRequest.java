package ch.epfl.sweng.favors.database;

import ch.epfl.sweng.favors.database.fields.DatabaseField;

/**
 * UserRequest allows to get a list of users that fulfill certain criteria
 * Equally it allows retrieving a user by its unique id from the database
 * and via email as a legacy method
 */
public final class UserRequest extends Request{
    private static final String TAG = "DB_USER_REQUEST";
    private static final String COLLECTION = "users";

    private UserRequest(){}
    public static void getList(ObservableArrayList<User>  list, DatabaseField element, Object value, Integer limit, DatabaseField orderBy){
        db.getList(list, User.class, COLLECTION, element, value, limit, orderBy);
    }

    public static void getWithId(User user, String value){
        db.getElement(user, User.class, COLLECTION, value);
    }
    public static void getWithEmail(User user, String email){
        db.getElement(user, User.class, COLLECTION, User.StringFields.email, email);
    }
}
