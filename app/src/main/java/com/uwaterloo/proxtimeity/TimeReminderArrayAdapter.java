package com.uwaterloo.proxtimeity;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Naima on 2017-07-09.
 */

public class TimeReminderArrayAdapter extends CustomArrayAdapter<TimeReminder> {

    private Context mContext;
    private ArrayList<TimeReminder> mReminders;
    private int mResource;

    public TimeReminderArrayAdapter(Context context, int resource, ArrayList<TimeReminder> values) {
        super(context, resource, values);
        this.mContext = context;
        this.mReminders = values;
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
    public void fillViewHolder(Object viewHolder, TimeReminder data) {
        final ViewHolder mViewHolder = (ViewHolder) viewHolder;
        if (data.reminderName != null) {
            mViewHolder.reminderName.setText(data.reminderName);
        }
        mViewHolder.picture.setImageResource(R.drawable.ic_clock);
    }
}
