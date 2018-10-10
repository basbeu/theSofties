package ch.epfl.sweng.favors.database;

import com.google.firebase.auth.FirebaseAuth;

public class User extends DatabaseHandler {
    private static final String COLLECTION = "users";

    private static User user = new User();
    public static User getMain(){
        return user;
    }

    public static void setMain(String id){
        user = new User(id);
    }

    public enum StringFields implements DatabaseStringField{firstName, lastName, email, sex, basedLocation}

    public User(){
        super(StringFields.values(), COLLECTION,FirebaseAuth.getInstance().getUid());
        if(FirebaseAuth.getInstance().getUid() != null)
            updateFromDb();
    }

    public User(String id){
        super(StringFields.values(), COLLECTION,id);
        if(FirebaseAuth.getInstance().getUid() != null)
            updateFromDb();
    }
}
