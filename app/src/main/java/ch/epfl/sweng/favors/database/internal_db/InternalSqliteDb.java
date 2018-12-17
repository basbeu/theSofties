package ch.epfl.sweng.favors.database.internal_db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

public final class InternalSqliteDb {
    private static SQLiteDatabase db;

    public static final String INTERNAL_DATABASE = "local_db";

    private InternalSqliteDb(){}

    public static void openDb(Context context){
        db = openOrCreateDatabase(context.getFilesDir()+INTERNAL_DATABASE,    null,null);
    }

    public static boolean isOpened(){
        return !(db == null);

    }
    public static SQLiteDatabase getInstance(){
        if(!isOpened())
            throw new IllegalStateException("Database is not opened or created");

        return db;
    }
}
