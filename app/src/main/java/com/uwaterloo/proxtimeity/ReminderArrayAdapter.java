package com.uwaterloo.proxtimeity;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Naima on 2017-06-18.
 */

public class ReminderArrayAdapter extends CustomArrayAdapter<Reminder> {

    private Context mContext;
    private ArrayList<Reminder> mReminders;
    private int mResource;

    public ReminderArrayAdapter(Context context, int resource, ArrayList<Reminder> reminders) {
        super(context, resource, reminders);
        this.mContext = context;
        this.mReminders = reminders;
        this.mResource = resource;
    }

    private class ViewHolder {
        TextView reminderName;
        ImageView picture;
    }

    @Override
    public Object getViewHolder(View rowView) {
        ViewHolder viewHolder = new ViewHolder();

        viewHolder.reminderName = (TextView)rowView.findViewById(R.id.reminder_name);
        viewHolder.picture = (ImageView)rowView.findViewById(R.id.reminder_image);

        return viewHolder;
    }

    @Override
    public void fillViewHolder(Object viewHolder, Reminder data) {
        final ViewHolder mViewHolder = (ViewHolder) viewHolder;
        if (data.reminderName != null) {
            mViewHolder.reminderName.setText(data.reminderName);
        }

        if (data.reminderType == 0) {
            mViewHolder.picture.setImageResource(R.drawable.ic_clock);
        } else {
            mViewHolder.picture.setImageResource(R.drawable.ic_location);
        }

    }
}
