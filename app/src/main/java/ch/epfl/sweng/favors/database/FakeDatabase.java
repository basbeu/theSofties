package ch.epfl.sweng.favors.database;

import android.databinding.ObservableArrayList;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;

import ch.epfl.sweng.favors.database.fields.DatabaseField;
import ch.epfl.sweng.favors.database.fields.DatabaseStringField;
import ch.epfl.sweng.favors.main.FavorsMain;

import static ch.epfl.sweng.favors.main.FavorsMain.TAG;

public class FakeDatabase extends Database{

    public static FakeDatabase db = null;
    private HashMap<String, DatabaseEntity> database;

    private FakeDatabase(){
        database = new HashMap<>();
    }

    public static FakeDatabase getInstance(){
        if(db == null){
            db = new FakeDatabase();
        }

        return db;
    }

    @Override
    public void updateOnDb(DatabaseEntity databaseEntity) {
        if(databaseEntity.documentID != null){
            database.put(databaseEntity.documentID, databaseEntity.copy());
        }
        else{
            Log.e(TAG, "Trying to update data on an unknown document");
        }
    }

    @Override
    public Task updateFromDb(DatabaseEntity databaseEntity) {
        if(databaseEntity.documentID == null || !database.containsKey(databaseEntity.documentID)){return Tasks.forCanceled();}
        databaseEntity.updateLocalData(database.get(databaseEntity.documentID).getEncapsulatedObjectOfMaps());
        return Tasks.forResult(true);
    }

    @Override
    protected <T extends DatabaseEntity> ObservableArrayList<T> getAll(Class<T> clazz, String collection, Integer limit, DatabaseStringField orderBy) {
        return new ObservableArrayList<>();
    }

    @Override
    protected <T extends DatabaseEntity> ObservableArrayList<T> getList(Class<T> clazz, String collection, DatabaseField element, String value, Integer limit, DatabaseStringField orderBy) {
        return new ObservableArrayList<>();
    }
}
