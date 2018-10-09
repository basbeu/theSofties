package ch.epfl.sweng.favours.database;

import android.databinding.ObservableField;

import java.util.HashMap;
import java.util.Map;


public abstract class DatabaseHandler {

    public DatabaseHandler(DatabaseStringField fields[]){
        stringData = new HashMap<DatabaseStringField, ObservableField<String>>(){
            {
                for(DatabaseStringField field : fields){
                    this.put(field, new ObservableField<String>());
                }
            }

        };;

        intData = new HashMap<>();
    }

    Map<DatabaseStringField, ObservableField<String>> stringData;
    Map<DatabaseIntField, ObservableField<Integer>> intData;

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



    /*private void parseStringData(Map<String, Object> data){
        for(StringField fieldName : stringData.values()){
            if(data.get(fieldName.toString()) instanceof String){
                set(fieldName, (String) data.get(fieldName.toString()));
            }
        }
    }*/


    /**
     * Convert the map containing some parameters in an String / Object map
     * @param from  The original map to convert
     * @param to    The map where to add objects
     * @param <T>   The enum of map content
     * @param <V>   The ObservableField content type
     * @param <U>   The ObservableField
     */
    <T extends DatabaseField, V, U extends ObservableField<V>> void convertTypedMapToObjectMap(Map<T, U> from, Map<String, Object> to) {
        for (Map.Entry<T, U> entry : from.entrySet()){
            V value = (V) entry.getValue().get();
            if(value != null) to.put(entry.getKey().toString(), value);
        }
    }

}
