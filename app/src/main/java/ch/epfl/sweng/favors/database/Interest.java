package ch.epfl.sweng.favors.database;

import ch.epfl.sweng.favors.database.fields.DatabaseBooleanField;
import ch.epfl.sweng.favors.database.fields.DatabaseLongField;
import ch.epfl.sweng.favors.database.fields.DatabaseObjectField;
import ch.epfl.sweng.favors.database.fields.DatabaseStringField;

/**
 * This class loads all the possible categories of interests from the firestore database
 * in order to provide the user a choice of interest to better
 * categorize and tag their favors
 *
 * Examples for such categories are:
 * Tutoring, Technological issues, Baysitting, etc.
 */
public class Interest extends DatabaseEntity {

    private static final String TAG = "DB_INTEREST";
    private static final String COLLECTION = "interests";

    public enum StringFields implements DatabaseStringField {title, description}
    public enum LongFields implements DatabaseLongField {}
    public enum ObjectFields implements DatabaseObjectField {linkedInterests}
    public enum BooleanFields implements DatabaseBooleanField {}


    /**
     * Empty constructor as required by firebase
     */
    public Interest(){
        super(StringFields.values(), LongFields.values(), BooleanFields.values(),
                ObjectFields.values(), COLLECTION,null);
    }

    /**
     * Allows retrieving a specific interest from the datbase
     * @param id unique id of an interest
     */
    public Interest(String id){
        super(StringFields.values(), LongFields.values(), BooleanFields.values(),
                ObjectFields.values(), COLLECTION,id);
        if(db != null) db.updateFromDb(this);
    }

    @Override
    public DatabaseEntity copy() {
        Interest i = new Interest();
        i.set(this.documentID, this.getEncapsulatedObjectOfMaps());
        return i;
    }
}
