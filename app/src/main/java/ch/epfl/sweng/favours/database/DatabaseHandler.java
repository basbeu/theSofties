package ch.epfl.sweng.favours.database;

import android.databinding.ObservableField;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public abstract class DatabaseHandler {
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    protected Map<DatabaseStringField, ObservableField<String>> stringData;
    protected Map<DatabaseIntField, ObservableField<Integer>> intData;

    protected final String collection;
    protected final String documentID;

    public DatabaseHandler(DatabaseStringField fields[], String collection, String documentID){
        assert(fields != null && collection != null);

        stringData = new HashMap<DatabaseStringField, ObservableField<String>>(){
            {
                for(DatabaseStringField field : fields){
                    this.put(field, new ObservableField<String>());
                }
            }

        };

        intData = new HashMap<>();
        this.collection = collection;
        this.documentID = documentID;
    }

    public void updateOnDb(){
        Map<String, Object> toSend = new HashMap<>();

        convertTypedMapToObjectMap(stringData, toSend);

        // Do the same here if other types of datas

        db.collection(collection).document(documentID).set(toSend)
                .addOnSuccessListener(aVoid ->   updateFromDb()).addOnFailureListener(e -> {
            /* Feedback of an error here - Impossible to update user informations */
        });
    }

    public void updateFromDb(){
        db.collection(collection).document(documentID)
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if(document.getData() != null) parseStringData(User.StringFields.values(),document.getData());
                // Do the same for rest of datas

            } else {
                /* Feedback of an error here - Impossible to get user informations from server */
            }
        });
    }

    public String get(DatabaseStringField field){
        if(stringData.get(field) != null)
            return stringData.get(field).get();
        else
            return null;
    }

    public ObservableField<String> getObservableStringObject(DatabaseStringField field){
        return stringData.get(field);
    }

    public void set(DatabaseStringField field, String value){
        stringData.get(field).set(value);
    }

    public Integer get(DatabaseIntField field){
        if(intData.get(field) != null)
            return intData.get(field).get();
        else
            return null;
    }

    protected void parseStringData(DatabaseStringField fields[], Map<String, Object> data){
        for(DatabaseStringField fieldName : fields){
            if(data.get(fieldName.toString()) instanceof String){
                set(fieldName, (String) data.get(fieldName.toString()));
            }
        }
    }

    /**
     * Convert the map containing some parameters in an String / Object map
     * @param from  The original map to convert
     * @param to    The map where to add objects
     * @param <T>   The enum of map content
     * @param <V>   The ObservableField content type
     * @param <U>   The ObservableField
     */
    protected <T extends DatabaseField, V, U extends ObservableField<V>> void convertTypedMapToObjectMap(Map<T, U> from, Map<String, Object> to) {
        for (Map.Entry<T, U> entry : from.entrySet()){
            V value = (V) entry.getValue().get();
            if(value != null) to.put(entry.getKey().toString(), value);
        }
    }

}
