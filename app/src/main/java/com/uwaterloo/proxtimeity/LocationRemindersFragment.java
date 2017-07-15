package com.uwaterloo.proxtimeity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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


public class LocationRemindersFragment extends Fragment {

    LocationReminderArrayAdapter adapter;
    ArrayList<LocationReminder> listOfLocationReminders;
    SharedPreferences mPrefs;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceResponse) {
        rootView = inflater.inflate(R.layout.fragment_location_reminders, container, false);
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LocationReminder reminderToBeEdited = listOfLocationReminders.get(position);
                Intent intent = new Intent(getActivity(), CreateLocationActivity.class);
                intent.putExtra("reminder", reminderToBeEdited);
                startActivity(intent);
            }
        });
        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();
        ListView listView = (ListView)rootView.findViewById(R.id.location_reminders_listview);
        listOfLocationReminders = new ArrayList<>();
        mPrefs = getContext().getSharedPreferences("com.uwaterloo.proxtimeity", Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String locationJson = mPrefs.getString("LocationReminders", "");
        Type locationType = new TypeToken<ArrayList<LocationReminder>>(){}.getType();

        if (gson.fromJson(locationJson, locationType) != null)
            listOfLocationReminders = gson.fromJson(locationJson, locationType);

        adapter = new LocationReminderArrayAdapter(getContext(), R.layout.item_reminder ,listOfLocationReminders);
        listView.setAdapter(adapter);
    }
}
