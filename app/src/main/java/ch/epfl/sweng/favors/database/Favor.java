package ch.epfl.sweng.favors.database;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

import ch.epfl.sweng.favors.database.fields.DatabaseBooleanField;
import ch.epfl.sweng.favors.database.fields.DatabaseIntField;
import ch.epfl.sweng.favors.database.fields.DatabaseObjectField;
import ch.epfl.sweng.favors.database.fields.DatabaseStringField;

/**
 * The Favor class is an extension of the Database handler
 */
public class Favor extends DatabaseHandler {

    // tag for log messages
    private static final String TAG = "FAVOR";
    // identifier for firebase
    private static final String COLLECTION = "favors";

    public enum StringFields implements DatabaseStringField {title, ownerID, description, locationCity, deadline, category}
    public enum IntegerFields implements DatabaseIntField {creationTimestamp, reward, expirationTimestamp}
    public enum ObjectFields implements DatabaseObjectField {location}
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

    public Favor(String id,FirebaseFirestore db){
        super(StringFields.values(), IntegerFields.values(), BooleanFields.values(),
                ObjectFields.values(), COLLECTION,id,db);
/*
        if(!ExecutionMode.getInstance().isTest()){
            throw new IllegalStateException("This constructor should be used only for testing purpose");
        }*/
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
                        Log.d(TAG,"failure to push favor to database");
                /* Feedback of an error here - Impossible to update user informations */
            });
        }else{
            super.updateOnDb();
        }
    }
}
