package ch.epfl.sweng.favours.database;

import android.support.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

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
