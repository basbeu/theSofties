package ch.epfl.sweng.favors.favors;

public enum NotificationType {
    INTEREST("Someone is interested in your favor"),
    PAYMENT("You have been paid for the favor"),
    EXPIRATION("Your favor is now expired"),
    SELECTION("You have been selected for the favor");


    private final String notificationMessage;

    NotificationType(String s){
        this.notificationMessage = s;
    }

    public String getNotificationMessage(){
        return notificationMessage;
    }

    }
