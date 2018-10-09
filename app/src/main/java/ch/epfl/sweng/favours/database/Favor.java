package ch.epfl.sweng.favours.database;

public class Favor extends DatabaseHandler {

    public enum StringFields implements DatabaseStringField {title, ownerID, description}
    public enum IntegerFields implements DatabaseIntField {creationTimestamp}

    public Favor(){
        super(StringFields.values(), "favor",null);
    }

}
