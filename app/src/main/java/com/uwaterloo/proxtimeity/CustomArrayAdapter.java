package com.uwaterloo.proxtimeity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by Naima on 2017-06-18.
 */

public abstract class CustomArrayAdapter<T> extends ArrayAdapter<T> {

    // The layout passed in the constructor
    private int resource;

    // Constructor: make sure you call super(context resource, values) in your constructor
    public CustomArrayAdapter(Context context, int resource, ArrayList<T> values) {
        super(context, resource, values);
        this.resource = resource;
    }


    // getView recycles views, uses getViewHolder and fillViewHolder to display data in view
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        // Conditionally inflate view
        Object viewHolder;
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(resource, parent, false);
            viewHolder = getViewHolder(rowView); // setup View Holder
            rowView.setTag(viewHolder); // set tag for recycling
        } else {
            viewHolder = rowView.getTag();
        }

        T data = getItem(position); // get data
        fillViewHolder(viewHolder, data); // Populate View Holder

        return rowView; // Return view (required by super class)
    }

    /**
     * Get an object which contains variables that reference an item's views
     *
     * @param rowView The inflated view. Find views by doing rowView.findViewById(...)
     * @return The object with view references
     */
    public abstract Object getViewHolder(View rowView);

    /**
     * Fill a Viewholder with data
     *
     * @param viewHolder Object configured in getViewHolder
     * @param data the data to add to the display
     */
    public abstract void fillViewHolder(Object viewHolder, T data);

}
