package ch.epfl.sweng.favors.database;

import android.databinding.ObservableArrayList;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import ch.epfl.sweng.favors.database.fields.DatabaseField;
import ch.epfl.sweng.favors.database.fields.DatabaseStringField;


public abstract class Request {

    private static final String TAG = "DB_REQUEST";
    protected static Database db = Database.getInstance();


}
