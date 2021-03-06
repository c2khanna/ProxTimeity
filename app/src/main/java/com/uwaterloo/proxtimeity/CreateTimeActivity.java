package com.uwaterloo.proxtimeity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CreateTimeActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    Calendar reminderDateTime = new GregorianCalendar() ;
    SharedPreferences mPrefs;
    TimeReminder reminderToBeEdited;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_time);
        reminderToBeEdited = getIntent().getParcelableExtra("reminder");
        mPrefs = this.getSharedPreferences("com.uwaterloo.proxtimeity", Context.MODE_PRIVATE);
        TextView dateSelectedText = (TextView)findViewById(R.id.date_selected);
        TextView timeSelectedText = (TextView)findViewById(R.id.time_selected);
        EditText description = (EditText)findViewById(R.id.time_reminder_description);

        if(reminderToBeEdited == null) {
            // set calendar with current time and set text on create screen
            Calendar nowCalendar = Calendar.getInstance();
            final java.text.DateFormat dateFormat = DateFormat.getLongDateFormat(getApplicationContext());
            dateSelectedText.setText(dateFormat.format(nowCalendar.getTime()));
            String template = "hh:mm aaa";
            timeSelectedText.setText(DateFormat.format(template, nowCalendar.getTime()));
        }
        else{
            final java.text.DateFormat dateFormat = DateFormat.getLongDateFormat(getApplicationContext());
            dateSelectedText.setText(dateFormat.format(reminderToBeEdited.reminderTime.getTime()));
            description.setText(reminderToBeEdited.reminderName);
            String template = "hh:mm aaa";
            timeSelectedText.setText(DateFormat.format(template, reminderToBeEdited.reminderTime.getTime()));
            reminderDateTime = reminderToBeEdited.reminderTime;
        }
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
    public void onDateSet(DatePicker view, int year, int month, int date) {
        reminderDateTime.set(year, month, date);
        final java.text.DateFormat dateFormat = DateFormat.getLongDateFormat(getApplicationContext());
        ((TextView) findViewById(R.id.date_selected))
                .setText(dateFormat.format(reminderDateTime.getTime()));
    }
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        reminderDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        reminderDateTime.set(Calendar.MINUTE, minute);
        reminderDateTime.set(Calendar.SECOND, 0);
        reminderDateTime.set(Calendar.MILLISECOND, 0);
        String template = "hh:mm aaa";
        ((TextView) findViewById(R.id.time_selected))
                .setText(DateFormat.format(template, reminderDateTime.getTime()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(reminderToBeEdited == null)
            getMenuInflater().inflate(R.menu.menu_no_delete_time, menu);
        else
            getMenuInflater().inflate(R.menu.menu_delete_time, menu);

        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.time_delete) {
            deleteTimeReminder(reminderToBeEdited);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteTimeReminder(TimeReminder reminderToBeDeleted){
        ArrayList<TimeReminder> timeReminders = new ArrayList<>();
        String json = mPrefs.getString("TimeReminders", "");
        Type type = new TypeToken<ArrayList<TimeReminder>>(){}.getType();
        Gson gson = new Gson();
        if (gson.fromJson(json, type) != null)
            timeReminders = gson.fromJson(json, type);

        for(int i = 0; i < timeReminders.size(); i++){
            if(timeReminders.get(i).reminderID == reminderToBeDeleted.reminderID){
                timeReminders.remove(i);
                break;
            }
        }

        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        String newJson = gson.toJson(timeReminders);
        prefsEditor.putString("TimeReminders", newJson);
        prefsEditor.apply();

        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent(this, AlarmReceiver.class);
        notificationIntent.putExtra("reminder name", reminderToBeDeleted.reminderName);
        notificationIntent.putExtra("reminder type", "Time Based Reminder");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int)reminderToBeDeleted.reminderID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
    }

    public void saveTimeReminder(View view) {
        EditText edit = (EditText)findViewById(R.id.time_reminder_description);
        String description = edit.getText().toString();

        TimeReminder reminder = new TimeReminder(reminderDateTime, description);

        String json = mPrefs.getString("TimeReminders", "");
        Type type = new TypeToken<ArrayList<TimeReminder>>(){}.getType();
        Gson gson = new Gson();
        ArrayList<TimeReminder> timeReminders = new ArrayList<>();
        if (gson.fromJson(json, type) != null)
            timeReminders = gson.fromJson(json, type);

        timeReminders.add(reminder);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        String newJson = gson.toJson(timeReminders);
        prefsEditor.putString("TimeReminders", newJson);
        prefsEditor.apply();

        Intent notificationIntent = new Intent(this, AlarmReceiver.class);
        notificationIntent.putExtra("reminder name", reminder.reminderName);
        notificationIntent.putExtra("reminder type", "Time Based Reminder");
        notificationIntent.putExtra("reminderObject", reminder);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int)reminder.reminderID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // set alarm
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        if(alarmManager != null) {
            long futureInMillis = reminder.reminderTime.getTimeInMillis();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, futureInMillis, pendingIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, futureInMillis, pendingIntent);
            }
        } else {
            Toast.makeText(this.getApplicationContext(), "Alarm is null", Toast.LENGTH_SHORT).show();
        }

        if(reminderToBeEdited != null){
            deleteTimeReminder(reminderToBeEdited);
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
