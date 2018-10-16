package ch.epfl.sweng.favors.database;

import android.databinding.ObservableArrayList;

public final class InterestRequest extends Request{
    private static final String TAG = "DB_INTEREST_REQUEST";
    private static final String COLLECTION = "interests";

    private InterestRequest(){}
    public static ObservableArrayList<Interest> getList(DatabaseField element, String value, Integer limit, DatabaseStringField orderBy){
        return getList(Interest.class, COLLECTION, element, value, limit, orderBy);
    }
    public static ObservableArrayList<Interest> all(Integer limit, DatabaseStringField orderBy){
        return getAll(Interest.class, COLLECTION, limit, orderBy);
    }


}
