package com.uwaterloo.proxtimeity;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by chaitanyakhanna on 2017-06-18.
 */

public class TimeReminder extends Reminder {
    Calendar reminderTime = new GregorianCalendar();

    public TimeReminder(Calendar reminder, String description){
        this.reminderTime = reminder;
        this.reminderName = description;
        this.reminderType = 0;
    }
}
