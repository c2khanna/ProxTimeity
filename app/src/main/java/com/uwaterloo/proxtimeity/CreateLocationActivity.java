package com.uwaterloo.proxtimeity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class CreateLocationActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    Calendar reminderExpiryDateTime = new GregorianCalendar();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_location);
        Spinner locationSpinner = (Spinner) findViewById(R.id.locations_spinner);
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
        EditText edit = (EditText)findViewById(R.id.editDescription);
        String description = edit.getText().toString();
        boolean isChecked = ((CheckBox) findViewById(R.id.check_store_hours)).isChecked();

        LocationReminder reminder = new LocationReminder(description, isChecked, reminderExpiryDateTime);
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
