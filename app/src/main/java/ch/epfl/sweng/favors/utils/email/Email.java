package ch.epfl.sweng.favors.utils.email;

import android.content.Context;
import android.support.annotation.NonNull;

public class Email {
    @NonNull
    private final String from;
    @NonNull
    private final String to;
    private final String subject;
    private final String message;
    @NonNull
    private final Context context;
    @NonNull
    private final String successMsg;
    @NonNull
    private final String failureMsg;

    /**
     * @param from - the originating source of the email
     * @param to - whom the email will be send
     * @param subject - subject/header of the email
     * @param message - the main body of the email
     * @param context - the current context that is using this method
     * @param successMsg - text that will be displayed as a toast if the email is successfully send
     * @param failureMsg - text that will be displayed as a toast if the email fails to be send
     */
    public Email(@NonNull String from, @NonNull String to, String subject, String message, @NonNull Context context, @NonNull String successMsg, @NonNull String failureMsg) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.message = message;
        this.context = context;
        this.successMsg = successMsg;
        this.failureMsg = failureMsg;
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

    public Context getContext() {
        return context;
    }

    public String getSuccessMsg() {
        return successMsg;
    }

    public String getFailureMsg() {
        return failureMsg;
    }
}
