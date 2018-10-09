package ch.epfl.sweng.favours.database;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Database {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static Database database = new Database();
    static public Database getInstance(){
        return database;
    }

    /**
     * Init database access as the mentioned user
     * If no user provided, init a guest access
     * @param user  The user identity
     */
    public void load(FirebaseUser user){

    }

    /**
     * Stop authentificated database access and provide only a guest access
     */
    public void unload(){
        this.load(null);
    }

    public CollectionReference getUserCollection(){
        return db.collection("users");
    }
}
