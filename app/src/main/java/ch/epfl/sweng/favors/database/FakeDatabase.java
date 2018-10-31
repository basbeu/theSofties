package ch.epfl.sweng.favors.database;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.os.Handler;
import android.support.annotation.IntRange;
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

    public static final String LAST_FAVOR_TITLE = "Flash needs some help";
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
                        addToList(clazz,(T)database.get("I4"),list);
                        addToList(clazz,(T)database.get("I5"),list);
                        break;
                    case "class ch.epfl.sweng.favors.database.Favor":
                        Log.d(TAG, "Adding Favor elements to fake DB ObservableList");
                        addToList(clazz,(T)database.get("F1"),list);
                        addToList(clazz,(T)database.get("F2"),list);
                        addToList(clazz,(T)database.get("F3"),list);
                        addToList(clazz,(T)database.get("F4"),list);
                        addToList(clazz,(T)database.get("F5"),list);
                        addToList(clazz,(T)database.get("F6"),list);
                        addToList(clazz,(T)database.get("F7"),list);
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
        Favor f7 = new Favor("F7");
        Favor f8 = new Favor("F8");
        Favor f9 = new Favor("F9");
        Favor f10 = new Favor("F10");


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

        f5.set(Favor.StringFields.ownerID, "U3");
        f5.set(Favor.StringFields.category, "Save the World");
        f5.set(Favor.StringFields.deadline, "12.12.20");
        f5.set(Favor.StringFields.description, "Help me stop Lex Luther");
        f5.set(Favor.StringFields.title, "Help Superman");
        f5.set(Favor.StringFields.locationCity, "US");

        f6.set(Favor.StringFields.ownerID, "U3");
        f6.set(Favor.StringFields.category, "Assasin");
        f6.set(Favor.StringFields.deadline, "12.12.20");
        f6.set(Favor.StringFields.description, "Stop the League of Assassins from overtaking the city");
        f6.set(Favor.StringFields.title, "League Help");
        f6.set(Favor.StringFields.locationCity, "DC world");

        f7.set(Favor.StringFields.ownerID, "U3");
        f7.set(Favor.StringFields.category, "The Flash");
        f7.set(Favor.StringFields.deadline, "12.12.20");
        f7.set(Favor.StringFields.description, "Stop Salazar from detroying the world");
        f7.set(Favor.StringFields.title, "Flash needs some help");
        f7.set(Favor.StringFields.locationCity, "DC world");


        f8.set(Favor.StringFields.ownerID, "U3");
        f8.set(Favor.StringFields.category, "Supergirl");
        f8.set(Favor.StringFields.deadline, "12.12.20");
        f8.set(Favor.StringFields.description, "Destroy kryptoninite on earth");
        f8.set(Favor.StringFields.title, "make use gods again");
        f8.set(Favor.StringFields.locationCity, "DC world");

        f9.set(Favor.StringFields.ownerID, "U3");
        f9.set(Favor.StringFields.category, "The Breacher");
        f9.set(Favor.StringFields.deadline, "12.12.20");
        f9.set(Favor.StringFields.description, "Improve my Vibe glasses");
        f9.set(Favor.StringFields.title, "Flash needs some help");
        f9.set(Favor.StringFields.locationCity, "DC world");

        f10.set(Favor.StringFields.ownerID, "U3");
        f10.set(Favor.StringFields.category, "The Legends");
        f10.set(Favor.StringFields.deadline, "12.12.20");
        f10.set(Favor.StringFields.description, "Fix a time anomaly in 300BC");
        f10.set(Favor.StringFields.title, "Stop cesar surviving the assasinaton.");
        f10.set(Favor.StringFields.locationCity, "The Timeline");


        Interest i1 = new Interest("I1");
        Interest i2 = new Interest("I2");
        Interest i3 = new Interest("I3");
        Interest i4 = new Interest("I4");
        Interest i5 = new Interest("I5");


        i1.set(Interest.StringFields.title, "DC");
        i1.set(Interest.StringFields.description, "DC Comics");

        i2.set(Interest.StringFields.title, "MARVEL");
        i2.set(Interest.StringFields.description, "Marvel Comics");


        i3.set(Interest.StringFields.title, "SWISS AVIATION");
        i3.set(Interest.StringFields.description, "Fly to the sky");

        i4.set(Interest.StringFields.title, "Marvel comics");
        i4.set(Interest.StringFields.description, "Comics are great");

        i5.set(Interest.StringFields.title, "Edelweiss");
        i5.set(Interest.StringFields.description, "Yet another great airline");

        getInstance().updateOnDb(i1);
        getInstance().updateOnDb(i2);
        getInstance().updateOnDb(i3);
        getInstance().updateOnDb(i4);
        getInstance().updateOnDb(i5);


        getInstance().updateOnDb(u1);
        getInstance().updateOnDb(u2);
        getInstance().updateOnDb(u3);
        getInstance().updateOnDb(u4);

        getInstance().updateOnDb(f1);
        getInstance().updateOnDb(f2);
        getInstance().updateOnDb(f3);
        getInstance().updateOnDb(f4);
        getInstance().updateOnDb(f5);
        getInstance().updateOnDb(f6);
        getInstance().updateOnDb(f7);
        getInstance().updateOnDb(f8);
        getInstance().updateOnDb(f9);
        getInstance().updateOnDb(f10);

    }

    /**
     * This method can be used to add extra elements to the DB.
     */
    public void addExtraToDb(){
        Favor fNew = new Favor("newFavor");
        Favor fNew2 = new Favor("newFavor2");

        fNew.set(Favor.StringFields.ownerID, "U3");
        fNew.set(Favor.StringFields.category, "Test Writter");
        fNew.set(Favor.StringFields.deadline, "12.12.20");
        fNew.set(Favor.StringFields.description, "Hel me get to 80% test coverage");
        fNew.set(Favor.StringFields.title, "help with tests");
        fNew.set(Favor.StringFields.locationCity, "EPFL");

        fNew2.set(Favor.StringFields.ownerID, "U3");
        fNew2.set(Favor.StringFields.category, "Hack The Blue Smurf Institute");
        fNew2.set(Favor.StringFields.deadline, "12.12.20");
        fNew2.set(Favor.StringFields.description, "We all want to be blue");
        fNew2.set(Favor.StringFields.title, "Smurf, Smurf, more Smurf");
        fNew2.set(Favor.StringFields.locationCity, "Smurf World");

        User uNew = new User("uNew");
        User uNew2 = new User("uNew2");

        uNew.set(User.StringFields.firstName, "Oliver");
        uNew.set(User.StringFields.lastName, "Queen");
        uNew.set(User.StringFields.email, "oliver.queen@queencorp.com");
        uNew.set(User.StringFields.city, "Starling City");
        User.UserGender.setGender(uNew, User.UserGender.M);

        uNew2.set(User.StringFields.firstName, "Barry");
        uNew2.set(User.StringFields.lastName, "Allen");
        uNew2.set(User.StringFields.email, "barry.allen@ccpd.com");
        uNew2.set(User.StringFields.city, "Starling City");
        User.UserGender.setGender(uNew2, User.UserGender.M);

        Interest iNew = new Interest("iNew");
        Interest iNew2 = new Interest("iNew2");

        iNew.set(Interest.StringFields.title, "DC");
        iNew.set(Interest.StringFields.description, "DC Comics");

        iNew.set(Interest.StringFields.title, "Speedforce");
        iNew.set(Interest.StringFields.description, "DC Comics");

        getInstance().updateOnDb(uNew);
        getInstance().updateOnDb(uNew2);
        getInstance().updateOnDb(iNew);
        getInstance().updateOnDb(iNew2);
        getInstance().updateOnDb(fNew);
        getInstance().updateOnDb(fNew2);
    }

    /**
     * This is the tear down method of addExtra method.
     */
    public void removeExtraFromDB(){
        database.remove("iNew");
        database.remove("iNew2");
        database.remove("uNew");
        database.remove("uNew2");
        database.remove("newFavor");
        database.remove("newFavor2");
    }

    /**
     * This empties the fakeDatabase. Allows for proper teardown of tests.
     */
    public void cleanUp(){
        database = new HashMap<>();
    }
}
