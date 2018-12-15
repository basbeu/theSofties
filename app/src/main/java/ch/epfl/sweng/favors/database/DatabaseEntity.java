package ch.epfl.sweng.favors.database;

import android.databinding.Observable;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.favors.database.fields.DatabaseBooleanField;
import ch.epfl.sweng.favors.database.fields.DatabaseField;
import ch.epfl.sweng.favors.database.fields.DatabaseLongField;
import ch.epfl.sweng.favors.database.fields.DatabaseObjectField;
import ch.epfl.sweng.favors.database.fields.DatabaseStringField;


public abstract class DatabaseEntity implements Observable {

    protected static Database db = Database.getInstance();

    protected Map<DatabaseStringField, ObservableField<String>> stringData;
    protected Map<DatabaseLongField, ObservableField<Long>> longData;
    protected Map<DatabaseBooleanField, ObservableField<Boolean>> booleanData;
    protected Map<DatabaseObjectField, ObservableField<Object>> objectData;

    protected final String collection;
    protected String documentID;

    public enum UpdateType{DATA, FROM_DB, FROM_REQUEST}

    private final static String TAG = "Favors_DatabaseHandler";

    public void setId(String documentId){
        this.documentID = documentId;
    }

    public String getId() {return documentID;}

    /**
     * Init a database object with all fields that are possible for the instanced collection in the database
     *
     * @param stringFieldsValues The possibles names of string objects in the database
     * @param longFieldsValues The possibles names of int objects in the database
     * @param booleanFieldsValues The possibles names of boolean objects in the database
     * @param objectFieldsValues The possibles names of generic objects in the database that must be test
     *                           later, often tables
     * @param collection The collection in the database
     * @param documentID If so, the doccumentId in the database
     */
    public DatabaseEntity(DatabaseStringField stringFieldsValues[], DatabaseLongField longFieldsValues[],
                          DatabaseBooleanField booleanFieldsValues[], DatabaseObjectField objectFieldsValues[],
                          String collection, String documentID){

        assert(collection != null);

        stringData = initMap(stringFieldsValues);
        longData = initMap(longFieldsValues);
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
    private <T extends DatabaseField, V> Map<T, ObservableField<V>>  initMap(final T[] possibleValues){
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
     * Return a uniform map with all data to send
     *
     * @return The maps with objects names and values
     */
    protected Map<String, Object> getEncapsulatedObjectOfMaps(){
        Map<String, Object> toSend = new HashMap<>();

        convertTypedMapToObjectMap(stringData, toSend);
        convertTypedMapToObjectMap(booleanData, toSend);
        convertTypedMapToObjectMap(longData, toSend);
        convertTypedMapToObjectMap(objectData, toSend);

        return toSend;
    }

    /**
     * Update local data with a generic content with Objects
     *
     * @param incommingData The map with object content and object value
     * @return True is successfull
     */
    protected void updateLocalData(Map<String, Object> incommingData){
        if(incommingData == null){
            return;
        }
        convertObjectMapToTypedMap(incommingData, stringData, String.class);
        convertObjectMapToTypedMap(incommingData, booleanData, Boolean.class);
        convertObjectMapToTypedMap(incommingData, objectData, Object.class);
        convertObjectMapToTypedMap(incommingData, longData, Long.class);
        for (OnPropertyChangedCallback callback : callbacks){
            callback.onPropertyChanged(this, UpdateType.FROM_DB.ordinal());
        }

        notifyContentChange();
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
    private <T extends DatabaseField, V , U extends ObservableField<V>> void convertObjectMapToTypedMap(Map<String, Object> from, Map<T, U> to, Class<V> clazz) {
        if(from == null || to == null){return;}
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
        if(from == null || to == null){return;}
        for (Map.Entry<T, U> entry : from.entrySet()){
            V value = (V) entry.getValue().get();
            if(value != null) to.put(entry.getKey().toString(), value);
        }
    }

    public void reset(){
        if(stringData != null)
            resetMap(stringData, null);
        if(booleanData != null)
            resetMap(booleanData, null);
        if(objectData != null)
            resetMap(objectData, null);
        if(longData != null)
            resetMap(longData,null);
        notifyContentChange();
    }

    /**
     * Clears a map of all the data that is contained in it and replaces it with the default value passed in.
     *
     * @param map Map that needs to be cleared of information
     * @param defaultValue What value should be placed in the map when being cleared
     * @param <K> Key type of the map
     * @param <V> Value contained in the observableField of the map
     */
    protected  <K,V> void resetMap(@NonNull Map<K, ObservableField<V>> map, V defaultValue) {
        for(K key : map.keySet()){
            map.get(key).set(defaultValue);
        }
    }

    List<OnPropertyChangedCallback> callbacks = new ArrayList<>();
    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        assert(callback != null);
        callbacks.add(callback);
    }
    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        callbacks.remove(callback);
    }
    private void notifyContentChange(){
        for (OnPropertyChangedCallback callback : callbacks){
            callback.onPropertyChanged(this, UpdateType.DATA.ordinal());
        }
    }

    public void set(String id, Map<String, Object> content){
        this.documentID =id;
        this.updateLocalData(content);
        for (OnPropertyChangedCallback callback : callbacks){
            callback.onPropertyChanged(this, UpdateType.FROM_REQUEST.ordinal());
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
        notifyContentChange();
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
        notifyContentChange();
    }

    public ObservableField<Object> getObservableObject(DatabaseObjectField field){
        return objectData.get(field);
    }

    public Long get(DatabaseLongField field){
        if(longData.get(field) != null)
            return longData.get(field).get();
        else
            return null;
    }

    public void set(DatabaseLongField field, Long value){
        longData.get(field).set(value);
        notifyContentChange();
    }

    public ObservableField<Long> getObservableObject(DatabaseLongField field){
        return longData.get(field);
    }

    public Boolean get(DatabaseBooleanField field){
        if(booleanData.get(field) != null)
            return booleanData.get(field).get();
        else
            return null;
    }

    public void set(DatabaseBooleanField field, Boolean value){
        booleanData.get(field).set(value);
        notifyContentChange();
    }

    public ObservableField<Boolean> getObservableObject(DatabaseBooleanField field){
        return booleanData.get(field);
    }

    public abstract DatabaseEntity copy();

}
