package ch.epfl.sweng.favors.database;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * The Favor class is an extension of the Database handler
 */
public class Favor extends DatabaseHandler {

    // tag for log messages
    private static final String TAG = "FAVOR";
    // identifier for firebase
    private static final String COLLECTION = "favors";

    // enums in order to abstract from database implementation
    public enum StringFields implements DatabaseStringField {title, ownerID, description}
    public enum IntegerFields implements DatabaseIntField {creationTimestamp, reward, expirationTimestamp}

    public enum ObjectFields implements DatabaseObjectField {location}
    public enum LocationFields implements DatabaseLocationField {currentLocation, preferredLocations}

    public enum BooleanFields implements DatabaseBooleanField {isOpen}


    /**
     * empty constructor as required per firebase
     */
    public Favor(){
        super(StringFields.values(), IntegerFields.values(), BooleanFields.values(),
                ObjectFields.values(), COLLECTION,null);
    }

    public Favor(String id){
        super(StringFields.values(), IntegerFields.values(), BooleanFields.values(),
                ObjectFields.values(), COLLECTION,id);
        updateFromDb();
    }


    @Override
    public void updateOnDb(){
        if(documentID == null){
             // Do the same here if other types of datas

            db.collection(collection).add(getEncapsulatedObjectOfMaps())
                    .addOnSuccessListener(docRef -> {
                        documentID = docRef.getId();
                        updateFromDb();
                    }).addOnFailureListener(e -> {
                        Log.d(TAG,"failure to push to database");
                /* Feedback of an error here - Impossible to update user informations */
            });
        }else{
            super.updateOnDb();
        }
    }
}
