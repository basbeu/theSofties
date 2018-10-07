package ch.epfl.sweng.favours;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DataBaseHandler {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void getUser(String uId){
        DocumentReference mDocRef = db.document("users/"+uId);
        mDocRef.get();
    }
}
