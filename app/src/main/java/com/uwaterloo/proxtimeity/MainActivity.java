package com.uwaterloo.proxtimeity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Reminder> listOfReminders = new ArrayList<>();
    ArrayList<TimeReminder> listOfTimeReminders = new ArrayList<>();
    ArrayList<LocationReminder> listOfLocationReminders = new ArrayList<>();
    ReminderArrayAdapter reminderAdapter;
    SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPrefs = this.getSharedPreferences("com.uwaterloo.proxtimeity", Context.MODE_PRIVATE);
        Gson gson = new Gson();

        String timeJson = mPrefs.getString("TimeReminders", "");
        String locationJson = mPrefs.getString("LocationReminders", "");
        Type locationType = new TypeToken<ArrayList<LocationReminder>>(){}.getType();
        Type timeType = new TypeToken<ArrayList<TimeReminder>>(){}.getType();

        if (gson.fromJson(timeJson, timeType) != null)
            listOfTimeReminders = gson.fromJson(timeJson, timeType);

        if (gson.fromJson(locationJson, locationType) != null)
            listOfLocationReminders = gson.fromJson(locationJson, locationType);

        reminderAdapter = new ReminderArrayAdapter(this, R.layout.item_reminder ,listOfReminders);
        ListView listView = (ListView) findViewById(R.id.listOfReminders);
        listView.setAdapter(reminderAdapter);

    }

    public void goToChooseType(View view) {
        Intent goToChooseScreen = new Intent(MainActivity.this, ChooseTypeActivity.class);
        startActivity(goToChooseScreen);
    }

}
