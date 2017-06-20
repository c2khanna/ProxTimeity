package com.uwaterloo.proxtimeity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class CreateLocationActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    Calendar reminderExpiryDateTime = new GregorianCalendar();
    SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_location);
        mPrefs = this.getSharedPreferences("com.uwaterloo.proxtimeity", Context.MODE_PRIVATE);
        Spinner locationSpinner = (Spinner) findViewById(R.id.locations_spinner);

        // set calendar with current time and set text on create screen
        TextView dateSelectedText = (TextView)findViewById(R.id.dateSelected);
        TextView timeSelectedText = (TextView)findViewById(R.id.timeSelected);
        Calendar nowCalendar = Calendar.getInstance();
        final java.text.DateFormat dateFormat = DateFormat.getLongDateFormat(getApplicationContext());
        dateSelectedText.setText(dateFormat.format(nowCalendar.getTime()));
        String template = "hh:mm aaa";
        timeSelectedText.setText(DateFormat.format(template, nowCalendar.getTime()));
    }

    public void datePicker(View view){
        CreateTimeActivity.DatePickerFragment fragment = new CreateTimeActivity.DatePickerFragment();
        fragment.show(getSupportFragmentManager(),"date");
    }

    public void timePicker(View view) {
        CreateTimeActivity.TimePickerFragment newFragment = new CreateTimeActivity.TimePickerFragment();
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
        String template = "hh:mm aaa";
        ((TextView) findViewById(R.id.timeSelected))
                .setText(DateFormat.format(template, reminderExpiryDateTime.getTime()));
    }

    public void saveLocationReminder(View view) {
        EditText edit = (EditText)findViewById(R.id.reminder_description);
        String description = edit.getText().toString();
        Spinner locationDropdown = (Spinner)findViewById(R.id.locations_spinner);
        String selectedLocation = locationDropdown.getSelectedItem().toString();
        boolean isChecked = ((CheckBox) findViewById(R.id.check_store_hours)).isChecked();
        LocationReminder reminder = new LocationReminder(description, selectedLocation ,isChecked, reminderExpiryDateTime);

        String json = mPrefs.getString("allReminders", "");
        Type type = new TypeToken<ArrayList<Reminder>>(){}.getType();
        Gson gson = new Gson();
        ArrayList<Reminder> allReminders = gson.fromJson(json, type);

        allReminders.add(reminder);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        String newJson = gson.toJson(allReminders);
        prefsEditor.putString("allReminders", newJson);
        prefsEditor.commit();

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
