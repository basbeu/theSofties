package ch.epfl.sweng.favors.database;

import android.databinding.ObservableField;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Map;
import javax.annotation.Nonnull;

public class User extends DatabaseHandler {

    private static final String TAG = "DB_USER";
    private static final String COLLECTION = "users";
    private FirebaseAuth instance;

    private static User user = new User();
    public static User getMain(){
        return user;
    }

    public FirebaseAuth getInstance() {
        return instance;
    }

    public static void setMain(String id){
        user = new User(id);
    }
    public static void setMain(User u) {user = u; }

    public enum StringFields implements DatabaseStringField{firstName, lastName, email, sex, location, pseudo}
    public enum IntFields implements DatabaseIntField{creationTimeStamp}
    public enum ObjectFields implements DatabaseObjectField {rights}


    public User(){
        super(StringFields.values(), COLLECTION,FirebaseAuth.getInstance().getUid());
        instance = FirebaseAuth.getInstance();
        if(instance.getUid() != null){
            updateFromDb();
        }

    }

    public User(String id){
        super(StringFields.values(), COLLECTION,id);
        instance = FirebaseAuth.getInstance();
        if(instance.getUid() != null) {
            updateFromDb();
        }
    }

    public User(FirebaseAuth instance){
        super(StringFields.values(), COLLECTION,instance.getUid());
        this.instance = instance;
        if(instance.getUid() != null) {
            updateFromDb();
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
            if(user != null) {
                String gender = user.get(User.StringFields.sex);
                gender = gender.trim().substring(0, 1);
                if (gender.toUpperCase().equals("M"))
                    return M;
                else if (gender.toUpperCase().equals("F"))
                    return F;
            }
            Log.e(TAG,"Failed to parse the gender returned by the database");
            return DEFAULT;
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
