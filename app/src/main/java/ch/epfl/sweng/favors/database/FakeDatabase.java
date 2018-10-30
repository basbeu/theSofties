package ch.epfl.sweng.favors.database;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.util.HashMap;

import ch.epfl.sweng.favors.authentication.FakeAuthentication;
import ch.epfl.sweng.favors.database.fields.DatabaseField;
import ch.epfl.sweng.favors.database.fields.DatabaseStringField;

import static ch.epfl.sweng.favors.main.FavorsMain.TAG;

/**
 * Singleton class that represent a fake DB for testing purpose
 */
public class FakeDatabase extends Database{

    public static final String LAST_FAVOR_TITLE = "Unify String Theroy";
    public static FakeDatabase db = null;
    private HashMap<String, DatabaseEntity> database;

    private FakeDatabase(){
        database = new HashMap<>();
    }

    /**
     * @return The current FakeDatabase or a new one if not yet instantiated
     */
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
        ObservableArrayList<T> list = new ObservableArrayList<>();


        Handler handler = new Handler();
        handler.postDelayed(()->{
                Log.d(TAG, "getAll called : "+ clazz.toString());
                switch (clazz.toString()){
                    case "class ch.epfl.sweng.favors.database.Interest":
                        Log.d(TAG, "Adding Intrest elements to fake DB ObservableList");
                        addToList(clazz,(T)database.get("I1"),list);
                        addToList(clazz,(T)database.get("I2"),list);
                        addToList(clazz,(T)database.get("I3"),list);
                        break;
                    case "class ch.epfl.sweng.favors.database.Favor":
                        Log.d(TAG, "Adding Favor elements to fake DB ObservableList");
                        addToList(clazz,(T)database.get("F1"),list);
                        addToList(clazz,(T)database.get("F2"),list);
                        addToList(clazz,(T)database.get("F3"),list);
                }
            },500);
        return list;
    }

    private  <T extends DatabaseEntity>  void addToList(Class<T> clazz, T document,ObservableArrayList<T> list){
        try{
            T documentObject = clazz.newInstance();
            documentObject.set(document.documentID, document.getEncapsulatedObjectOfMaps());
            list.add(documentObject);
        }
        catch (Exception e){
            Log.e(TAG, "Illegal access exception");
        }
    }

    @Override
    protected <T extends DatabaseEntity> ObservableArrayList<T> getList(Class<T> clazz, String collection, DatabaseField element, String value, Integer limit, DatabaseStringField orderBy) {
        return new ObservableArrayList<>();
    }

    /**
     * Fills the FakeDatabase with a few users and favors
     */
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
        Favor f3 = new Favor("F3");
        Favor f4 = new Favor("F4");
        Favor f5 = new Favor("F5");
        Favor f6 = new Favor("F6");

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

        f3.set(Favor.StringFields.ownerID, "U3");
        f3.set(Favor.StringFields.category, "Riddle");
        f3.set(Favor.StringFields.deadline, "12.12.20");
        f3.set(Favor.StringFields.description, "We're five little items of an everyday sort; you'll find us all in 'a tennis court'");
        f3.set(Favor.StringFields.title, "TICK TOK");
        f3.set(Favor.StringFields.locationCity, "Gotham City");

        f4.set(Favor.StringFields.ownerID, "U3");
        f4.set(Favor.StringFields.category, "Homework");
        f4.set(Favor.StringFields.deadline, "12.12.20");
        f4.set(Favor.StringFields.description, "A bit of help with physics");
        f4.set(Favor.StringFields.title, "Unify String Theroy");
        f4.set(Favor.StringFields.locationCity, "Caltech");

        Interest i1 = new Interest("I1");
        Interest i2 = new Interest("I2");
        Interest i3 = new Interest("I3");


        i1.set(Interest.StringFields.title, "DC");
        i1.set(Interest.StringFields.description, "DC Comics");

        i2.set(Interest.StringFields.title, "MARVEL");
        i2.set(Interest.StringFields.description, "Marvel Comics");


        i3.set(Interest.StringFields.title, "SWISS AVIATION");
        i3.set(Interest.StringFields.description, "Fly to the sky");

        getInstance().updateOnDb(i1);
        getInstance().updateOnDb(i2);
        getInstance().updateOnDb(i3);


        getInstance().updateOnDb(u1);
        getInstance().updateOnDb(u2);
        getInstance().updateOnDb(u3);
        getInstance().updateOnDb(u4);

        getInstance().updateOnDb(f1);
        getInstance().updateOnDb(f2);
        getInstance().updateOnDb(f3);
        getInstance().updateOnDb(f4);
    }
}
