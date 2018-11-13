package ch.epfl.sweng.favors.database;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.util.Log;

import com.google.android.gms.tasks.Task;

import ch.epfl.sweng.favors.database.fields.DatabaseField;
import ch.epfl.sweng.favors.database.fields.DatabaseStringField;
import ch.epfl.sweng.favors.utils.ExecutionMode;

import static ch.epfl.sweng.favors.main.FavorsMain.TAG;

/**
 * Abstract class that represent a singleton for the database storage for the app
 */
public abstract class Database {

    private static Database db = null;

    /**
     * Method that update on the db a specified record
     * @param databaseEntity DatabaseEntity to update on the DB
     */
    public abstract void updateOnDb(DatabaseEntity databaseEntity);

    /**
     * Method that checkout the latest record for a DatabaseEntity
     * @param databaseEntity DatabaseEntity that must be updated with the db version
     * @return Task that update the DatabaseEntity
     */
    public abstract Task updateFromDb(DatabaseEntity databaseEntity);

    /**
     *
     * @param clazz The class that indicate the entity type <T>
     * @param collection Collection in which the entities must be found
     * @param limit The upper bound of the number of elements in the ArrayList
     * @param orderBy StringField that indicates the order in which the ArrayList should be returned
     * @param <T> Generic type that extends DatabaseEntity
     * @return An observable Arraylist of all the DatabaseEntity of type <T> in the database
     */
    protected abstract <T extends DatabaseEntity> ObservableArrayList<T> getAll(Class<T> clazz,
                                                                                String collection,
                                                                                Integer limit,
                                                                                DatabaseStringField orderBy);


    protected  <T extends DatabaseEntity> ObservableArrayList<T> getList(Class<T> clazz,
                                                                         String collection,
                                                                         DatabaseField element,
                                                                         String value,
                                                                         Integer limit,
                                                                         DatabaseStringField orderBy){
        ObservableArrayList<T> result = new ObservableArrayList<>();
        updateList(result, clazz, collection, element, value, limit, orderBy);
        return result;
    }

    protected  <T extends DatabaseEntity> T getElement(Class<T> clazz, String collection,
                                                       String value){
        try{
            T result = clazz.newInstance();
            updateElement(result, clazz,  collection, value);
            return result;
        }
        catch (Exception e) {
            Log.e(TAG, "Illegal access exception");
            return null;
        }

    }


    protected  <T extends DatabaseEntity> T getElement(Class<T> clazz, String collection,
                                                       DatabaseField element, String value){
        try{
            T result = clazz.newInstance();
            updateElement(result, clazz, collection, element, value);
            return result;
        }
        catch (Exception e){
            Log.e(TAG, "Illegal access exception");
            return null;
        }

    }

    protected  abstract <T extends DatabaseEntity> void updateList(ObservableArrayList<T> list, Class<T> clazz,
                                                          String collection,
                                                          DatabaseField element,
                                                          String value,
                                                          Integer limit,
                                                          DatabaseStringField orderBy);

    protected abstract <T extends DatabaseEntity> void updateElement(T toUpdate, Class<T> clazz, String collection,
                                                             String value);

    protected abstract <T extends DatabaseEntity> void updateElement(T toUpdate, Class<T> clazz, String collection,
                                                             DatabaseField element, String value);

    /**
     * @return Database that is the DB for the current session
     */
    public static Database getInstance(){
        if(db == null){
            if(ExecutionMode.getInstance().isTest()){
                db = FakeDatabase.getInstance();
            }
            else{
                db = FirebaseDatabase.getInstance();
            }
        }
        return db;
    }

    public abstract void cleanUp();

    public static void cleanUpAll(){
        if(db != null){
            db.cleanUp();
            db = null;
        }
    }
}
