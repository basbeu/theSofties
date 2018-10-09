package ch.epfl.sweng.favours.database;

import java.util.HashMap;
import java.util.Map;

public class Favor extends DatabaseHandler {

    private static final String COLLECTION = "favors";

    public enum StringFields implements DatabaseStringField {title, ownerID, description}
    public enum IntegerFields implements DatabaseIntField {creationTimestamp}

    public Favor(){
        super(StringFields.values(), COLLECTION,null);
    }

    public Favor(String id){
        super(StringFields.values(), COLLECTION,id);
        updateFromDb();
    }

    @Override
    public void updateOnDb(){
        if(documentID == null){
            Map<String, Object> toSend = new HashMap<>();

            convertTypedMapToObjectMap(stringData, toSend);

            // Do the same here if other types of datas

            db.collection(collection).add(toSend)
                    .addOnSuccessListener(docRef -> {
                        documentID = docRef.getId();
                        updateFromDb();
                    }).addOnFailureListener(e -> {
                /* Feedback of an error here - Impossible to update user informations */
            });
        }else{
            super.updateOnDb();
        }
    }
}
