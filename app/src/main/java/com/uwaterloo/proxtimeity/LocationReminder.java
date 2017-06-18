package com.uwaterloo.proxtimeity;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by chaitanyakhanna on 2017-06-18.
 */

public class LocationReminder extends Reminder {
    String location = "";
    Boolean remindDuringHours = Boolean.FALSE;
    Calendar remindMeBefore = new GregorianCalendar();

    public LocationReminder(String location, Boolean remindDuringHours) {
        this.location = location;
        this.remindDuringHours = remindDuringHours;
    }
}
