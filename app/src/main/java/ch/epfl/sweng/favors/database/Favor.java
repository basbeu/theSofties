package ch.epfl.sweng.favors.database;

import ch.epfl.sweng.favors.database.fields.DatabaseBooleanField;
import ch.epfl.sweng.favors.database.fields.DatabaseLongField;
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

    public enum StringFields implements DatabaseStringField {title, ownerID, description, locationCity, deadline, category, ownerEmail, tokens, pictureReference}
    public enum LongFields implements DatabaseLongField {tokenPerPerson, nbPerson}
    public enum ObjectFields implements DatabaseObjectField {location, creationTimestamp, expirationTimestamp, interested, selectedPeople}
    public enum BooleanFields implements DatabaseBooleanField {isOpen}

    /**
     * empty constructor as required per firebase
     */
    public Favor(){
        super(StringFields.values(), LongFields.values(), BooleanFields.values(),
                ObjectFields.values(), COLLECTION,null);
        }

    public Favor(String id){
        super(StringFields.values(), LongFields.values(), BooleanFields.values(),
                ObjectFields.values(), COLLECTION, id);
        if(db != null) db.updateFromDb(this);
    }

    @Override
    public DatabaseEntity copy() {
        Favor f = new Favor();
        f.set(this.documentID, this.getEncapsulatedObjectOfMaps());
        return f;
    }
}
