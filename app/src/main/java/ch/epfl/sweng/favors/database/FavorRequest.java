package ch.epfl.sweng.favors.database;

import java.util.Map;

import ch.epfl.sweng.favors.database.fields.DatabaseField;

/**
 * The FavorRequest class is a convenient way to retrieve ObservableLists from the database
 * using certain criteria
 *
 * The need from this class arises when we want to display the user a list of nearby favors
 * Or favors with an upcoming deadline, etc.
 *
 * It is possible to specify how many favors to retrieve from the database and
 * which order they should abide
 */
public final class FavorRequest extends Request{

    private static final String TAG = "DB_FAVOR_REQUEST";
    private static final String COLLECTION = "favors";

    /**
     * Constructor as required by Firebase
     */
    private FavorRequest(){}

    /**
     * Retrieves a list from the database
     *
     * @param list the ObservableList to be populated
     * @param element Data type
     * @param value
     * @param limit maximal numbers of favors to retrieve
     * @param orderBy order to abide
     */
    public static void getList(ObservableArrayList<Favor> list, DatabaseField element, Object value, Integer limit, DatabaseField orderBy){
        db.getList(list, Favor.class, COLLECTION, element, value, limit, orderBy);
    }

    public static void all(ObservableArrayList<Favor> list, Integer limit, DatabaseField orderBy){
        db.getAll(list, Favor.class, COLLECTION, limit, orderBy);
    }

    public static void getList(ObservableArrayList<Favor> list, Map<DatabaseField, Object> mapEquals,
                               Map<DatabaseField, Object> mapLess,
                               Map<DatabaseField, Object> mapMore,
                               Integer limit,
                               DatabaseField orderBy){
        db.getList(list, Favor.class, COLLECTION, mapEquals, mapLess, mapMore, null, limit, orderBy);
    }

}

