package com.uwaterloo.proxtimeity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class LocationRemindersFragment extends Fragment {

    LocationReminderArrayAdapter adapter;
    ArrayList<LocationReminder> listOfLocationReminders;
    SharedPreferences mPrefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceResponse) {
        View rootView = inflater.inflate(R.layout.fragment_location_reminders, container, false);
        ListView listView = (ListView)rootView.findViewById(R.id.location_reminders_listview);
        listOfLocationReminders = new ArrayList<>();
        mPrefs = getContext().getSharedPreferences("com.uwaterloo.proxtimeity", Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String timeJson = mPrefs.getString("LocationReminders", "");
        Type locationType = new TypeToken<ArrayList<LocationReminder>>(){}.getType();

        if (gson.fromJson(timeJson, locationType) != null)
            listOfLocationReminders = gson.fromJson(timeJson, locationType);

        adapter = new LocationReminderArrayAdapter(getContext(), R.layout.item_reminder ,listOfLocationReminders);
        listView.setAdapter(adapter);
        return rootView;
    }
}
