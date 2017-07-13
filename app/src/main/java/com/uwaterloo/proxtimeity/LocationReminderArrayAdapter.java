package com.uwaterloo.proxtimeity;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Naima on 2017-07-09.
 */

public class LocationReminderArrayAdapter extends CustomArrayAdapter<LocationReminder> {

    private Context mContext;
    private ArrayList<LocationReminder> mReminders;
    private int mResource;

    public LocationReminderArrayAdapter(Context context, int resource, ArrayList<LocationReminder> values) {
        super(context, resource, values);
        this.mContext = context;
        this.mReminders = values;
        this.mResource = resource;
    }

    private class ViewHolder {
        TextView reminderName;
        ImageView picture;
        TextView reminderLocation;
    }

    @Override
    public Object getViewHolder(View rowView) {
        ViewHolder viewHolder = new ViewHolder();

        viewHolder.reminderName = (TextView)rowView.findViewById(R.id.reminder_name);
        viewHolder.picture = (ImageView)rowView.findViewById(R.id.reminder_image);
        viewHolder.reminderLocation = (TextView) rowView.findViewById(R.id.reminder_details);

        return viewHolder;
    }

    @Override
    public void fillViewHolder(Object viewHolder, LocationReminder data) {
        final ViewHolder mViewHolder = (ViewHolder) viewHolder;
        if (data.reminderName != null) {
            mViewHolder.reminderName.setText(data.reminderName);
        }
        String reminderLocation = "At " + data.location;
        mViewHolder.picture.setImageResource(R.drawable.ic_location);
        mViewHolder.reminderLocation.setText(reminderLocation);
    }
}
