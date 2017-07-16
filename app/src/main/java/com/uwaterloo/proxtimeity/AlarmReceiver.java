package com.uwaterloo.proxtimeity;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import static android.content.ContentValues.TAG;
import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by andrewchung on 2017-06-20.
 */

public class AlarmReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            NotificationCompat.Builder noti =
                    new NotificationCompat.Builder(context)
                            .setContentTitle(intent.getStringExtra("reminder type"))
                            .setContentText(intent.getStringExtra("reminder name"))
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setPriority(Notification.PRIORITY_HIGH);

            if(intent.hasExtra("reminder type") && intent.getStringExtra("reminder type").equals("Location Based Reminder")) {
                noti.setSmallIcon(R.drawable.ic_location);
            } else {
                noti.setSmallIcon(R.drawable.ic_clock);
            }

            int mNotificationId = 001;
            NotificationManager mNotifyMgr =
                    (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
            mNotifyMgr.notify(mNotificationId, noti.build());
        }
    }
}
