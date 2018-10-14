package ch.epfl.sweng.favors.database;

import com.google.firebase.auth.FirebaseAuth;

public class User extends DatabaseHandler {
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

    public enum StringFields implements DatabaseStringField{firstName, lastName, email, sex, basedLocation}

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
}
