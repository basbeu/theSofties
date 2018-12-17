package ch.epfl.sweng.favors.notifications;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import ch.epfl.sweng.favors.R;

public class PushNotifications extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";

    @Override
    public void onNewToken(String s) {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();
        sendNotification(title, body, this);
    }

    public void sendNotification(String title, String body, Context context){
        if(context == null)
            throw new IllegalArgumentException();

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, context.getString(R.string.default_notification_channel_id))
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle(title)
                        .setContentText(body);


        int mNotificationId = (int)System.currentTimeMillis();

        NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }


}
