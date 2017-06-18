package com.uwaterloo.proxtimeity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ChooseTypeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_type);
    }

    public void goToTimeActivity(View view) {
        Intent goToTimeActivity = new Intent(ChooseTypeActivity.this, CreateTimeActivity.class);
        startActivity(goToTimeActivity);
    }

    public void goToLocationActivity(View view) {
        Intent goToLocationActivity = new Intent(ChooseTypeActivity.this, CreateLocationActivity.class);
        startActivity(goToLocationActivity);
    }
}
