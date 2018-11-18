package ch.epfl.sweng.favors.database;

import ch.epfl.sweng.favors.database.fields.DatabaseField;
import ch.epfl.sweng.favors.database.fields.DatabaseStringField;

public final class InterestRequest extends Request{
    private static final String TAG = "DB_INTEREST_REQUEST";
    private static final String COLLECTION = "interests";

    private InterestRequest(){}
    public static void getList(ObservableArrayList<Interest>list, DatabaseField element, Object value, Integer limit, DatabaseField orderBy){
        db.getList(list, Interest.class, COLLECTION, element, value, limit, orderBy);
    }
    public static void all(ObservableArrayList<Interest> list, Integer limit, DatabaseField orderBy){
        db.getAll(list, Interest.class, COLLECTION, limit, orderBy);
    }

}
