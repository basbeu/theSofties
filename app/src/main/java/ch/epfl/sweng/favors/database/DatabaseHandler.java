package ch.epfl.sweng.favors.database;

import android.content.Intent;
import android.databinding.ObservableField;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import ch.epfl.sweng.favors.FavorsMain;


public abstract class DatabaseHandler {

    protected static FirebaseFirestore db = FirebaseFirestore.getInstance();

    protected Map<DatabaseStringField, ObservableField<String>> stringData;
    protected Map<DatabaseIntField, ObservableField<Integer>> intData;
    protected Map<DatabaseBooleanField, ObservableField<Boolean>> booleanData;
    protected Map<DatabaseObjectField, ObservableField<Object>> objectData;


    protected final String collection;
    protected String documentID;

    private final static String TAG = "Favors_DatabaseHandler";

    /**
     * Init a database object with all fields that are possible for the instanced collection in the database
     *
     * @param stringFieldsValues The possibles names of string objects in the database
     * @param intFieldsValues The possibles names of int objects in the database
     * @param booleanFieldsValues The possibles names of boolean objects in the database
     * @param objectFieldsValues The possibles names of generic objects in the database that must be test
     *                           later, often tables
     * @param collection The collection in the database
     * @param documentID If so, the doccumentId in the database
     */
    public DatabaseHandler(DatabaseStringField stringFieldsValues[], DatabaseIntField intFieldsValues[],
                           DatabaseBooleanField booleanFieldsValues[], DatabaseObjectField objectFieldsValues[],
                           String collection, String documentID){

        assert(collection != null);

        stringData = initMap(stringFieldsValues);
        intData = initMap(intFieldsValues);
        booleanData = initMap(booleanFieldsValues);
        objectData = initMap(objectFieldsValues);


        this.collection = collection;
        this.documentID = documentID;
    }


    /**
     * Init the map with a null value for every possible object of a specific type
     *
     * @param possibleValues An array containing the possible names of this kind of object
     * @param <T> The field enum type
     * @param <V> The type of objects
     * @return An initialised map
     */
    private <T extends DatabaseField, V> Map<T, ObservableField<V>>  initMap( final T[] possibleValues){
        if(possibleValues == null || possibleValues.length == 0){return null;}
        return new HashMap<T, ObservableField<V>>(){
            {
                for(T field : possibleValues){
                    this.put(field, new ObservableField<V>());
                }
            }

        };
    }

    /**
     * Update all data currently in the class maps to the database
     */
    public void updateOnDb(){
        if(documentID != null) {
            db.collection(collection).document(documentID).set(getEncapsulatedObjectOfMaps())
                    .addOnSuccessListener(aVoid -> updateFromDb()).addOnFailureListener(e -> {
                /* Feedback of an error here - Impossible to update user informations */
            });
        }
        else{
            Log.e(TAG, "Trying to update data on an unknown document");
        }
    }

    /**
     * Return a uniform map with all data to send
     *
     * @return The maps with objects names and values
     */
    protected Map<String, Object> getEncapsulatedObjectOfMaps(){
        Map<String, Object> toSend = new HashMap<>();

        convertTypedMapToObjectMap(stringData, toSend);
        convertTypedMapToObjectMap(booleanData, toSend);
        convertTypedMapToObjectMap(intData, toSend);
        convertTypedMapToObjectMap(objectData, toSend);

        return toSend;
    }

    /**
     * Send request to update local map with database content
     *
     * @return True if the request for data was sent
     */
    public boolean updateFromDb(){
        if(documentID == null){return false;}
        db.collection(collection).document(documentID)
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                updateLocalData(document.getData());

            } else {
                Toast.makeText(FavorsMain.getContext(), "An error occured while requesting " +
                        "data from database",Toast.LENGTH_LONG);
            }
        });
        return true;
    }

    /**
     * Update local data with a generic content with Objects
     *
     * @param incommingData The map with object content and object value
     * @return True is successfull
     */
    protected void updateLocalData(Map<String, Object> incommingData){
        convertObjectMapToTypedMap(incommingData, stringData, String.class);
        convertObjectMapToTypedMap(incommingData, booleanData, Boolean.class);
        convertObjectMapToTypedMap(incommingData, objectData, Object.class);
        convertObjectMapToTypedMap(incommingData, intData, Integer.class);
    }

    /**
     * Convert a string / object map to local typed object map
     *
     * @param from  The received map to convert
     * @param to    The map where to add typed object
     * @param <T>   The enum of map content
     * @param <V>   The ObservableField content type
     * @param <U>   The ObservableField
     */
    private static <T extends DatabaseField, V extends Object, U extends ObservableField<V>> void convertObjectMapToTypedMap(Map<String, Object> from, Map<T, U> to, Class<V> clazz) {
        for (Map.Entry<T, U> entry : to.entrySet()){
            T fieldName = entry.getKey();
            Object object = from.get(fieldName.toString());
            if(object != null){
                try {
                    to.get(fieldName).set(clazz.cast(object));
                } catch(ClassCastException e) {
                    Log.e(TAG, "Error while casting incomming data");
                }
            }
        }
    }

    /**
     * Convert the map containing some parameters in an String / Object map
     *
     * @param from  The original map to convert
     * @param to    The map where to add objects
     * @param <T>   The enum of map content
     * @param <V>   The ObservableField content type
     * @param <U>   The ObservableField
     */
    private <T extends DatabaseField, V, U extends ObservableField<V>> void convertTypedMapToObjectMap(Map<T, U> from, Map<String, Object> to) {
        for (Map.Entry<T, U> entry : from.entrySet()){
            V value = (V) entry.getValue().get();
            if(value != null) to.put(entry.getKey().toString(), value);
        }
    }


    /*
     * Get / set methods for the different types of data
     */
    public String get(DatabaseStringField field){
        if(stringData.get(field) != null)
            return stringData.get(field).get();
        else
            return null;
    }

    public void set(DatabaseStringField field, String value){
        stringData.get(field).set(value);
    }

    public ObservableField<String> getObservableObject(DatabaseStringField field){
        return stringData.get(field);
    }

    public Object get(DatabaseObjectField field){
        if(objectData.get(field) != null)
            return objectData.get(field).get();
        else
            return null;
    }

    public void set(DatabaseObjectField field, Object value){
        objectData.get(field).set(value);
    }

    public ObservableField<Object> getObservableObject(DatabaseObjectField field){
        return objectData.get(field);
    }

    public Integer get(DatabaseIntField field){
        if(intData.get(field) != null)
            return intData.get(field).get();
        else
            return null;
    }

    public void set(DatabaseIntField field, Integer value){
        intData.get(field).set(value);
    }

    public ObservableField<Integer> getObservableObject(DatabaseIntField field){
        return intData.get(field);
    }

    public Boolean get(DatabaseBooleanField field){
        if(booleanData.get(field) != null)
            return booleanData.get(field).get();
        else
            return null;
    }

    public void set(DatabaseBooleanField field, Boolean value){
        booleanData.get(field).set(value);
    }

    public ObservableField<Boolean> getObservableObject(DatabaseBooleanField field){
        return booleanData.get(field);
    }

    public DatabaseHandler(String id, Map<String, Object> content){


    };
}
