package ch.epfl.sweng.favors.database;

import android.databinding.ObservableArrayList;

import com.google.android.gms.tasks.Task;

import ch.epfl.sweng.favors.database.fields.DatabaseField;
import ch.epfl.sweng.favors.database.fields.DatabaseStringField;

public class FakeDatabase extends Database{

    @Override
    public void updateOnDb(DatabaseEntity databaseEntity) {

    }

    @Override
    public Task updateFromDb(DatabaseEntity databaseEntity) {
        return null;
    }

    @Override
    protected <T extends DatabaseEntity> ObservableArrayList<T> getAll(Class<T> clazz, String collection, Integer limit, DatabaseStringField orderBy) {
        return null;
    }

    @Override
    protected <T extends DatabaseEntity> ObservableArrayList<T> getList(Class<T> clazz, String collection, DatabaseField element, String value, Integer limit, DatabaseStringField orderBy) {
        return null;
    }
}
