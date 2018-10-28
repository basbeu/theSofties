package ch.epfl.sweng.favors.database;

import android.databinding.ObservableArrayList;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;

import ch.epfl.sweng.favors.authentication.Authentication;
import ch.epfl.sweng.favors.authentication.FakeAuthentication;
import ch.epfl.sweng.favors.database.fields.DatabaseField;
import ch.epfl.sweng.favors.database.fields.DatabaseStringField;
import ch.epfl.sweng.favors.main.FavorsMain;

import static ch.epfl.sweng.favors.main.FavorsMain.TAG;

public class FakeDatabase extends Database{

    public static FakeDatabase db = null;
    private HashMap<String, DatabaseEntity> database;

    private FakeDatabase(){
        database = new HashMap<>();
        //createBasicDatabase();
    }

    public static FakeDatabase getInstance(){
        if(db == null){
            db = new FakeDatabase();
        }

        return db;
    }

    @Override
    public void updateOnDb(DatabaseEntity databaseEntity) {
        if(databaseEntity.documentID != null){
            database.put(databaseEntity.documentID, databaseEntity.copy());
        }
        else{
            Log.e(TAG, "Trying to update data on an unknown document");
        }
    }

    @Override
    public Task updateFromDb(DatabaseEntity databaseEntity) {
        if(databaseEntity.documentID == null || !database.containsKey(databaseEntity.documentID)){return Tasks.forCanceled();}
        databaseEntity.updateLocalData(database.get(databaseEntity.documentID).getEncapsulatedObjectOfMaps());

        return Tasks.forResult(true);
    }

    @Override
    protected <T extends DatabaseEntity> ObservableArrayList<T> getAll(Class<T> clazz, String collection, Integer limit, DatabaseStringField orderBy) {
        return new ObservableArrayList<>();
    }

    @Override
    protected <T extends DatabaseEntity> ObservableArrayList<T> getList(Class<T> clazz, String collection, DatabaseField element, String value, Integer limit, DatabaseStringField orderBy) {
        return new ObservableArrayList<>();
    }

    public void createBasicDatabase(){
        User u1 = new User("U1");
        User u2 = new User("U2");
        User u3 = new User("U3");
        User u4 = new User(FakeAuthentication.UID);

        u1.set(User.StringFields.firstName, "Toto");
        u1.set(User.StringFields.lastName, "Lolo");
        u1.set(User.StringFields.email, "toto.lolo@test.com");
        u1.set(User.StringFields.city, "Tombouctou");
        User.UserGender.setGender(u1, User.UserGender.M);

        u2.set(User.StringFields.firstName, "Bruce");
        u2.set(User.StringFields.lastName, "Wayne");
        u2.set(User.StringFields.email, "bruce.wayne@waynecorp.com");
        u2.set(User.StringFields.city, "Gotham City");
        User.UserGender.setGender(u2, User.UserGender.M);

        u3.set(User.StringFields.firstName, "Harvey");
        u3.set(User.StringFields.lastName, "Dent");
        u3.set(User.StringFields.email, "harvey.dent@gotham.com");
        u3.set(User.StringFields.city, "Arkham Asylum");
        User.UserGender.setGender(u3, User.UserGender.M);

        u4.set(User.StringFields.firstName, FakeAuthentication.FIRST_NAME);
        u4.set(User.StringFields.lastName,FakeAuthentication.LAST_NAME);
        u4.set(User.StringFields.email, FakeAuthentication.EMAIL);
        u4.set(User.StringFields.city, FakeAuthentication.CITY);
        User.UserGender.setGender(u4, FakeAuthentication.GENDER);

        Favor f1 = new Favor("F1");
        Favor f2 = new Favor("F2");


        f1.set(Favor.StringFields.ownerID, "U3");
        f1.set(Favor.StringFields.category, "Hand help");
        f1.set(Favor.StringFields.deadline, "12.12.20");
        f1.set(Favor.StringFields.description, "I need help to get rid of an old friend.");
        f1.set(Favor.StringFields.title, "KILL THE BATMAN");
        f1.set(Favor.StringFields.locationCity, "Gotham City");

        f2.set(Favor.StringFields.ownerID, "U1");
        f2.set(Favor.StringFields.category, "Cooking");
        f2.set(Favor.StringFields.deadline, "10.01.19");
        f2.set(Favor.StringFields.description, "Cook me a cookie");
        f2.set(Favor.StringFields.title, "I am hungry pls hurry");
        f2.set(Favor.StringFields.locationCity, "Tombouctou");

        getInstance().updateOnDb(u1);
        getInstance().updateOnDb(u2);
        getInstance().updateOnDb(u3);
        getInstance().updateOnDb(u4);

        getInstance().updateOnDb(f1);
        getInstance().updateOnDb(f2);
    }
}
