package ch.epfl.sweng.favors.utils.email;

import android.support.annotation.NonNull;

public class Email {
    @NonNull
    private final String from;
    @NonNull
    private final String to;
    private final String subject;
    private final String message;

    /**
     * @param from - the originating source of the email
     * @param to - whom the email will be send
     * @param subject - subject/header of the email
     * @param message - the main body of the email
     */
    public Email(@NonNull String from, @NonNull String to, String subject, String message) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.message = message;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }
}
