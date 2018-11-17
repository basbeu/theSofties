package ch.epfl.sweng.favors.database;


import android.databinding.Observable;
import android.databinding.ObservableList;

import com.google.firebase.firestore.GeoPoint;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import ch.epfl.sweng.favors.location.SortLocations;
import ch.epfl.sweng.favors.database.fields.DatabaseField;
import ch.epfl.sweng.favors.database.fields.DatabaseStringField;

public final class FavorRequest extends Request{

    private static final String TAG = "DB_FAVOR_REQUEST";
    private static final String COLLECTION = "favors";

    private FavorRequest(){}
    public static void getList(ObservableArrayList<Favor> list, DatabaseField element, String value, Integer limit, DatabaseStringField orderBy){
        db.getList(list, Favor.class, COLLECTION, element, value, limit, orderBy);
    }

    public static void all(ObservableArrayList<Favor> list, Integer limit, DatabaseStringField orderBy){
        db.getAll(list, Favor.class, COLLECTION, limit, orderBy);
    }

    public static void getWithId(Favor favor, String value){
        db.getElement(favor, Favor.class, COLLECTION, value);
    }

    public static void getWithLocation(ObservableArrayList<Favor> list, GeoPoint geoPoint, Integer limit, DatabaseStringField orderBy){
        db.getAll(list, Favor.class, COLLECTION, limit, orderBy);
        list.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                Collections.sort((ObservableList) sender, new SortLocations(geoPoint));
                list.removeOnPropertyChangedCallback(this);
            }
        });
    }


}

