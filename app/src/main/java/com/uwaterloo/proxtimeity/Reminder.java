package com.uwaterloo.proxtimeity;

/**
 * Created by Naima on 2017-06-18.
 */

public class Reminder {

    int reminderType = 0; // default is 0 = time based, 1 = location based
    String reminderName = "";

    public Reminder() {
    }

    public Reminder(int type, String name){
        reminderType = type;
        reminderName = name;
    }

}
