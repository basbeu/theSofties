package ch.epfl.sweng.favors.database;

import android.databinding.ObservableArrayList;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

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

    public static void setFirebaseTest(FirebaseFirestore newFireStore) {
        dbFireStore = newFireStore;
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
    protected <T extends DatabaseEntity> ObservableArrayList<T> getAll(Class<T> clazz,
                                                                       String collection,
                                                                       Integer limit,
                                                                       DatabaseStringField orderBy){
        ObservableArrayList<T> result = new ObservableArrayList<>();
        Query query = dbFireStore.collection(collection);
        if(orderBy != null){
            query = query.orderBy(orderBy.toString());
        }
        if(limit != null){
            query = query.limit(limit);
        }
        getList(query, result, clazz);
        return result;
    }

    @Override
    protected  <T extends DatabaseEntity> ObservableArrayList<T> getList(Class<T> clazz,
                                                                         String collection,
                                                                         DatabaseField element,
                                                                         String value,
                                                                         Integer limit,
                                                                         DatabaseStringField orderBy){
        ObservableArrayList<T> result = new ObservableArrayList<>();
        if(element == null || value == null){return null;}
        Query query = dbFireStore.collection(collection).whereEqualTo(element.toString(), value);
        if(orderBy != null){
            query = query.orderBy(orderBy.toString());
        }
        if(limit != null){
            query = query.limit(limit);
        }
        getList(query, result, clazz);
        return result;
    }

    /**
     *
     * @param query
     * @param feedbackContainer
     * @param clazz
     * @param <T>
     */
    private  <T extends DatabaseEntity>  void getList(Query query,
                                                      ObservableArrayList<T> feedbackContainer,
                                                      Class<T> clazz ){
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Request success", task.getException());
                    ArrayList<T> tempList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        try{
                            T documentObject = clazz.newInstance();
                            documentObject.set(document.getId(), document.getData());
                            tempList.add(documentObject);
                        }
                        catch (Exception e){
                            Log.e(TAG, "Illegal access exception");
                        }
                    }
                    feedbackContainer.addAll(tempList);
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }
}
