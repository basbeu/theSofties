package ch.epfl.sweng.favors.database;

import android.databinding.ObservableField;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.annotation.Nonnull;

import ch.epfl.sweng.favors.ExecutionMode;

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

    public enum StringFields implements DatabaseStringField{firstName, lastName, email, sex, pseudo, city}
    public enum IntegerFields implements DatabaseIntField{creationTimeStamp}
    public enum ObjectFields implements DatabaseObjectField {rights, location}
    public enum BooleanFields implements DatabaseBooleanField {}

    public void updateUser(){
        user = new User(instance.getUid());
        user.set(StringFields.email, instance.getCurrentUser().getEmail());
    }

    public User(){
        super(StringFields.values(), IntegerFields.values(), BooleanFields.values(),
                ObjectFields.values(), COLLECTION,FirebaseAuth.getInstance().getUid());
        instance = FirebaseAuth.getInstance();
        if(instance.getUid() != null){
            updateFromDb();
        }
    }

    public User(String id){
        super(StringFields.values(), IntegerFields.values(), BooleanFields.values(),
                ObjectFields.values(), COLLECTION,id);
        instance = FirebaseAuth.getInstance();
        if(instance.getUid() != null) {
            updateFromDb();
        }
    }

    public User(FirebaseAuth instance){
        super(StringFields.values(), IntegerFields.values(), BooleanFields.values(),
                ObjectFields.values(), COLLECTION,instance.getUid());
        this.instance = instance;

        if(!ExecutionMode.getInstance().isTest()){
            throw new IllegalStateException("This constructor should be used only for testing purpose");
        }

        if(instance.getUid() != null) {
            updateFromDb();
        }
    }

    public User(FirebaseAuth instance, FirebaseFirestore db){
        super(StringFields.values(), IntegerFields.values(), BooleanFields.values(),
                ObjectFields.values(), COLLECTION,instance.getUid(),db);

        if(!ExecutionMode.getInstance().isTest()){
            throw new IllegalStateException("This constructor should be used only for testing purpose");
        }

        this.instance = instance;
        if(instance.getUid() != null) {
            updateFromDb();
        }
    }


    static public void resetMain() {
        user.reset();
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
                if (gender.toUpperCase().equals("M"))
                    userGender =  M;
                else if (gender.toUpperCase().equals("F"))
                    userGender = F;
                else
                    Log.e(TAG, "Failed to parse the gender returned by the database");
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
