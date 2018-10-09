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

    private static User user = new User();
    public static User getMain(){
        return user;
    }

    public enum StringFields implements DatabaseStringField{firstName, lastName, email, sex, basedLocation}

    public User(){
        super(StringFields.values(), "users",FirebaseAuth.getInstance().getUid());
        if(FirebaseAuth.getInstance().getUid() != null)
            updateFromDb();
    }

}
