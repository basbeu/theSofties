package ch.epfl.sweng.favors.database;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import ch.epfl.sweng.favors.authentication.FakeAuthentication;
import ch.epfl.sweng.favors.database.fields.DatabaseBooleanField;
import ch.epfl.sweng.favors.database.fields.DatabaseField;
import ch.epfl.sweng.favors.database.fields.DatabaseLongField;
import ch.epfl.sweng.favors.database.fields.DatabaseObjectField;
import ch.epfl.sweng.favors.database.fields.DatabaseStringField;


/**
 * Singleton class that represent a fake DB for testing purpose
 */
public class FakeDatabase extends Database{

    public static final String LAST_FAVOR_TITLE = "Flash needs some help";
    public static FakeDatabase db = null;
    private HashMap<String, DatabaseEntity> database;
    private static final String TAG = "FAKE_DB";

    final HandlerThread handlerThread = new HandlerThread("background-thread");

    ArrayList<Listener> listeners = new ArrayList<>();

    abstract class Listener{
        abstract void notifyChange();
    }
    private void notifyDatabaseModification(){
        for(Listener ln: listeners){
            ln.notifyChange();
        }
    }

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
            databaseEntity.documentID = generatedString;
            database.put( generatedString, databaseEntity.copy());
        }
        notifyDatabaseModification();
        updateFromDb(databaseEntity);

    }
    @Override
    public Task updateFromDb(DatabaseEntity databaseEntity) {
        if(databaseEntity.documentID == null || !database.containsKey(databaseEntity.documentID)){return Tasks.forCanceled();}
        databaseEntity.updateLocalData(database.get(databaseEntity.documentID).getEncapsulatedObjectOfMaps());

        Handler handler = new Handler(handlerThread.getLooper());
        handler.postDelayed(()->{
            new Handler(Looper.getMainLooper()).post(() -> {
                if(database.get(databaseEntity.documentID) == null) return;
                databaseEntity.updateLocalData(database.get(databaseEntity.documentID).getEncapsulatedObjectOfMaps());
            });

        },20);

        return Tasks.forResult(true);
    }

    @Override
    public Task deleteFromDatabase(DatabaseEntity databaseEntity) {
        if(databaseEntity == null) return Tasks.forResult(false);
        database.remove(databaseEntity.documentID);

        return Tasks.forResult(true);
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
            new Handler(Looper.getMainLooper()).post(() -> list.update(tempList));


        },500);
    }

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
            new Handler(Looper.getMainLooper()).post(() -> list.update(tempList));

        },500);
    }

    @Override
    protected  <T extends DatabaseEntity> void getLiveList(ObservableArrayList<T> list, Class<T> clazz,
                                                       String collection,
                                                       DatabaseField key,
                                                       Object value,
                                                       Integer limit,
                                                       DatabaseField orderBy){

        Handler handler = new Handler(handlerThread.getLooper());
        handler.postDelayed(()->{
            listeners.add(new Listener() {
                @Override
                void notifyChange() {
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
                    if(key == ChatMessage.StringFields.conversationId) Collections.reverse(tempList);
                    new Handler(Looper.getMainLooper()).post(() -> list.update(tempList));
                }
            });
            notifyDatabaseModification();
        },500);
    }

    enum CheckType{Greater, Equal, Less}

    private boolean check(CheckType type, Map.Entry<DatabaseField, Object>  entry, DatabaseEntity entity){
        if(entry == null || entity == null) return false;

        if(entry.getKey() instanceof DatabaseStringField) {
            String temp = entity.get((DatabaseStringField) entry.getKey());
            if(temp == null || !(entry.getValue() instanceof String)) return false;
            if(type == CheckType.Equal) return temp.equals(entry.getValue());
            else return false; // Comparison of String have no meaning
        }
        else if(entry.getKey() instanceof DatabaseLongField) {
            Long temp = entity.get((DatabaseLongField) entry.getKey());
            if(temp == null || !(entry.getValue() instanceof Long)) return false;
            if(type == CheckType.Equal) return temp == (long) entry.getValue();
            if(type == CheckType.Greater) return temp < (long) entry.getValue();
            if(type == CheckType.Less) return temp > (long) entry.getValue();
        }

        else if(entry.getKey() instanceof DatabaseBooleanField) {
            Boolean temp = entity.get((DatabaseBooleanField) entry.getKey());
            if(temp == null || !(entry.getValue() instanceof Boolean)) return false;
            if(type == CheckType.Equal) return temp == (boolean) entry.getValue();
            else return false; // Comparison on boolean has no meaning
        }
        else {
            Object temp = entity.get((DatabaseObjectField) entry.getKey());
            if(temp == null) return false;
            if(temp instanceof Timestamp) {
                if(!(entry.getValue() instanceof Timestamp)) return false;
                int out = ((Timestamp) temp).compareTo((Timestamp) entry.getValue());
                if(type == CheckType.Equal) return out == 0;
                if(type == CheckType.Greater) return out > 0;
                if(type == CheckType.Less) return out < 0;
            }
            if(temp instanceof ArrayList) {
                if(!(entry.getValue() instanceof String)) return false;
                if(((ArrayList<String>) temp).contains((String) entry.getValue())) return true;
                return false;
            }
        }
        Log.d(TAG, "Performing a not implemented comparison in fake database");
        return false;
    }

    @Override
    protected  <T extends DatabaseEntity>  void getList(ObservableArrayList<T> list, Class<T> clazz,
                                                        String collection,
                                                        Map<DatabaseField, Object> mapEquals,
                                                        Map<DatabaseField, Object> mapLess,
                                                        Map<DatabaseField, Object> mapMore,
                                                        Map<DatabaseField, Object> mapContains,
                                                        Integer limit,
                                                        DatabaseField orderBy){
        Log.d(TAG, clazz.getName());
        Handler handler = new Handler(handlerThread.getLooper());
        handler.postDelayed(()->{

            ArrayList<T> tempList = new ArrayList<>();
            for(DatabaseEntity entity : database.values()) {
                if(!clazz.isInstance(entity)){
                    continue;
                }
                Boolean toAdd = true;
                //noinspection ConstantConditions
                toAdd = processMap(mapEquals, entity, toAdd, CheckType.Equal);
                toAdd = processMap(mapLess, entity, toAdd, CheckType.Less);
                toAdd = processMap(mapMore, entity, toAdd,  CheckType.Greater);
                toAdd = processMap(mapContains, entity, toAdd,  CheckType.Equal);
                if(toAdd) {
                    addToList(clazz, tempList, entity);
                }
            }
            new Handler(Looper.getMainLooper()).post(() -> list.update(tempList));

        },500);
    }

    /**
     * Checks if the parameters specified in map are valide, if they are not the *toAdd* parameter will be returned as false. The checkType is required to allow for the proper types of checks to be opperated.
     * Only pass the check type corresponding to the information that is contained in the map.
     * @param map map of parameters to check
     * @param entity element of the database that is being checkes with the different querry parameters to determine if it shoul dbe added to the DB
     * @param toAdd boolean value that determins if entity will be added to the list
     * @param checkType Type of check to opperate on the map
     * @return updated version of toAdd this is required to check if the element still needs to be added.
     */
    private boolean processMap(Map<DatabaseField, Object> map, DatabaseEntity entity, Boolean toAdd, CheckType checkType) {
        if (toAdd && map != null) {
            for (Map.Entry<DatabaseField, Object> e : map.entrySet()) {
                if (!check(checkType, e, entity)) {
                    toAdd = false;
                    break;
                }
            }
        }
        return toAdd;
    }

    /**
     *
     * @param clazz Class
     * @param tempList the list that will receive the elements
     * @param entity entity to add to list
     * @param <T> Tape of entityType
     */
    private <T extends DatabaseEntity> void addToList(Class<T> clazz, ArrayList<T> tempList, DatabaseEntity entity) {
            try {
                T temp = clazz.newInstance();
                temp.set(entity.documentID, entity.getEncapsulatedObjectOfMaps());
                tempList.add(temp);
                Log.d(TAG, "Added element to return");
            } catch (Exception e) {
                Log.e(TAG, "Illegal access exception");
            }
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
                new Handler(Looper.getMainLooper()).post(() -> toUpdate.set(value, output.getEncapsulatedObjectOfMaps()));

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
                    new Handler(Looper.getMainLooper()).post(() -> toUpdate.set(entity.documentID, entity.getEncapsulatedObjectOfMaps()));

                    return;
                }
                else if (clazz.isInstance(entity) && value instanceof Long && entity.get((DatabaseLongField) key).equals(value)) {
                    new Handler(Looper.getMainLooper()).post(() -> toUpdate.set(entity.documentID, entity.getEncapsulatedObjectOfMaps()));

                    return;
                }
            }
            Log.d(TAG, "The element if type "+ key.toString()
                    +" with id " + value + " wasn't found on db.");
        },500);
    }


    private long dayToMs(int n){
        return (long) n * 86400L;
    }
    /**
     * Fills the FakeDatabase with a few users and favors
     */
    public void createBasicDatabase(){
        getInstance().cleanUp();

        User u0 = new User(FakeAuthentication.UID);
        User u1 = new User("U1");
        User u2 = new User("U2");
        User u3 = new User("U3");
        User u4 = new User("U4");

        u0.set(User.StringFields.firstName, FakeAuthentication.FIRST_NAME);
        u0.set(User.StringFields.lastName,FakeAuthentication.LAST_NAME);
        u0.set(User.StringFields.email, FakeAuthentication.EMAIL);
        u0.set(User.StringFields.city, FakeAuthentication.CITY);
        u0.set(User.LongFields.tokens, FakeAuthentication.TOKENS);
        User.UserGender.setGender(u0, FakeAuthentication.GENDER);
        ArrayList<String> notifications = new ArrayList<String>();
        notifications.add("Someone is interested in your favor !");
        u0.set(User.ObjectFields.notifications, notifications);



        u1.set(User.StringFields.firstName, "Jean");
        u1.set(User.StringFields.lastName, "Marchand");
        u1.set(User.StringFields.email, "jean.marchand@thesoftie.com");
        u1.set(User.StringFields.city, "Sydney, AU");
        u1.set(User.LongFields.tokens, 10L);
        User.UserGender.setGender(u1, User.UserGender.M);
        u1.set(User.ObjectFields.notifications, new ArrayList<String>());

        u2.set(User.StringFields.firstName, "Jeanne");
        u2.set(User.StringFields.lastName, "Trousse");
        u2.set(User.StringFields.email, "jeanne.trousse@thesoftie.com");
        u2.set(User.StringFields.city, "New York, US");
        u2.set(User.LongFields.tokens, 10L);
        User.UserGender.setGender(u2, User.UserGender.F);
        u2.set(User.ObjectFields.notifications, new ArrayList<String>());

        u3.set(User.StringFields.firstName, "Harvey");
        u3.set(User.StringFields.lastName, "Dent");
        u3.set(User.StringFields.email, "harvey.dent@gthesoftie.com");
        u3.set(User.StringFields.city, "London, UK");
        u3.set(User.LongFields.tokens, 10L);
        User.UserGender.setGender(u3, User.UserGender.M);
        u3.set(User.ObjectFields.notifications, new ArrayList<String>());

        u4.set(User.StringFields.firstName, "Marie");
        u4.set(User.StringFields.lastName, "Vaud");
        u4.set(User.StringFields.email, "marie.vaud@gthesoftie.com");
        u4.set(User.StringFields.city, "Madrid, ES");
        u4.set(User.LongFields.tokens, 10L);
        User.UserGender.setGender(u4, User.UserGender.F);
        u4.set(User.ObjectFields.notifications, new ArrayList<String>());

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


        ArrayList<String> interestedPeople1 = new ArrayList<>();
        interestedPeople1.add(u2.getId());
        interestedPeople1.add(u4.getId());

        f1.set(Favor.StringFields.ownerID, "U1");
        f1.set(Favor.StringFields.category, "Babysitting");
        f1.set(Favor.StringFields.description, "I lost my baby.");
        f1.set(Favor.StringFields.title, "Expiring soon favor");

        f1.set(Favor.StringFields.locationCity, "London, UK");
        f1.set(Favor.ObjectFields.location, new GeoPoint(51.509865, -0.118092));
        f1.set(Favor.ObjectFields.creationTimestamp, new Timestamp(System.currentTimeMillis() / 1000L - dayToMs(13), 0));
        f1.set(Favor.ObjectFields.expirationTimestamp, new Timestamp(System.currentTimeMillis() / 1000L + dayToMs(1), 0));

        f1.set(Favor.ObjectFields.interested, interestedPeople1);
        f1.set(Favor.LongFields.nbPerson, 1L);
        f1.set(Favor.LongFields.tokenPerPerson, 2L);



        ArrayList<String> interestedPeople2 = new ArrayList<>();
        interestedPeople2.add(u0.getId());
        interestedPeople2.add(u3.getId());

        f2.set(Favor.StringFields.ownerID, "U1");
        f2.set(Favor.StringFields.category, "Cooking");
        f2.set(Favor.StringFields.description, "Cook me a cookie");
        f2.set(Favor.StringFields.title, "I am hungry");

        f2.set(Favor.StringFields.locationCity, "Strasbourg, FR");
        f2.set(Favor.ObjectFields.location, new GeoPoint(48.58392, 7.74553));
        f2.set(Favor.ObjectFields.interested, interestedPeople2);
        f2.set(Favor.ObjectFields.creationTimestamp, new Timestamp(System.currentTimeMillis() / 1000L - dayToMs(11), 0));
        f2.set(Favor.ObjectFields.expirationTimestamp, new Timestamp(System.currentTimeMillis() / 1000L + dayToMs(11), 0));

        f2.set(Favor.LongFields.nbPerson, 2L);
        f2.set(Favor.LongFields.tokenPerPerson, 1L);



        ArrayList<String> interestedPeople3 = new ArrayList<>();
        interestedPeople3.add(u3.getId());
        interestedPeople3.add(u2.getId());

        f3.set(Favor.StringFields.ownerID, FakeAuthentication.UID);
        f3.set(Favor.StringFields.title, "Cook at home");
        f3.set(Favor.StringFields.description, "Cook for my grandma");
        f3.set(Favor.StringFields.category, "Cooking");

        f3.set(Favor.StringFields.locationCity, "Paris, FR");
        f3.set(Favor.ObjectFields.location, new GeoPoint(48.864716, 2.349014));
        f3.set(Favor.ObjectFields.interested, interestedPeople3);
        f4.set(Favor.ObjectFields.creationTimestamp, new Timestamp(System.currentTimeMillis() / 1000L - dayToMs(9), 0));
        f4.set(Favor.ObjectFields.expirationTimestamp, new Timestamp(System.currentTimeMillis() / 1000L + dayToMs(9), 0));


        f3.set(Favor.LongFields.nbPerson, 2L);
        f3.set(Favor.LongFields.tokenPerPerson, 2L);



        ArrayList<String> interestedPeople4 = new ArrayList<>();
        interestedPeople4.add(u3.getId());
        interestedPeople4.add(u2.getId());
        interestedPeople4.add(u4.getId());

        f4.set(Favor.StringFields.ownerID, "U1");
        f4.set(Favor.StringFields.title, "Cook at work");
        f4.set(Favor.StringFields.description, "A bit of help with physics");
        f4.set(Favor.StringFields.category, "Cooking");

        f4.set(Favor.StringFields.locationCity, "Vienna, AT");
        f4.set(Favor.ObjectFields.location, new GeoPoint(48.210033, 16.363449));
        f4.set(Favor.ObjectFields.interested, interestedPeople4);
        f4.set(Favor.ObjectFields.creationTimestamp, new Timestamp(System.currentTimeMillis() / 1000L - dayToMs(8), 0));
        f4.set(Favor.ObjectFields.expirationTimestamp, new Timestamp(System.currentTimeMillis() / 1000L + dayToMs(8), 0));

        f4.set(Favor.LongFields.nbPerson, 1L);
        f4.set(Favor.LongFields.tokenPerPerson, 1L);




        f5.set(Favor.StringFields.ownerID, "U2");
        f5.set(Favor.StringFields.title, "Help Superman");
        f5.set(Favor.StringFields.description, "Help me stop Lex Luther");
        f5.set(Favor.StringFields.category, "Tutoring");

        f5.set(Favor.StringFields.locationCity, "Stockholm, SE");
        f5.set(Favor.ObjectFields.location, new GeoPoint(59.334591, 18.063240));
        f5.set(Favor.ObjectFields.creationTimestamp, new Timestamp(System.currentTimeMillis() / 1000L - dayToMs(7), 0));
        f5.set(Favor.ObjectFields.expirationTimestamp, new Timestamp(System.currentTimeMillis() / 1000L + dayToMs(7), 0));

        f5.set(Favor.LongFields.nbPerson, 1L);
        f5.set(Favor.LongFields.tokenPerPerson, 2L);



        f6.set(Favor.StringFields.ownerID, "U3");
        f6.set(Favor.StringFields.title, "League Help");
        f6.set(Favor.StringFields.description, "Stop the League of Assassins from overtaking the city");
        f6.set(Favor.StringFields.category, "Tutoring");

        f6.set(Favor.StringFields.locationCity, "Hong Kong");
        f6.set(Favor.ObjectFields.location, new GeoPoint(22.286394, 114.149139));
        f6.set(Favor.ObjectFields.creationTimestamp, new Timestamp(System.currentTimeMillis() / 1000L - dayToMs(6), 0));
        f6.set(Favor.ObjectFields.expirationTimestamp, new Timestamp(System.currentTimeMillis() / 1000L + dayToMs(6), 0));


        f6.set(Favor.LongFields.nbPerson, 1L);
        f6.set(Favor.LongFields.tokenPerPerson, 1L);



        f7.set(Favor.StringFields.ownerID, "U1");
        f7.set(Favor.StringFields.title, "Closest favor");
        f7.set(Favor.StringFields.description, "Stop Salazar from detroying the world");
        f7.set(Favor.StringFields.category, "Babysitting");

        f7.set(Favor.StringFields.locationCity, "Lausanne, CH");
        f7.set(Favor.ObjectFields.location, new GeoPoint(46.516, 6.63282));
        f7.set(Favor.ObjectFields.creationTimestamp, new Timestamp(System.currentTimeMillis() / 1000L - dayToMs(5), 0));
        f7.set(Favor.ObjectFields.expirationTimestamp, new Timestamp(System.currentTimeMillis() / 1000L + dayToMs(5), 0));


        f7.set(Favor.LongFields.nbPerson, 2L);
        f7.set(Favor.LongFields.tokenPerPerson, 1L);



        f8.set(Favor.StringFields.ownerID, FakeAuthentication.UID);
        f8.set(Favor.StringFields.title, "Make use gods again");
        f8.set(Favor.StringFields.description, "Destroy kryptoninite on earth");
        f8.set(Favor.StringFields.category, "Babysitting");

        f8.set(Favor.StringFields.locationCity, "Los Angeles, US");
        f8.set(Favor.ObjectFields.location, new GeoPoint(34.05223, -118.24368));
        f8.set(Favor.ObjectFields.creationTimestamp, new Timestamp(System.currentTimeMillis() / 1000L - dayToMs(4), 0));
        f8.set(Favor.ObjectFields.expirationTimestamp, new Timestamp(System.currentTimeMillis() / 1000L - dayToMs(2), 0));


        f8.set(Favor.LongFields.nbPerson, 1L);
        f8.set(Favor.LongFields.tokenPerPerson, 2L);



        f9.set(Favor.StringFields.ownerID, "U2");
        f9.set(Favor.StringFields.title, "My dog is sick");
        f9.set(Favor.StringFields.category, "Gardening");
        f9.set(Favor.StringFields.description, "Improve my Vibe glasses");

        f9.set(Favor.StringFields.locationCity, "Zurich, CH");
        f9.set(Favor.ObjectFields.location, new GeoPoint(47.36667, 8.55));
        f9.set(Favor.ObjectFields.creationTimestamp, new Timestamp(System.currentTimeMillis() / 1000L - dayToMs(3), 0));
        f9.set(Favor.ObjectFields.expirationTimestamp, new Timestamp(System.currentTimeMillis() / 1000L + dayToMs(3), 0));

        f9.set(Favor.LongFields.nbPerson, 1L);
        f9.set(Favor.LongFields.tokenPerPerson, 1L);


        ArrayList<String> interestedPeople6 = new ArrayList<>();
        interestedPeople6.add(u4.getId());
        interestedPeople6.add(u2.getId());
        interestedPeople6.add(u3.getId());

        f10.set(Favor.StringFields.ownerID, FakeAuthentication.UID);
        f10.set(Favor.StringFields.title, "Most recent favor");
        f10.set(Favor.StringFields.category, "Gardening");
        f10.set(Favor.StringFields.description, "It does messy things");

        f10.set(Favor.StringFields.locationCity, "Basel, CH");
        f10.set(Favor.ObjectFields.location, new GeoPoint(47.559601, 7.588576));
        f10.set(Favor.ObjectFields.interested, interestedPeople6);
        f10.set(Favor.ObjectFields.creationTimestamp, new Timestamp(System.currentTimeMillis() / 1000L - dayToMs(2), 0));
        f10.set(Favor.ObjectFields.expirationTimestamp, new Timestamp(System.currentTimeMillis() / 1000L + dayToMs(2), 0));

        f10.set(Favor.LongFields.nbPerson, 3L);
        f10.set(Favor.LongFields.tokenPerPerson, 2L);


        Interest i1 = new Interest("I1");
        Interest i2 = new Interest("I2");
        Interest i3 = new Interest("I3");
        Interest i4 = new Interest("I4");
        Interest i5 = new Interest("I5");

        i1.set(Interest.StringFields.title, "Cooking");
        i1.set(Interest.StringFields.description, "To cook for someone");

        i2.set(Interest.StringFields.title, "Tutoring");
        i2.set(Interest.StringFields.description, "Marvel Comics");

        i3.set(Interest.StringFields.title, "Gardening");
        i3.set(Interest.StringFields.description, "Fly to the sky");

        i4.set(Interest.StringFields.title, "Babysitting");
        i4.set(Interest.StringFields.description, "Comics are great");

        i5.set(Interest.StringFields.title, "Shopping");
        i5.set(Interest.StringFields.description, "Yet another great airline");

        ChatInformations c1 = new ChatInformations("C1");
        ChatInformations c2 = new ChatInformations("C2");

        ArrayList<String> participants1 = new ArrayList<>();
        participants1.add(u0.getId());
        participants1.add(u1.getId());
        participants1.add(u2.getId());
        c1.set(ChatInformations.ObjectFields.participants, participants1);
        c1.set(ChatInformations.LongFields.creationTime, System.currentTimeMillis() - 20000000);
        c1.set(ChatInformations.LongFields.lastMessageTime,  System.currentTimeMillis() - 2000000);
        c1.set(ChatInformations.StringFields.title,  "Conversation test");


        ArrayList<String> participants2 = new ArrayList<>();
        participants2.add(u0.getId());
        participants2.add(u3.getId());
        c2.set(ChatInformations.ObjectFields.participants, participants2);
        c2.set(ChatInformations.LongFields.creationTime, System.currentTimeMillis() - 10000000);
        c2.set(ChatInformations.LongFields.lastMessageTime,  System.currentTimeMillis() - 1000000);



        ChatMessage m0 = new ChatMessage("M0");
        ChatMessage m1 = new ChatMessage("M1");
        ChatMessage m2 = new ChatMessage("M2");

        m0.set(ChatMessage.StringFields.conversationId, "C1");
        m1.set(ChatMessage.StringFields.conversationId, "C1");
        m2.set(ChatMessage.StringFields.conversationId, "C1");

        m0.set(ChatMessage.StringFields.messageContent, "Hey");
        m1.set(ChatMessage.StringFields.messageContent, "Salut, tu vas bien ?");
        m2.set(ChatMessage.StringFields.messageContent, "Bien et toi ?");

        m0.set(ChatMessage.StringFields.writerId, FakeAuthentication.UID);
        m1.set(ChatMessage.StringFields.writerId, "U1");
        m2.set(ChatMessage.StringFields.writerId, FakeAuthentication.UID);

        m0.set(ChatMessage.LongFields.messageDate, System.currentTimeMillis() - 8600000);
        m1.set(ChatMessage.LongFields.messageDate, System.currentTimeMillis() - 5000000);
        m2.set(ChatMessage.LongFields.messageDate, System.currentTimeMillis() - 2000000);


        ChatMessage m3 = new ChatMessage("M3");
        ChatMessage m4 = new ChatMessage("M4");
        ChatMessage m5 = new ChatMessage("M5");

        m3.set(ChatMessage.StringFields.conversationId, "C2");
        m4.set(ChatMessage.StringFields.conversationId, "C2");
        m5.set(ChatMessage.StringFields.conversationId, "C2");

        m3.set(ChatMessage.StringFields.messageContent, "T'es en vacances ?");
        m4.set(ChatMessage.StringFields.messageContent, "Pas encore");
        m5.set(ChatMessage.StringFields.messageContent, "Moi non plus");

        m3.set(ChatMessage.StringFields.writerId, "U3");
        m4.set(ChatMessage.StringFields.writerId, FakeAuthentication.UID);
        m5.set(ChatMessage.StringFields.writerId, "U3");

        m3.set(ChatMessage.LongFields.messageDate, System.currentTimeMillis() - 8600000);
        m4.set(ChatMessage.LongFields.messageDate, System.currentTimeMillis() - 5000000);
        m5.set(ChatMessage.LongFields.messageDate, System.currentTimeMillis() - 1000000);


        getInstance().updateOnDb(i1);
        getInstance().updateOnDb(i2);
        getInstance().updateOnDb(i3);
        getInstance().updateOnDb(i4);
        getInstance().updateOnDb(i5);

        getInstance().updateOnDb(u0);
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

        getInstance().updateOnDb(c1);
        getInstance().updateOnDb(c2);

        getInstance().updateOnDb(m0);
        getInstance().updateOnDb(m1);
        getInstance().updateOnDb(m2);
        getInstance().updateOnDb(m3);
        getInstance().updateOnDb(m4);
        getInstance().updateOnDb(m5);
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
