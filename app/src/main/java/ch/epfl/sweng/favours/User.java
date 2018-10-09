package ch.epfl.sweng.favours;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

public class User {

    private static User user = new User();
    public static User getMain(){
        return user;
    }

    public User(){
        if(FirebaseAuth.getInstance().getUid() != null)
            updateFromDb();
    }


    public enum StringFields{firstName, lastName, email, sex, basedLocation}
    Map<StringFields, ObservableField<String>> stringData = new HashMap<StringFields, ObservableField<String>>(){
        {
            for(StringFields field : StringFields.values()){
                this.put(field, new ObservableField<String>());
            }
        }

    };


    public String get(StringFields field){
        if(stringData.get(field) != null) return stringData.get(field).get();
        else return null;
    }
    public ObservableField<String> getObservableStringObject(StringFields field){
        return stringData.get(field);
    }

    public void set(StringFields field, String value){
        stringData.get(field).set(value);
    }


    public void updateUserDataOnServer(){
        Map<String, Object> toSend = new HashMap<>();

        convertTypedMapToObjectMap(stringData, toSend);

        // Do the same here if other types of datas

        Database.getInstance().getUserCollection().document(FirebaseAuth.getInstance().getUid()).set(toSend)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                updateFromDb();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                /* Feedback of an error here - Impossible to update user informations */
            }
        });
    }

    public void updateFromDb(){
        Database.getInstance().getUserCollection().document(FirebaseAuth.getInstance().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.getData() != null) parseStringData(document.getData());
                    // Do the same for rest of datas

                } else {
                    /* Feedback of an error here - Impossible to get user informations from server */
                }
            }
        });
    }

    private void parseStringData(Map<String, Object> data){
        for(StringFields fieldName : StringFields.values()){
            if(data.get(fieldName.toString()) instanceof String){
                set(fieldName, (String) data.get(fieldName.toString()));
            }
        }
    }

    /**
     * Convert the map containing some parameters in an String / Object map
     * @param from  The original map to convert
     * @param to    The map where to add objects
     * @param <T>   The enum of map content
     * @param <V>   The ObservableField content type
     * @param <U>   The ObservableField
     */
    <T extends Enum<T>, V, U extends ObservableField<V>> void convertTypedMapToObjectMap(Map<T, U> from, Map<String, Object> to) {
        for (Map.Entry<T, U> entry : from.entrySet()){
            V value = (V) entry.getValue().get();
            if(value != null) to.put(entry.getKey().toString(), value);
        }
    }


}
