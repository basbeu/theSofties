package ch.epfl.sweng.favors.database;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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

    HandlerThread handlerThread = new HandlerThread("background-thread");

    private FakeDatabase(){
        database = new HashMap<>();
        handlerThread.start();
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
            int leftLimit = 97; // letter 'a'
            int rightLimit = 122; // letter 'z'
            int targetStringLength = 5;
            Random random = new Random();
            StringBuilder buffer = new StringBuilder(targetStringLength);
            for (int i = 0; i < targetStringLength; i++) {
                int randomLimitedInt = leftLimit + (int)
                        (random.nextFloat() * (rightLimit - leftLimit + 1));
                buffer.append((char) randomLimitedInt);
            }
            String generatedString = buffer.toString();
            database.put( generatedString, databaseEntity.copy());
        }

    }

    @Override
    public Task updateFromDb(DatabaseEntity databaseEntity) {
        if(databaseEntity.documentID == null || !database.containsKey(databaseEntity.documentID)){return Tasks.forCanceled();}
        databaseEntity.updateLocalData(database.get(databaseEntity.documentID).getEncapsulatedObjectOfMaps());

        return Tasks.forResult(true);
    }

    @Override
    public void deleteFromDatabase(DatabaseEntity databaseEntity) {
       if(databaseEntity == null) return;
        database.remove(databaseEntity.documentID);
    }

    @Override
    protected <T extends DatabaseEntity> void getAll(ObservableArrayList<T> list, Class<T> clazz, String collection, Integer limit, DatabaseField orderBy) {

        Handler handler = new Handler(handlerThread.getLooper());
        handler.postDelayed(()->{
            ArrayList<T> tempList = new ArrayList<>();
            for(DatabaseEntity entity : database.values()) {
                if (clazz.isInstance(entity)) {
                    try {
                        T temp = clazz.newInstance();
                        temp.set(entity.documentID, entity.getEncapsulatedObjectOfMaps());
                        tempList.add(temp);

                    } catch (Exception e){
                        Log.e(TAG, "Illegal access exception");
                    }
                }

            }
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    list.clear();
                    list.addAll(tempList);
                }
            });


        },500);
    }

    /*private  <T extends DatabaseEntity>  void addToList(Class<T> clazz, T document,ObservableArrayList<T> list){
        try{
            T documentObject = clazz.newInstance();
            documentObject.set(document.documentID, document.getEncapsulatedObjectOfMaps());
            list.add(documentObject);
        }
        catch (Exception e){
            Log.e(TAG, "Illegal access exception");
        }
    }*/


    /**
     * Updates a list with the all elements of the database having the @value for the @key
     *
     * @param list The list to fulfill
     * @param clazz The object type
     * @param collection Not used here, only for real db interactions
     * @param value The value we want to have for the key
     * @param <T> The object type
     */
    @Override
    protected  <T extends DatabaseEntity> void getList(ObservableArrayList<T> list, Class<T> clazz,
                                                          String collection,
                                                          DatabaseField key,
                                                          Object value,
                                                          Integer limit,
                                                          DatabaseField orderBy){
        Handler handler = new Handler(handlerThread.getLooper());
        handler.postDelayed(()->{
            ArrayList<T> tempList = new ArrayList<>();
            for(DatabaseEntity entity : database.values()) {
                if (clazz.isInstance(entity) && value instanceof String && entity.get((DatabaseStringField) key).equals(value)) {
                    try {
                        T temp = clazz.newInstance();
                        temp.set(entity.documentID, entity.getEncapsulatedObjectOfMaps());
                        tempList.add(temp);
                    } catch (Exception e){
                        Log.e(TAG, "Illegal access exception");
                    }
                }
            }
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    list.clear();
                    list.addAll(tempList);
                }
            });

        },500);
    }

    @Override
    protected  <T extends DatabaseEntity>  void getList(ObservableArrayList<T> list, Class<T> clazz,
                                                   String collection,
                                                   Map<DatabaseField, Object> mapEquals,
                                                   Map<DatabaseField, Object> mapLess,
                                                   Map<DatabaseField, Object> mapMore,
                                                   Integer limit,
                                                   DatabaseField orderBy){
        Handler handler = new Handler(handlerThread.getLooper());
        handler.postDelayed(()->{
            ArrayList<T> tempList = new ArrayList<>();
            for(DatabaseEntity entity : database.values()) {
                Boolean toAdd = true;
                for(Map.Entry<DatabaseField, Object> el : mapEquals.entrySet()) {
                    if (!(clazz.isInstance(entity) && el.getValue() instanceof String && entity.get((DatabaseStringField) el.getKey()).equals(el.getValue()))) {
                        toAdd = false;
                        break;
                    }
                }
                if (toAdd) {
                    try {
                        T temp = clazz.newInstance();
                        temp.set(entity.documentID, entity.getEncapsulatedObjectOfMaps());
                        tempList.add(temp);
                    } catch (Exception e){
                        Log.e(TAG, "Illegal access exception");
                    }
                }
            }
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    list.clear();
                    list.addAll(tempList);
                }
            });

        },500);
    }


    /**
     * Updates an object with the element of the database having the @value as documentID
     *
     * @param toUpdate The element to update
     * @param clazz The object type
     * @param collection Not used here, only for real db interactions
     * @param value The value we want to have for the key
     * @param <T> The object type
     */
    @Override
    protected  <T extends DatabaseEntity> void getElement(T toUpdate, Class<T> clazz, String collection,
                                                             String value){

        Handler handler = new Handler(handlerThread.getLooper());
        handler.postDelayed(()->{
            toUpdate.reset();

            DatabaseEntity output = database.get(value);
            if(clazz.isInstance(output)){
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        toUpdate.set(value, output.getEncapsulatedObjectOfMaps());
                    }
                });

            }
            else{
                Log.d(TAG, "The element with id " + value + " wasn't found on db.");
            }
        },500);
    }


    /**
     * Updates an object with the first element of the database having the @value for the @key
     *
     * @param toUpdate The element to update
     * @param clazz The object type
     * @param collection Not used here, only for real db interactions
     * @param key The key of the element to check
     * @param value The value we want to have for the key
     * @param <T> The object type
     */
    @Override
    protected  <T extends DatabaseEntity> void getElement(T toUpdate, Class<T> clazz, String collection,
                                                             DatabaseField key, Object value){

        Handler handler = new Handler(handlerThread.getLooper());
        handler.postDelayed(()->{
            toUpdate.reset();

            for(DatabaseEntity entity : database.values()) {
                if (clazz.isInstance(entity) && value instanceof String && entity.get((DatabaseStringField) key).equals(value)) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            toUpdate.set(entity.documentID, entity.getEncapsulatedObjectOfMaps());
                        }
                    });

                    return;
                }
            }
            Log.d(TAG, "The element if type "+ key.toString()
                    +" with id " + value + " wasn't found on db.");
        },500);
    }

    /**
     * Fills the FakeDatabase with a few users and favors
     */
    public void createBasicDatabase(){
        getInstance().cleanUp();
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
        u4.set(User.StringFields.tokens, FakeAuthentication.TOKENS);
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
        f1.set(Favor.StringFields.ownerEmail, "toto.tata@pipi.com");
        f1.set(Favor.ObjectFields.location, new GeoPoint(0,0));

        f2.set(Favor.StringFields.ownerID, "U1");
        f2.set(Favor.StringFields.category, "Cooking");
        f2.set(Favor.StringFields.deadline, "10.01.19");
        f2.set(Favor.StringFields.description, "Cook me a cookie");
        f2.set(Favor.StringFields.title, "I am hungry pls hurry");
        f2.set(Favor.StringFields.locationCity, "Tombouctou");
        f2.set(Favor.StringFields.ownerEmail, "toto.tata@pipi.com");
        f2.set(Favor.ObjectFields.location, new GeoPoint(0,0));

        f3.set(Favor.StringFields.ownerID, "U3");
        f3.set(Favor.StringFields.category, "Riddle");
        f3.set(Favor.StringFields.deadline, "12.12.20");
        f3.set(Favor.StringFields.description, "We're five little items of an everyday sort; you'll find us all in 'a tennis court'");
        f3.set(Favor.StringFields.title, "TICK TOK");
        f3.set(Favor.StringFields.locationCity, "Gotham City");
        f3.set(Favor.StringFields.ownerEmail, "toto.tata@pipi.com");
        f3.set(Favor.ObjectFields.location, new GeoPoint(0,0));

        f4.set(Favor.StringFields.ownerID, "U3");
        f4.set(Favor.StringFields.category, "Homework");
        f4.set(Favor.StringFields.deadline, "12.12.20");
        f4.set(Favor.StringFields.description, "A bit of help with physics");
        f4.set(Favor.StringFields.title, "Unify String Theroy");
        f4.set(Favor.StringFields.locationCity, "Caltech");
        f4.set(Favor.StringFields.ownerEmail, "toto.tata@pipi.com");
        f4.set(Favor.ObjectFields.location, new GeoPoint(0,0));

        f5.set(Favor.StringFields.ownerID, "U3");
        f5.set(Favor.StringFields.category, "Save the World");
        f5.set(Favor.StringFields.deadline, "12.12.20");
        f5.set(Favor.StringFields.description, "Help me stop Lex Luther");
        f5.set(Favor.StringFields.title, "Help Superman");
        f5.set(Favor.StringFields.locationCity, "US");
        f5.set(Favor.StringFields.ownerEmail, "toto.tata@pipi.com");
        f5.set(Favor.ObjectFields.location, new GeoPoint(0,0));

        f6.set(Favor.StringFields.ownerID, "U3");
        f6.set(Favor.StringFields.category, "Assasin");
        f6.set(Favor.StringFields.deadline, "12.12.20");
        f6.set(Favor.StringFields.description, "Stop the League of Assassins from overtaking the city");
        f6.set(Favor.StringFields.title, "League Help");
        f6.set(Favor.StringFields.locationCity, "DC world");
        f6.set(Favor.StringFields.ownerEmail, "toto.tata@pipi.com");
        f6.set(Favor.ObjectFields.location, new GeoPoint(0,0));

        f7.set(Favor.StringFields.ownerID, "U3");
        f7.set(Favor.StringFields.category, "The Flash");
        f7.set(Favor.StringFields.deadline, "12.12.20");
        f7.set(Favor.StringFields.description, "Stop Salazar from detroying the world");
        f7.set(Favor.StringFields.title, "Flash needs some help");
        f7.set(Favor.StringFields.locationCity, "DC world");
        f7.set(Favor.StringFields.ownerEmail, "toto.tata@pipi.com");
        f7.set(Favor.ObjectFields.location, new GeoPoint(0,0));

        f8.set(Favor.StringFields.ownerID, "U3");
        f8.set(Favor.StringFields.category, "Supergirl");
        f8.set(Favor.StringFields.deadline, "12.12.20");
        f8.set(Favor.StringFields.description, "Destroy kryptoninite on earth");
        f8.set(Favor.StringFields.title, "make use gods again");
        f8.set(Favor.StringFields.locationCity, "DC world");
        f8.set(Favor.StringFields.ownerEmail, "toto.tata@pipi.com");
        f8.set(Favor.ObjectFields.location, new GeoPoint(0,0));

        f9.set(Favor.StringFields.ownerID, "U3");
        f9.set(Favor.StringFields.category, "The Breacher");
        f9.set(Favor.StringFields.deadline, "12.12.20");
        f9.set(Favor.StringFields.description, "Improve my Vibe glasses");
        f9.set(Favor.StringFields.title, "Flash needs some help");
        f9.set(Favor.StringFields.locationCity, "DC world");
        f9.set(Favor.StringFields.ownerEmail, "toto.tata@pipi.com");
        f9.set(Favor.ObjectFields.location, new GeoPoint(0,0));

        f10.set(Favor.StringFields.ownerID, "U3");
        f10.set(Favor.StringFields.category, "The Legends");
        f10.set(Favor.StringFields.deadline, "12.12.20");
        f10.set(Favor.StringFields.description, "Fix a time anomaly in 300BC");
        f10.set(Favor.StringFields.title, "Stop cesar surviving the assasinaton.");
        f10.set(Favor.StringFields.locationCity, "The Timeline");
        f10.set(Favor.StringFields.ownerEmail, "toto.tata@pipi.com");
        f10.set(Favor.ObjectFields.location, new GeoPoint(0,0));


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
        fNew.set(Favor.StringFields.ownerEmail, "toto.tata@pipi.com");
        fNew.set(Favor.ObjectFields.location, new GeoPoint(0,0));

        fNew2.set(Favor.StringFields.ownerID, "U3");
        fNew2.set(Favor.StringFields.category, "Hack The Blue Smurf Institute");
        fNew2.set(Favor.StringFields.deadline, "12.12.20");
        fNew2.set(Favor.StringFields.description, "We all want to be blue");
        fNew2.set(Favor.StringFields.title, "Smurf, Smurf, more Smurf");
        fNew2.set(Favor.StringFields.locationCity, "Smurf World");
        fNew2.set(Favor.StringFields.ownerEmail, "toto.tata@pipi.com");
        fNew2.set(Favor.ObjectFields.location, new GeoPoint(0,0));

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
