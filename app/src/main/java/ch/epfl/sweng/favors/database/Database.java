package ch.epfl.sweng.favors.database;

import android.databinding.ObservableArrayList;

import com.google.android.gms.tasks.Task;

import ch.epfl.sweng.favors.database.fields.DatabaseField;
import ch.epfl.sweng.favors.database.fields.DatabaseStringField;
import ch.epfl.sweng.favors.utils.ExecutionMode;

public abstract class Database {

    private static Database db = null;

    public abstract void updateOnDb(DatabaseEntity databaseEntity);
    public abstract Task updateFromDb(DatabaseEntity databaseEntity);
    protected abstract <T extends DatabaseEntity> ObservableArrayList<T> getAll(Class<T> clazz,
                                                                                String collection,
                                                                                Integer limit,
                                                                                DatabaseStringField orderBy);
    protected abstract <T extends DatabaseEntity> ObservableArrayList<T> getList(Class<T> clazz,
                                                                                 String collection,
                                                                                 DatabaseField element,
                                                                                 String value,
                                                                                 Integer limit,
                                                                                 DatabaseStringField orderBy);


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

}
