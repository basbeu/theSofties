package ch.epfl.sweng.favors.database;

import android.databinding.ObservableField;
import android.util.Log;

import com.google.firebase.firestore.GeoPoint;

import javax.annotation.Nonnull;

import ch.epfl.sweng.favors.authentication.Authentication;
import ch.epfl.sweng.favors.database.fields.DatabaseBooleanField;
import ch.epfl.sweng.favors.database.fields.DatabaseLongField;
import ch.epfl.sweng.favors.database.fields.DatabaseObjectField;
import ch.epfl.sweng.favors.database.fields.DatabaseStringField;

public class User extends DatabaseEntity{

    private static final String TAG = "DB_USER";
    private static final String COLLECTION = "users";
    public static final Long DEFAULT_TOKENS_NUMBER = 5L;

    private static Status status = new Status(Status.Values.NotLogged);

    private static User user = new User(Authentication.getInstance().getUid());
    public static User getMain(){
        return user;
    }
    public static void updateMain(){user = new User(Authentication.getInstance().getUid());}


    /**
     * User Database fields: String
     */
    public enum StringFields implements DatabaseStringField {
        firstName, lastName,
        email,
        sex,
        pseudo,
        city, // a string of the homecity selected by the user
        profilePicRef, // a reference to the image stored on Firebase
        token_id
    }

    /**
     * User Database fields: Numbers
     */
    public enum LongFields implements DatabaseLongField {
        creationTimeStamp,
        tokens
    }

    /**
     * User Database fields: Objects
     * Database objects are more complex non-primitive types
     * such as a location, rights or settings
     */
    public enum ObjectFields implements DatabaseObjectField {
        rights,
        location,
        notifications
    }

    /**
     * User Database fields: Boolean
     */
    public enum BooleanFields implements DatabaseBooleanField {
        emailNotifications // set whether a user will receive E-Mail notifications
    }

    /**
     * Empty user constructor required by Firebase
     */
    public User(){
        super(StringFields.values(), LongFields.values(), BooleanFields.values(),
                ObjectFields.values(), COLLECTION, null);
    }

    /**
     * Creates a new user object from the unique ID
     *
     * TODO As a future improvement that could be changed to a Static Factory singleton
     * which would not allow to create more than one instance of an already existing user
     * which should not be newly constructed but rather called e.g. getUser(UID id)
     * @param id
     */
    public User(String id){
        super(StringFields.values(), LongFields.values(), BooleanFields.values(),
                ObjectFields.values(), COLLECTION,id);
        if(db != null) db.updateFromDb(this);
    }

    @Override
    public DatabaseEntity copy() {
        User u = new User();
        u.set(this.documentID, this.getEncapsulatedObjectOfMaps());
        return u;
    }

    static public void resetMain() {
        status.disconnect();
        user.reset();
    }

    public void setLocation(@Nonnull GeoPoint geo){
        if (user.get(StringFields.lastName) != null
                && user.get(StringFields.email) != null
                && user.get(StringFields.sex) != null) {
            this.set(ObjectFields.location, geo);
            Database.getInstance().updateOnDb(user);
        }
    }

    public enum UserGender {
        M ,F, DEFAULT;

        private static final String TAG = "USER_GENDER";

        /**
         * Provides users gender. If user gender cannot be found it will return the DEFAULT value. If the gender is determined this method will return  M or F.
         * @param user that needs to be accessed to determine sex
         * @return M or F depending on users sex. Can return DEFAULT if
         */
        static public UserGender getGenderFromUser(User user){
            if(user == null) return DEFAULT;
            UserGender userGender = DEFAULT;
            String gender = user.get(User.StringFields.sex);
            if (genderIsValid(gender)) {
                gender = gender.trim().substring(0, 1);
                switch (gender.toUpperCase()) {
                    case "M":
                        userGender = M;
                        break;
                    case "F":
                        userGender = F;
                        break;
                    default:
                        Log.e(TAG, "Failed to parse the gender returned by the database");
                        break;
                }
            }

            return userGender;
        }

        private static boolean genderIsValid(String gender) {
            return !(gender == null || gender.length() == 0);
        }

        /**
         * Sets the gender of the user
         * @param user who sex needs to be modified
         * @param gender to set the users gender to
         */
        static public void setGender(@Nonnull User user, @Nonnull UserGender gender){
            user.set(User.StringFields.sex,gender.toString().toUpperCase());
        }

        static public ObservableField<String> getObservableGenderString(@Nonnull User user){
            return user.getObservableObject(User.StringFields.sex);
        }
    }
}

class Status extends ObservableField<Status.Values>{
    private static final String TAG = "User_Status";

    public enum Values{NotLogged, Logged}

    public Status(Status.Values value){
        super(value);
    }

    @Override
    public void set(Status.Values status){
        Log.e(TAG, "Editing the status of user directly is forbidden");
    }

    public void loggedInSuccess() {
        super.set(Values.Logged);
    }

    public void disconnect(){
        super.set(Values.NotLogged);
    }


}