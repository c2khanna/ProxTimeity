package com.uwaterloo.proxtimeity;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import static android.content.ContentValues.TAG;
import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by andrewchung on 2017-06-20.
 */

public class AlarmReceiver extends BroadcastReceiver {

    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";

    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "TRIGGERED!!!");
        System.out.println("ASDF!@#");
        Toast.makeText(context, "Alarm Triggered", Toast.LENGTH_SHORT).show();

    }
}
