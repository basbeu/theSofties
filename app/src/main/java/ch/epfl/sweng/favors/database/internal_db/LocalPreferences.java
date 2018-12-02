package ch.epfl.sweng.favors.database.internal_db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import ch.epfl.sweng.favors.authentication.Authentication;

public class LocalPreferences {
    private static LocalPreferences instance;

    private static final String TABLE_NAME = "LocalPreferences";
    private static final String PK_PREFERENCES = "PK_preferences";
    private static final String FIELD_UID = "UID";
    private static final String FIELD_EMAIL_NOTIF = "EmailNotif";

    private boolean emailNofifEnabled;

    private LocalPreferences(){
        String Uid = Authentication.getInstance().getUid();

        SQLiteDatabase db = InternalSqliteDb.getInstance();
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" + PK_PREFERENCES + " INTEGER PRIMARY KEY," + FIELD_UID + " VARCHAR," + FIELD_EMAIL_NOTIF + " BOOLEAN);");
        db.execSQL("INSERT OR IGNORE INTO " + TABLE_NAME + "(" + FIELD_UID + ", " + FIELD_EMAIL_NOTIF + ") VALUES ('" + Uid + "', 1);");

        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE "+ FIELD_UID + " = '" + Uid+"';",null);
        res.moveToFirst();
        int index = res.getColumnIndex(FIELD_EMAIL_NOTIF);
        emailNofifEnabled = res.getInt(index) > 0;
    }

    public static LocalPreferences getInstance(){
        if(instance == null) {
            instance = new LocalPreferences();
        }

        return instance;
    }

    public boolean isEmailNofifEnabled() {
        return emailNofifEnabled;
    }

    public void setEmailNofifEnabled(boolean emailNofifEnabled) {
        this.emailNofifEnabled = emailNofifEnabled;

        String Uid = Authentication.getInstance().getUid();
        SQLiteDatabase db = InternalSqliteDb.getInstance();
        db.execSQL("UPDATE " + TABLE_NAME + " SET " + FIELD_EMAIL_NOTIF + " = " + (emailNofifEnabled ? 1:0) + " WHERE " + FIELD_UID + "='" + Uid + "';");
    }
}
