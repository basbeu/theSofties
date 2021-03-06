package ch.epfl.sweng.favors.notifications;

import ch.epfl.sweng.favors.database.Favor;

public class Notification {
    private Favor favor;
    private final NotificationType notificationType;

    public Notification(NotificationType notificationType, Favor favor){
        this.notificationType = notificationType;
        if(favor != null){
            this.favor = favor;
        }
    }

    public String toString() {
        return notificationType.getNotificationMessage() + " " + favor.get(Favor.StringFields.title);
    }
}
