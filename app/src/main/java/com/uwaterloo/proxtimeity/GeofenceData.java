package com.uwaterloo.proxtimeity;

/**
 * Created by andrewchung on 2017-07-13.
 */

public class GeofenceData {
    String name;
    String latitude;
    String longitude;
    String radius;
    String description;

    public GeofenceData() {

    }

    @Override
    public String toString() {
        return name + '\n' + latitude + '\n' + longitude + '\n' + radius + '\n' + description + '\n';
    }
}
