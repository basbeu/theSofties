package ch.epfl.sweng.favors.database;

import android.util.Log;

import ch.epfl.sweng.favors.database.fields.DatabaseBooleanField;
import ch.epfl.sweng.favors.database.fields.DatabaseIntField;
import ch.epfl.sweng.favors.database.fields.DatabaseObjectField;
import ch.epfl.sweng.favors.database.fields.DatabaseStringField;

public class ApiKeys extends DatabaseEntity {
    public enum StringFields implements DatabaseStringField {mailGun}
    public enum IntegerFields implements DatabaseIntField {}
    public enum ObjectFields implements DatabaseObjectField {}
    public enum BooleanFields implements DatabaseBooleanField {}

    private static String COLLECTION = "apiKeys";
    private static String DOC_ID = "keys";
    private static String TAG = "API_KEYS";

    private static ApiKeys mInstance;

    private ApiKeys() {
        super(ApiKeys.StringFields.values(), ApiKeys.IntegerFields.values(), ApiKeys.BooleanFields.values(),
                ApiKeys.ObjectFields.values(), COLLECTION,DOC_ID);
        Log.d(TAG, "Creating new api key object");
        db.updateFromDb(this);
        if(mInstance != null) throw new UnsupportedOperationException("Instance already exists");
    }

    public static synchronized ApiKeys getInstance(){
        if (mInstance == null) {
            mInstance = new ApiKeys();
        }
        return mInstance;
    }


    @Override
    public DatabaseEntity copy() {
        return this;
    }
}