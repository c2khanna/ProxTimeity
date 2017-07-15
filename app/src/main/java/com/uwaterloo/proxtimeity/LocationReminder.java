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

    public LocationReminder(String reminderName, String location,
                            Boolean remindDuringHours, Calendar remindMeBefore) {
        this.reminderName = reminderName;
        this.location = location;
        this.remindDuringHours = remindDuringHours;
        this.remindMeBefore = remindMeBefore;
        this.reminderID = (int) System.currentTimeMillis();
    }
}
