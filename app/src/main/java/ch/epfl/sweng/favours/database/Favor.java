package ch.epfl.sweng.favours;

public class Favor {

    public enum StringField implements DatabaseStringField{title, ownerID, description,}
    public enum IntegerField implements DatabaseIntField{creationTimestamp}
}
