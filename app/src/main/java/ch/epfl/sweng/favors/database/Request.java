package ch.epfl.sweng.favors.database;

/**
 * Abstract class defining contract for Database requests
 */
public abstract class Request {

    private static final String TAG = "DB_REQUEST";
    protected static Database db = Database.getInstance();
}
