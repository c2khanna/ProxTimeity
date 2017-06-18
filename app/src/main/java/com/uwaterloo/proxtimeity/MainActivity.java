package com.uwaterloo.proxtimeity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Reminder> listOfReminders = new ArrayList<>();
    ReminderArrayAdapter reminderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listOfReminders.add(new Reminder(0, "Testing"));
        listOfReminders.add(new Reminder(1, "Testing"));

        reminderAdapter = new ReminderArrayAdapter(this, R.layout.item_reminder ,listOfReminders);
        ListView listView = (ListView) findViewById(R.id.listOfReminders);
        listView.setAdapter(reminderAdapter);


    }

    public void goToChooseType(View view) {
        Intent goToChooseScreen = new Intent(MainActivity.this, ChooseTypeActivity.class);
        startActivity(goToChooseScreen);
    }


}
