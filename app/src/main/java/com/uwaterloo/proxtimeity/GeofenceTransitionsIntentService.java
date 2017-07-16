package com.uwaterloo.proxtimeity;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by andrewchung on 2017-07-12.
 */

public class GeofenceTransitionsIntentService extends IntentService {
    private static final String TAG = GeofenceTransitionsIntentService.class.getSimpleName();
    SharedPreferences mPrefs;
    public GeofenceTransitionsIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            String errorMessage = Integer.toString(geofencingEvent.getErrorCode());
            Log.e(TAG, errorMessage);
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {

            // Get the geofences that were triggered. A single event can trigger
            // multiple geofences.
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            mPrefs = this.getSharedPreferences("com.uwaterloo.proxtimeity", Context.MODE_PRIVATE);
            String json = mPrefs.getString("Geofences", "");
            Type type = new TypeToken<ArrayList<GeofenceData>>(){}.getType();
            Gson gson = new Gson();
            ArrayList<GeofenceData> geofences = new ArrayList<>();
            GeofenceData mGeofence;
            if (gson.fromJson(json, type) != null)
                geofences = gson.fromJson(json, type);

            // Get the transition details as a String.
            String reminderDesc = "Location Reached!";
            Log.i(TAG, (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) ? "Geofence Entered for: " : "Geofence Exited for: ");
            for (Geofence g : triggeringGeofences) {
                Log.i(TAG, g.getRequestId());
                for (GeofenceData g2 : geofences) {
                    if (g2.name.equals(g.getRequestId().toString())) {
                        reminderDesc = g2.description;
                    }
                }
            }

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.ic_location)
                            .setContentTitle("Location Based Reminder")
                            .setContentText(reminderDesc)
                            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setDefaults(Notification.DEFAULT_ALL);
            int mNotificationId = 1;

            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            mNotificationManager.notify(mNotificationId, mBuilder.build());

            // Send notification and log the transition details.
//            sendNotification(geofenceTransitionDetails);
//            Log.i(TAG, geofenceTransitionDetails);

        } else {
            // Log the error.
//            Log.e(TAG, getString(R.string.geofence_transition_invalid_type,
//                    geofenceTransition));
            Log.e(TAG, "GEOFENCE ERROR!");
        }
    }
}
