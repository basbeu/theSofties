package ch.epfl.sweng.favours.database;

import ch.epfl.sweng.favours.database.DatabaseIntField;
import ch.epfl.sweng.favours.database.DatabaseStringField;

public class Favor {

    public enum StringField implements DatabaseStringField {title, ownerID, description,}
    public enum IntegerField implements DatabaseIntField {creationTimestamp}
}
