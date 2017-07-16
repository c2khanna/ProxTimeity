package com.uwaterloo.proxtimeity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class CompletedTimeRemindersFragment extends Fragment {

    TimeReminderArrayAdapter adapter;
    ArrayList<TimeReminder> listOfTimeReminders;
    SharedPreferences mPrefs;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceResponse) {
        rootView = inflater.inflate(R.layout.fragment_completed_time_reminders, container, false);
        ListView listView = (ListView)rootView.findViewById(R.id.completed_time_reminders_listview);
        listOfTimeReminders = new ArrayList<>();
        mPrefs = getContext().getSharedPreferences("com.uwaterloo.proxtimeity", Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String timeJson = mPrefs.getString("CompletedTimeReminders", "");
        Type timeType = new TypeToken<ArrayList<TimeReminder>>(){}.getType();

        if (gson.fromJson(timeJson, timeType) != null)
            listOfTimeReminders = gson.fromJson(timeJson, timeType);

        adapter = new TimeReminderArrayAdapter(getContext(), R.layout.item_reminder ,listOfTimeReminders);
        listView.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();
        ListView listView = (ListView)rootView.findViewById(R.id.completed_time_reminders_listview);
        listOfTimeReminders = new ArrayList<>();
        mPrefs = getContext().getSharedPreferences("com.uwaterloo.proxtimeity", Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String timeJson = mPrefs.getString("CompletedTimeReminders", "");
        Type timeType = new TypeToken<ArrayList<TimeReminder>>(){}.getType();

        if (gson.fromJson(timeJson, timeType) != null)
            listOfTimeReminders = gson.fromJson(timeJson, timeType);

        adapter = new TimeReminderArrayAdapter(getContext(), R.layout.item_reminder ,listOfTimeReminders);
        listView.setAdapter(adapter);
    }
}
