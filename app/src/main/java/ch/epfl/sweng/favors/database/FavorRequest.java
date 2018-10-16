package ch.epfl.sweng.favors.database;


import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;

import java.util.ArrayList;

public final class FavorRequest extends Request{

    private static final String TAG = "DB_FAVOR_REQUEST";
    private static final String COLLECTION = "favors";

    private FavorRequest(){}
    public static ObservableArrayList<Favor> getList(DatabaseField element, String value, Integer limit, DatabaseStringField orderBy){
        return getList(Favor.class, COLLECTION, element, value, limit, orderBy);
    }
    public static ObservableArrayList<Favor> all(Integer limit, DatabaseStringField orderBy){
        return getAll(Favor.class, COLLECTION, limit, orderBy);
    }

}
