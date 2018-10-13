package ch.epfl.sweng.favors.database;

import com.google.firebase.auth.FirebaseAuth;

public class Discussion extends DatabaseHandler{

    private static final String TAG = "DB_DISCUSSION";
    private static final String COLLECTION = "discussions";


    public enum StringFields implements DatabaseStringField{}
    public enum IntFields implements DatabaseIntField{}
    public enum ObjectFields implements DatabaseObjectField {}
    public enum BooleanFields implements DatabaseBooleanField {}


    public Discussion(String id){
        super(StringFields.values(), IntFields.values(), BooleanFields.values(),
                ObjectFields.values(), COLLECTION, id);
        updateFromDb();
    }


}
