package jp.co.benesse.touch.common.anrsendservice;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import jp.co.benesse.touch.common.anrsendservice.util.Logger;

public class AnrSendNotification {
    private static final String CHANNEL_ID = "ANR_SEND_NOTIFICATION";
    private static final String NOTIFICATION_CHANNEL_DESCRIPTION = "Shows notifications whenever work starts";
    private static final int NOTIFICATION_ID = 1;
    private static final String NOTIFICATION_MESSAGE = "ANR sending.";
    private static final String TAG = AnrSendNotification.class.getSimpleName();
    private static final CharSequence NOTIFICATION_CHANNEL_NAME = "ANR send WorkManager Notifications";
    private static final CharSequence NOTIFICATION_TITLE = "ANR send WorkRequest";

    public void makeNotification(Context context) {
        Logger.d(TAG, "makeNotification 001");
        if (Build.VERSION.SDK_INT >= 26) {
            Logger.d(TAG, "makeNotification 002");
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, 2);
            notificationChannel.setDescription(NOTIFICATION_CHANNEL_DESCRIPTION);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
            if (notificationManager != null) {
                Logger.d(TAG, "makeNotification 003");
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
        NotificationManagerCompat.from(context).notify(1, new NotificationCompat.Builder(context, CHANNEL_ID).setSmallIcon(R.drawable.ic_transparent).setContentTitle(NOTIFICATION_TITLE).setContentText(NOTIFICATION_MESSAGE).setPriority(1).setVisibility(0).setNotificationSilent().build());
        Logger.d(TAG, "makeNotification 004");
    }

    public void cancelNotification(Context context) {
        Logger.d(TAG, "cancelNotification 001");
        NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
        if (notificationManager != null) {
            Logger.d(TAG, "cancelNotification 002");
            notificationManager.cancel(1);
        }
        Logger.d(TAG, "cancelNotification 003");
    }
}
