package ch.epfl.sweng.favors.favors;

import ch.epfl.sweng.favors.database.Favor;

public class Notification {
    private Favor favor;
    private enum NOTIF_TYPE {INTEREST, PAYMENT, EXPIRATION, SELECTION};
}
