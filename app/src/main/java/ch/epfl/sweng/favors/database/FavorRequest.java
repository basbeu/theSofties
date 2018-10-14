package ch.epfl.sweng.favors.database;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public final class FavorRequest {

    protected static FirebaseFirestore db = FirebaseFirestore.getInstance();


    private static final String TAG = "DB_FAVOR";
    private static final String COLLECTION = "favors";

    private FavorRequest(){}

    static <T extends DatabaseHandler> ObservableField<ArrayList<T>> getList(DatabaseField element, String value, Integer limit, DatabaseStringField orderBy){
        ObservableField<ArrayList<T>> result = new ObservableField<>(new ArrayList<T>());
        if(element == null || value == null){return null;}
        Query query = db.collection(COLLECTION).whereEqualTo(element.toString(), value);
        if(orderBy != null){
            query = query.orderBy(orderBy.toString());
        }
        if(limit != null){
            query = query.limit(limit);
        }
        getList(query, result);
        return result;
    }
    private static <T extends DatabaseHandler>  void getList(Query query, final ObservableField<ArrayList<T>> feedbackContainer){
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        feedbackContainer.get().add(new T(document.getId(), document.getData()));
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }
}
