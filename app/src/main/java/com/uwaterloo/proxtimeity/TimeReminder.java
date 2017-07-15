package com.uwaterloo.proxtimeity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.SynchronousQueue;

/**
 * Created by chaitanyakhanna on 2017-06-18.
 */

public class TimeReminder extends Reminder implements Parcelable{
    Calendar reminderTime = new GregorianCalendar();

    public TimeReminder(Calendar reminder, String description){
        this.reminderTime = reminder;
        this.reminderName = description;
        this.reminderID = System.currentTimeMillis();
    }

    public TimeReminder(Parcel in){
        this.reminderID = in.readLong();
        this.reminderName = in.readString();
        Calendar tempCal = new GregorianCalendar();
        tempCal.setTimeInMillis(in.readLong());
        this.reminderTime = tempCal;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.reminderID);
        dest.writeString(this.reminderName);
        dest.writeLong(this.reminderTime.getTimeInMillis());
    }

    public static final Creator CREATOR = new Creator() {
        public TimeReminder createFromParcel(Parcel in) {
            return new TimeReminder(in);
        }
        public TimeReminder[] newArray(int size) {
            return new TimeReminder[size];
        }
    };
}
