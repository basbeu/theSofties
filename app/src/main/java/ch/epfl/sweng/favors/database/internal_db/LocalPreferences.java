package ch.epfl.sweng.favors.database.internal_db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import ch.epfl.sweng.favors.R;
import ch.epfl.sweng.favors.authentication.Authentication;

public class LocalPreferences {
    private static LocalPreferences instance;

    private static final String TABLE_NAME = "LocalPreferences";
    private static final String PK_PREFERENCES = "PK_preferences";
    private static final String FIELD_UID = "UID";
    private static final String FIELD_EMAIL_NOTIF = "EmailNotif";
    private static final String FIELD_APP_NOTIF = "AppNotif";
    private static final String FIELD_COLOR = "Color";

    private boolean emailNofifEnabled;
    private boolean appNotifEnabled;
    private int color;

    private LocalPreferences(){
        String Uid = Authentication.getInstance().getUid();

        SQLiteDatabase db = InternalSqliteDb.getInstance();
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" + PK_PREFERENCES + " INTEGER PRIMARY KEY," + FIELD_UID + " VARCHAR," + FIELD_EMAIL_NOTIF + " BOOLEAN DEFAULT 1, "+ FIELD_APP_NOTIF + " BOOLEAN DEFAULT 1, "+FIELD_COLOR+ " INTEGER);");

        Cursor res;
        do{
            res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE "+ FIELD_UID + " = '" + Uid+"';",null);
            if(res.getCount() == 0){
                db.execSQL("INSERT OR IGNORE INTO " + TABLE_NAME + "(" + FIELD_UID +", "+FIELD_COLOR+") VALUES ('" + Uid + "',"+ R.color.colorToolbar+");");
            }
        }while(res.getCount() == 0);

        res.moveToFirst();
        emailNofifEnabled = getBoolean(res,res.getColumnIndex(FIELD_EMAIL_NOTIF));
        appNotifEnabled = getBoolean(res,res.getColumnIndex(FIELD_APP_NOTIF));
        color = res.getInt(res.getColumnIndex(FIELD_COLOR));
    }

    public static LocalPreferences getInstance(){
        if(instance == null) {
            instance = new LocalPreferences();
        }

        return instance;
    }

    public static void closeInstance(){
        instance = null;
    }

    public boolean isEmailNofifEnabled() {
        return emailNofifEnabled;
    }

    public void setEmailNofifEnabled(boolean emailNofifEnabled) {
        this.emailNofifEnabled = emailNofifEnabled;
        updateBooleanField(FIELD_EMAIL_NOTIF,emailNofifEnabled);
    }

    public boolean isAppNotifEnabled() {
        return appNotifEnabled;
    }

    public void setAppNotifEnabled(boolean appNotifEnabled) {
        this.appNotifEnabled = appNotifEnabled;
        updateBooleanField(FIELD_APP_NOTIF,appNotifEnabled);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        String Uid = Authentication.getInstance().getUid();
        SQLiteDatabase db = InternalSqliteDb.getInstance();
        db.execSQL("UPDATE " + TABLE_NAME + " SET " + FIELD_COLOR + " = " + color + " WHERE " + FIELD_UID + "='" + Uid + "';");
    }

    private boolean getBoolean(Cursor res, int index){
        return res.getInt(index) > 0;
    }

    private void updateBooleanField(String field, Boolean value){
        String Uid = Authentication.getInstance().getUid();
        SQLiteDatabase db = InternalSqliteDb.getInstance();
        db.execSQL("UPDATE " + TABLE_NAME + " SET " + field + " = " + (value ? 1:0) + " WHERE " + FIELD_UID + "='" + Uid + "';");
    }

}
