package com.uwaterloo.proxtimeity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;

public class CreateLocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_location);
        Spinner locationSpinner = (Spinner) findViewById(R.id.locations_spinner);
    }

    public void saveLocationReminder(View view) {
        return;
    }
}
