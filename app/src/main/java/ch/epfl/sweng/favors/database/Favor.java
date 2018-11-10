package ch.epfl.sweng.favors.database;

import ch.epfl.sweng.favors.database.fields.DatabaseBooleanField;
import ch.epfl.sweng.favors.database.fields.DatabaseIntField;
import ch.epfl.sweng.favors.database.fields.DatabaseObjectField;
import ch.epfl.sweng.favors.database.fields.DatabaseStringField;

/**
 * The Favor class is an extension of the Database handler
 */
public class Favor extends DatabaseEntity {

    // tag for log messages
    private static final String TAG = "FAVOR";
    // identifier for firebase
    private static final String COLLECTION = "favors";

    public enum StringFields implements DatabaseStringField {title, ownerID, description, locationCity, deadline, category, ownerEmail}
    public enum IntegerFields implements DatabaseIntField {reward}
    public enum ObjectFields implements DatabaseObjectField {location, creationTimestamp, expirationTimestamp}
    public enum BooleanFields implements DatabaseBooleanField {isOpen}


    /**
     * empty constructor as required per firebase
     */
    public Favor(){
        super(StringFields.values(), IntegerFields.values(), BooleanFields.values(),
                ObjectFields.values(), COLLECTION,null);
    }

    public Favor(String id){
        super(StringFields.values(), IntegerFields.values(), BooleanFields.values(),
                ObjectFields.values(), COLLECTION, id);
        db.updateFromDb(this);
    }

    @Override
    public DatabaseEntity copy() {
        Favor f = new Favor(this.documentID);
        f.updateLocalData(this.getEncapsulatedObjectOfMaps());
        return f;
    }
}
