package ch.epfl.sweng.favors.database;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.favors.database.fields.DatabaseField;
import ch.epfl.sweng.favors.database.fields.DatabaseStringField;
import ch.epfl.sweng.favors.main.FavorsMain;

import static ch.epfl.sweng.favors.main.FavorsMain.TAG;

public class FirebaseDatabase extends Database{

    private static FirebaseDatabase db = null;
    private static FirebaseFirestore dbFireStore = null;

    private FirebaseDatabase(){
        dbFireStore = FirebaseFirestore.getInstance();
    }


    /**
     * @return The current FirebaseDatabase or a new one if not yet instantiated
     */
    public static FirebaseDatabase getInstance(){
        if(db == null){
            db = new FirebaseDatabase();
        }

        return db;
    }

    public static void setFirebaseTest(FirebaseFirestore newFireStore) {
        dbFireStore = newFireStore;
    }

    @Override
    public void updateOnDb(DatabaseEntity databaseEntity){
        if(databaseEntity.documentID == null){
            // Do the same here if other types of datas

            dbFireStore.collection(databaseEntity.collection).add(databaseEntity.getEncapsulatedObjectOfMaps())
                    .addOnSuccessListener(docRef -> {
                        databaseEntity.documentID = docRef.getId();
                        updateFromDb(databaseEntity);
                    }).addOnFailureListener(e -> {
                Log.d(TAG,"failure to push favor to database");
                /* Feedback of an error here - Impossible to update user informations*/
            });
        }else {
            dbFireStore.collection(databaseEntity.collection).document(databaseEntity.documentID).set(databaseEntity.getEncapsulatedObjectOfMaps())
                    .addOnSuccessListener(aVoid -> updateFromDb(databaseEntity));
        }
        /* Feedback of an error here - Impossible to update user informations */
    }

    @Override
    public Task updateFromDb(DatabaseEntity databaseEntity){
        if(databaseEntity.documentID == null){return Tasks.forCanceled();}
        return dbFireStore.collection(databaseEntity.collection).document(databaseEntity.documentID)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        databaseEntity.updateLocalData(document.getData());
                    } else {
                        Toast.makeText(FavorsMain.getContext(), "An error occured while requesting " +
                                "data from database",Toast.LENGTH_LONG);
                    }
                });

    }

    @Override
    public void deleteFromDatabase(DatabaseEntity databaseEntity) {
        if(databaseEntity == null) return;
        dbFireStore.collection(databaseEntity.collection).document(databaseEntity.documentID).delete();
    }

    class ListRequestFb<T extends DatabaseEntity> implements OnCompleteListener{

        ObservableArrayList<T> list;
        T firstElement;
        Class<T> clazz;

        public  ListRequestFb(ObservableArrayList<T> list, Class<T> clazz){
            super();
            this.list = list;
            this.clazz = clazz;
        }
        public  ListRequestFb(T extratctedFirstElement, Class<T> clazz){
            super();
            this.firstElement = extratctedFirstElement;
            this.clazz = clazz;
        }

        @Override
        public void onComplete(@NonNull Task task) {
            if (!task.isSuccessful()) {
                Log.d(TAG, "get failed with ", task.getException());
            }

            if(task.getResult() instanceof DocumentSnapshot){
                DocumentSnapshot document = (DocumentSnapshot) task.getResult();
                if(firstElement != null ){
                    firstElement.reset();
                    firstElement.set(document.getId(), document.getData());
                }
            }

            else if(task.getResult() instanceof QuerySnapshot){
                if(list != null) list.clear();
                ArrayList<T> temp = new ArrayList<>();
                for (QueryDocumentSnapshot document : (QuerySnapshot) task.getResult()) {
                    try {
                        if (firstElement != null) {
                            firstElement.reset();
                            firstElement.set(document.getId(), document.getData());
                        }
                        if (list != null) {
                            T documentObject = clazz.newInstance();
                            documentObject.set(document.getId(), document.getData());
                            temp.add(documentObject);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Illegal access exception");
                    }
                }
                if(list != null) list.addAll(temp);
            }

        }
    }

    Query addParametersToQuery(Query query, Integer limit, DatabaseField orderBy){
        if(orderBy != null){
            if(orderBy == Favor.ObjectFields.creationTimestamp){
                query = query.orderBy(orderBy.toString(), Query.Direction.DESCENDING);
            } else {
                query = query.orderBy(orderBy.toString());
            }
        }
        if(limit != null){
            query = query.limit(limit);
        }
        return query;
    }

    @Override
    protected <T extends DatabaseEntity> void getAll(ObservableArrayList<T> list, Class<T> clazz,
                                                                       String collection,
                                                                       Integer limit,
                                                                       DatabaseField orderBy){
        Query query = dbFireStore.collection(collection);
        query = addParametersToQuery(query, limit, orderBy);
        query.get().addOnCompleteListener(new ListRequestFb<T>(list, clazz));
    }


    @Override
    protected  <T extends DatabaseEntity> void getList(ObservableArrayList<T> list, Class<T> clazz,
                                                                         String collection,
                                                                         Map<DatabaseField, Object> map,
                                                                         Integer limit,
                                                                         DatabaseField orderBy){


        if(map == null){return;}
        Query query = dbFireStore.collection(collection);
        for(Map.Entry<DatabaseField, Object> el : map.entrySet()){
            query = query.whereEqualTo(el.getKey().toString(), el.getValue());
        }
        query = addParametersToQuery(query, limit, orderBy);
        query.get().addOnCompleteListener(new ListRequestFb<T>(list, clazz));
    }

    protected <T extends DatabaseEntity> void getList(ObservableArrayList<T> list, Class<T> clazz,
                                                                String collection,
                                                                DatabaseField element,
                                                                Object value,
                                                                Integer limit,
                                                                DatabaseField orderBy){
        if(element == null || value == null){return;}
        Query query = dbFireStore.collection(collection).whereEqualTo(element.toString(), value);
        query = addParametersToQuery(query, limit, orderBy);
        query.get().addOnCompleteListener(new ListRequestFb<T>(list, clazz));

    }

    @Override
    protected  <T extends DatabaseEntity> void getElement(T toUpdate, Class<T> clazz, String collection,
                                                             String value){
        if(value == null || toUpdate == null){return;}
        DocumentReference query = dbFireStore.collection(collection).document(value);
        query.get().addOnCompleteListener(new ListRequestFb<T>(toUpdate, clazz));

    }


    @Override
    protected  <T extends DatabaseEntity> void getElement(T toUpdate, Class<T> clazz, String collection,
                                                             DatabaseField element, Object value){
        if(value == null || toUpdate == null){return;}
        Query query = dbFireStore.collection(collection).whereEqualTo(element.toString(), value);
        query.get().addOnCompleteListener(new ListRequestFb<T>(toUpdate, clazz));

    }


    public void cleanUp(){
        db = null;
        dbFireStore = null;
    }
}
