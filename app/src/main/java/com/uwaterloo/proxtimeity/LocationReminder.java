package com.uwaterloo.proxtimeity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.location.places.Place;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by chaitanyakhanna on 2017-06-18.
 */

public class LocationReminder extends Reminder implements Parcelable{
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

    public LocationReminder(Parcel in){
        this.reminderID = in.readLong();
        this.reminderName = in.readString();
        Calendar tempCal = new GregorianCalendar();
        tempCal.setTimeInMillis(in.readLong());
        this.remindMeBefore = tempCal;
        this.remindDuringHours = in.readByte() != 0;
        this.location = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.reminderID);
        dest.writeString(this.reminderName);
        dest.writeLong(this.remindMeBefore.getTimeInMillis());
        dest.writeByte((byte) (this.remindDuringHours ? 1 : 0));
        dest.writeString(this.location);
    }

    public static final Creator CREATOR = new Creator() {
        public LocationReminder createFromParcel(Parcel in) {
            return new LocationReminder(in);
        }
        public LocationReminder[] newArray(int size) {
            return new LocationReminder[size];
        }
    };
}
