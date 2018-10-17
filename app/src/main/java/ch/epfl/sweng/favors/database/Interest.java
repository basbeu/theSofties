package ch.epfl.sweng.favors.database;

import android.util.Log;

public class Interest extends DatabaseHandler{

    private static final String TAG = "DB_INTEREST";
    private static final String COLLECTION = "interests";

    public enum StringFields implements DatabaseStringField {title, description}
    public enum IntegerFields implements DatabaseIntField {}
    public enum ObjectFields implements DatabaseObjectField {linkedInterests}
    public enum BooleanFields implements DatabaseBooleanField {}


    public Interest(String id){
        super(StringFields.values(), IntegerFields.values(), BooleanFields.values(),
                ObjectFields.values(), COLLECTION,id);
        updateFromDb();
    }

    public Interest(){
        super(StringFields.values(), IntegerFields.values(), BooleanFields.values(),
                ObjectFields.values(), COLLECTION,null);
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
                Log.d(TAG,"failure to push interest to database");
                /* Feedback of an error here - Impossible to update user informations */
            });
        }else{
            super.updateOnDb();
        }
    }

}
