package ch.epfl.sweng.favors.database;

import ch.epfl.sweng.favors.database.fields.DatabaseBooleanField;
import ch.epfl.sweng.favors.database.fields.DatabaseLongField;
import ch.epfl.sweng.favors.database.fields.DatabaseObjectField;
import ch.epfl.sweng.favors.database.fields.DatabaseStringField;

public class Interest extends DatabaseEntity {

    private static final String TAG = "DB_INTEREST";
    private static final String COLLECTION = "interests";

    public enum StringFields implements DatabaseStringField {title, description}
    public enum LongFields implements DatabaseLongField {}
    public enum ObjectFields implements DatabaseObjectField {linkedInterests}
    public enum BooleanFields implements DatabaseBooleanField {}


    public Interest(){
        super(StringFields.values(), LongFields.values(), BooleanFields.values(),
                ObjectFields.values(), COLLECTION,null);
    }

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
