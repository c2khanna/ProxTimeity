package com.uwaterloo.proxtimeity;

import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

public class CreateLocationActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    Calendar reminderExpiryDateTime = new GregorianCalendar();
    SharedPreferences mPrefs;
    private FusedLocationProviderClient mFusedLocationClient;

    private PendingIntent mGeofencePendingIntent;
    private ArrayList<Geofence> mGeofenceList = new ArrayList<>();
    private GeofencingClient mGeofencingClient;
//    LatLng GeofenceLatLng1, GeofenceLatLng2;

    LatLng locationLatLng1, locationLatLng2;
    PlaceAutocompleteFragment autocompleteFragment;
    Place selectedPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_location);
        mPrefs = this.getSharedPreferences("com.uwaterloo.proxtimeity", Context.MODE_PRIVATE);

        // set calendar with current time and set text on create screen
        TextView dateSelectedText = (TextView)findViewById(R.id.dateSelected);
        TextView timeSelectedText = (TextView)findViewById(R.id.timeSelected);
        Calendar nowCalendar = Calendar.getInstance();
        final java.text.DateFormat dateFormat = DateFormat.getLongDateFormat(getApplicationContext());
        dateSelectedText.setText(dateFormat.format(nowCalendar.getTime()));
        String template = "hh:mm aaa";
        timeSelectedText.setText(DateFormat.format(template, nowCalendar.getTime()));
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        placeListener();
    }

    public void placeListener(){
        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            locationLatLng1 = new LatLng(location.getLatitude(), location.getLongitude());
                            locationLatLng2 = new LatLng(location.getLatitude(), location.getLongitude());

                            autocompleteFragment.setBoundsBias(new LatLngBounds(
                                    locationLatLng1,
                                    locationLatLng2));
                        }
                    }
                });

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                selectedPlace = place;
                // TODO: Get info about the selected place.
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error
            }
        });
    }
    public void datePicker(View view){
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.show(getSupportFragmentManager(),"date");
    }

    public void timePicker(View view) {
        TimePickerFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "time");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        reminderExpiryDateTime.set(year, month, day);
        final java.text.DateFormat dateFormat = DateFormat.getLongDateFormat(getApplicationContext());
        ((TextView) findViewById(R.id.dateSelected))
                .setText(dateFormat.format(reminderExpiryDateTime.getTime()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        reminderExpiryDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        reminderExpiryDateTime.set(Calendar.MINUTE, minute);
        reminderExpiryDateTime.set(Calendar.SECOND, 0);
        reminderExpiryDateTime.set(Calendar.MILLISECOND, 0);
        String template = "hh:mm aaa";
        ((TextView) findViewById(R.id.timeSelected))
                .setText(DateFormat.format(template, reminderExpiryDateTime.getTime()));
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        mGeofencePendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
        return mGeofencePendingIntent;
    }

    public void saveLocationReminder(View view) {
        EditText edit = (EditText)findViewById(R.id.reminder_description);
        String description = edit.getText().toString();

        String selectedLocation = "";

        if (selectedPlace != null){
            selectedLocation = selectedPlace.getName().toString();
        }
        boolean isChecked = ((CheckBox) findViewById(R.id.check_store_hours)).isChecked();
        LocationReminder reminder = new LocationReminder(description, selectedLocation ,isChecked, reminderExpiryDateTime);

        String json = mPrefs.getString("LocationReminders", "");
        Type type = new TypeToken<ArrayList<LocationReminder>>(){}.getType();
        Gson gson = new Gson();
        ArrayList<LocationReminder> LocationReminders = new ArrayList<>();
        if (gson.fromJson(json, type) != null)
            LocationReminders = gson.fromJson(json, type);

        LocationReminders.add(reminder);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        String newJson = gson.toJson(LocationReminders);
        prefsEditor.putString("LocationReminders", newJson);
        prefsEditor.apply();

        Intent notificationIntent = new Intent(this, AlarmReceiver.class);
        notificationIntent.putExtra("reminder name", reminder.reminderName);
        notificationIntent.putExtra("reminder type", "Location Based Reminder");
        final int uniqueID = (int) System.currentTimeMillis();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, uniqueID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // set alarm
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        if(alarmManager != null) {
            long futureInMillis = reminder.remindMeBefore.getTimeInMillis();
            Log.d("the time", Long.toString(futureInMillis));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, futureInMillis, pendingIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, futureInMillis, pendingIntent);
            }
        } else {
            Toast.makeText(this.getApplicationContext(), "Alarm is null", Toast.LENGTH_SHORT).show();
        }

        mGeofencingClient = LocationServices.getGeofencingClient(this);

        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            return;
        }
//        boolean locationTracking = mPrefs.getBoolean("LocationTracking", false);

//        if (!locationTracking) {
//        prefsEditor.putBoolean("LocationTracking", true);
        UUID idOne = UUID.randomUUID();

//        mFusedLocationClient.getLastLocation()
//                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
//                    @Override
//                    public void onSuccess(Location location) {
//                        Log.i("SUCCES!!: ", "Location Value!");
//                        Log.i("Location is Null: ", Boolean.toString((location == null)));
//                        // Got last known location. In some rare situations this can be null.
//                        if (location != null) {
//                            GeofenceLatLng1 = new LatLng(location.getLatitude(), location.getLongitude());
//                        }
//                    }
//                });


        if (selectedPlace != null) {
            LatLng selectedLatLng = selectedPlace.getLatLng();
            mGeofenceList.add(new Geofence.Builder()
                    // Set the request ID of the geofence. This is a string to identify this
                    // geofence.
                    .setRequestId(idOne.toString())
                    .setCircularRegion(
                            selectedLatLng.latitude,
                            selectedLatLng.longitude,
                            2000
                    )
                    .setExpirationDuration(60 * 60 * 1000)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                            Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build());

//        }

            mGeofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                    .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Geofences added
                            Log.i("SUCCESS: ", "Geofence added");
                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Failed to add geofences
                            Log.i("FAILURE: ", "Geofence FAILED TO ADD");
                        }
                    });
        }

        //return to Home screen
        Intent goToMainScreen = new Intent(this, MainActivity.class);
        goToMainScreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(goToMainScreen);
        finish();
    }

    public static class DatePickerFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(),
                    (DatePickerDialog.OnDateSetListener)
                            getActivity(), year, month, day);
        }
    }

    public static class TimePickerFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(), (TimePickerDialog.OnTimeSetListener)
                    getActivity(), hour, minute,
                    false);
        }
    }
}
