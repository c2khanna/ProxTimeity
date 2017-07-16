package com.uwaterloo.proxtimeity;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by andrewchung on 2017-06-20.
 */

public class AlarmReceiver extends BroadcastReceiver {
    String typeOfReminder;
    SharedPreferences mPrefs;
    public void onReceive(Context context, Intent intent) {
        mPrefs = context.getSharedPreferences("com.uwaterloo.proxtimeity", Context.MODE_PRIVATE);

        // trigger notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            NotificationCompat.Builder noti =
                    new NotificationCompat.Builder(context)
                            .setContentTitle(intent.getStringExtra("reminder type"))
                            .setContentText(intent.getStringExtra("reminder name"))
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setPriority(Notification.PRIORITY_HIGH);

            if(intent.hasExtra("reminder type") && intent.getStringExtra("reminder type").equals("Location Based Reminder")) {
                typeOfReminder = "location";
                noti.setSmallIcon(R.drawable.ic_location);
            } else {
                typeOfReminder = "time";
                noti.setSmallIcon(R.drawable.ic_clock);
            }

            int mNotificationId = 001;
            NotificationManager mNotifyMgr =
                    (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
            mNotifyMgr.notify(mNotificationId, noti.build());
        }

        // set the reminder as completed by removing it from active reminders list
        // to completed reminders list in shared preferences

        if(typeOfReminder.equals("time")) {
            TimeReminder completedReminder = intent.getParcelableExtra("reminderObject");

            // remove reminder from active list
            ArrayList<TimeReminder> timeReminders = new ArrayList<>();
            String json = mPrefs.getString("TimeReminders", "");
            Type type = new TypeToken<ArrayList<TimeReminder>>(){}.getType();
            Gson gson = new Gson();
            if (gson.fromJson(json, type) != null)
                timeReminders = gson.fromJson(json, type);

            for(int i = 0; i < timeReminders.size(); i++){
                if(timeReminders.get(i).reminderID == completedReminder.reminderID){
                    timeReminders.remove(i);
                    break;
                }
            }
            // save the updated list of active reminders to shared preferences
            SharedPreferences.Editor prefsEditor = mPrefs.edit();
            String newJson = gson.toJson(timeReminders);
            prefsEditor.putString("TimeReminders", newJson);
            prefsEditor.apply();

            // add reminder to completed list
            ArrayList<TimeReminder> completedTimeReminders = new ArrayList<>();
            String completedJSON = mPrefs.getString("CompletedTimeReminders", "");
            Type completedType = new TypeToken<ArrayList<TimeReminder>>(){}.getType();
            Gson completedGson = new Gson();
            if (completedGson.fromJson(completedJSON, completedType) != null)
                completedTimeReminders = completedGson.fromJson(completedJSON, completedType);

            completedTimeReminders.add(completedReminder);

            // save the updated list of completed reminders to shared preferences
            SharedPreferences.Editor prefsEditorCompleted = mPrefs.edit();
            String completedJSONString = completedGson.toJson(completedTimeReminders);
            prefsEditorCompleted.putString("CompletedTimeReminders", completedJSONString);
            prefsEditorCompleted.apply();
        } else {
            // location based reminder
            LocationReminder completedReminder = intent.getParcelableExtra("reminderObject");

            // remove reminder from active list
            ArrayList<LocationReminder> locationReminders = new ArrayList<>();
            String json = mPrefs.getString("LocationReminders", "");
            Type type = new TypeToken<ArrayList<LocationReminder>>(){}.getType();
            Gson gson = new Gson();
            if (gson.fromJson(json, type) != null)
                locationReminders = gson.fromJson(json, type);

            for(int i = 0; i < locationReminders.size(); i++){
                if(locationReminders.get(i).reminderID == completedReminder.reminderID){
                    locationReminders.remove(i);
                    break;
                }
            }
            // save the updated list of active reminders to shared preferences
            SharedPreferences.Editor prefsEditor = mPrefs.edit();
            String newJson = gson.toJson(locationReminders);
            prefsEditor.putString("LocationReminders", newJson);
            prefsEditor.apply();

            // add reminder to completed list
            ArrayList<LocationReminder> completedLocationReminders = new ArrayList<>();
            String completedJSON = mPrefs.getString("CompletedLocationReminders", "");
            Type completedType = new TypeToken<ArrayList<LocationReminder>>(){}.getType();
            Gson completedGson = new Gson();
            if (completedGson.fromJson(completedJSON, completedType) != null)
                completedLocationReminders = completedGson.fromJson(completedJSON, completedType);

            completedLocationReminders.add(completedReminder);

            // save the updated list of completed reminders to shared preferences
            SharedPreferences.Editor prefsEditorCompleted = mPrefs.edit();
            String completedJSONString = completedGson.toJson(completedLocationReminders);
            prefsEditorCompleted.putString("CompletedLocationReminders", completedJSONString);
            prefsEditorCompleted.apply();

        }
    }
}
