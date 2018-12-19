package ch.epfl.sweng.favors.database;

import ch.epfl.sweng.favors.database.fields.DatabaseBooleanField;
import ch.epfl.sweng.favors.database.fields.DatabaseLongField;
import ch.epfl.sweng.favors.database.fields.DatabaseObjectField;
import ch.epfl.sweng.favors.database.fields.DatabaseStringField;

/**
 * The Favor class is an extension of the Database handler
 *
 * represents a favor, the basis of our app, which users can then react with
 *
 * A favor has a:
 * title & an owner
 * a description, location and deadline
 * optionally it is also possible to upload a picture (which is saved as reference in the favor)
 *
 * as helpers we store the creation date and the owner email
 *
 * and most importantly: how many token is it worth and for how many people is the favor accessible
 * and last but not least a list of people which are interested
 *
 * A favor is a favor and should have a database entry (since it is not literally one)
 */
public class Favor extends DatabaseEntity {

    // tag for log messages
    private static final String TAG = "FAVOR";
    // identifier for firebase
    private static final String COLLECTION = "favors";

    public enum StringFields implements DatabaseStringField {title, ownerID, description, locationCity, deadline, category, ownerEmail, pictureReference}
    public enum LongFields implements DatabaseLongField {tokenPerPerson, nbPerson}
    public enum ObjectFields implements DatabaseObjectField {location, creationTimestamp, expirationTimestamp, interested}
    public enum BooleanFields implements DatabaseBooleanField {isOpen}

    /**
     * empty constructor as required per firebase
     */
    public Favor(){
        super(StringFields.values(), LongFields.values(), BooleanFields.values(),
                ObjectFields.values(), COLLECTION,null);
    }

    /**
     * This constructor allows to retrieve a favor using its unique id
     *
     * @param id unique id of the favor
     */
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
