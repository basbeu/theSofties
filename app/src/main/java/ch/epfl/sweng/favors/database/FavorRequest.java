package ch.epfl.sweng.favors.database;

import java.util.Map;

import ch.epfl.sweng.favors.database.fields.DatabaseField;
import ch.epfl.sweng.favors.database.fields.DatabaseStringField;

public final class FavorRequest extends Request{

    private static final String TAG = "DB_FAVOR_REQUEST";
    private static final String COLLECTION = "favors";

    private FavorRequest(){}
    public static void getList(ObservableArrayList<Favor> list, DatabaseField element, Object value, Integer limit, DatabaseField orderBy){
        db.getList(list, Favor.class, COLLECTION, element, value, limit, orderBy);
    }

    public static void all(ObservableArrayList<Favor> list, Integer limit, DatabaseField orderBy){
        db.getAll(list, Favor.class, COLLECTION, limit, orderBy);
    }

    public static void getList(ObservableArrayList<Favor> list, Map<DatabaseField, Object> map, Integer limit, DatabaseField orderBy){
        db.getList(list, Favor.class, COLLECTION, map, limit, orderBy);
    }

    /*public static void getWithId(Favor favor, String value){
        db.getElement(favor, Favor.class, COLLECTION, value);
    }*/

}

