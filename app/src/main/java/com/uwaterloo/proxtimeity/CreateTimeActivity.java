package com.uwaterloo.proxtimeity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.TextView;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CreateTimeActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    Calendar reminderDateTime = new GregorianCalendar() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_time);
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
        ((TextView) findViewById(R.id.dateSelected))
                .setText(dateFormat.format(reminderDateTime.getTime()));
    }
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        reminderDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        reminderDateTime.set(Calendar.MINUTE, minute);
        String template = "hh:mm aaa";
        ((TextView) findViewById(R.id.timeSelected))
                .setText(DateFormat.format(template, reminderDateTime.getTime()));
    }

    public void saveTimeReminder(View view) {
        EditText edit = (EditText)findViewById(R.id.editDescription);
        String description = edit.getText().toString();

        TimeReminder reminder = new TimeReminder(reminderDateTime, description);
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
