package ch.epfl.sweng.favors.database;

import android.databinding.ObservableArrayList;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;


public abstract class Request {

    private static final String TAG = "DB_REQUEST";

    protected static FirebaseFirestore db = FirebaseFirestore.getInstance();

    protected static <T extends DatabaseHandler> ObservableArrayList<T> getList(Class<T> clazz,
                                                                                String collection,
                                                                                DatabaseField element,
                                                                                String value,
                                                                                Integer limit,
                                                                                DatabaseStringField orderBy){
        ObservableArrayList<T> result = new ObservableArrayList<>();
        if(element == null || value == null){return null;}
        Log.d(TAG,element.toString() + value);
        Query query = db.collection(collection).whereEqualTo(element.toString(), value);
        if(orderBy != null){
            query = query.orderBy(orderBy.toString());
        }
        if(limit != null){
            query = query.limit(limit);
        }
        getList(query, result, clazz);
        return result;
    }
    private static <T extends DatabaseHandler>  void getList(Query query,
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
