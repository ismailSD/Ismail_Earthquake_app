package org.me.gcu.equakestartercode;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ItemAdapter extends ArrayAdapter implements View.OnClickListener{
    private static final String TAG = "FeedAdapter";
    private final int layoutResource;
    private final LayoutInflater layoutInflater;
    private ArrayList<Item> items;

    public ItemAdapter(Context context, int resource, ArrayList<Item> items){
        super(context, resource);
        this.layoutResource = resource;
        this.layoutInflater = LayoutInflater.from(context);
        this.items = items;
    }

    // override two methods of the ArrayAdapter base class
    @Override
    public int getCount() {
        // returns the number of items in our list
        return items.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        // this is the constraint layout that holds the 3 text views
        // param parent is the list_record which allows us access to the entries
        if(convertView == null){
            // reusing the view
            convertView = layoutInflater.inflate(layoutResource, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Item currentItem = items.get(position);

        String location = currentItem.getDescription().split(";")[1].split(":")[1].toLowerCase();
        double magnitude = Double.parseDouble(currentItem.getDescription().split(";")[4].split(":")[1]);

        viewHolder.textViewStrength.setText(String.format(Locale.UK, "%.2f",magnitude));
        viewHolder.textViewLocation.setText(location);
        if(magnitude>=3){
            // red
            viewHolder.textViewStrength.setTextColor(Color.rgb(255,236, 255));
            viewHolder.textViewLocation.setTextColor(Color.rgb(255,236, 255));
            convertView.setBackgroundColor(Color.rgb(236, 38, 0));
        }else if(magnitude>=2){
            // orange red
            viewHolder.textViewStrength.setTextColor(Color.rgb(0, 0, 0));
            viewHolder.textViewLocation.setTextColor(Color.rgb(0, 0, 0));
            convertView.setBackgroundColor(Color.rgb(236, 156, 0));
        }else {
            // yellow
            viewHolder.textViewStrength.setTextColor(Color.rgb(0, 0, 0));
            viewHolder.textViewLocation.setTextColor(Color.rgb(0, 0, 0));
            convertView.setBackgroundColor(Color.rgb(198, 236, 0));
        }
        return convertView;
    }

    @Override
    public void onClick(View v) {
        System.out.println("You clicked on item:::::::::::::::::::::::::");
    }


    private class ViewHolder{
        private final TextView textViewStrength;
        private final TextView textViewLocation;
        public ViewHolder(View v){
            this.textViewStrength = (TextView) v.findViewById(R.id.textViewStrength);
            this.textViewLocation = (TextView) v.findViewById(R.id.textViewLocation);
        }

    }


    public List<Item> getItems() {
        return items;
    }
}
