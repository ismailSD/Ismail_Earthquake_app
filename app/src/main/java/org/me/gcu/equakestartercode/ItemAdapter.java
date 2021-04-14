package org.me.gcu.equakestartercode;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
/**Created by ismail adam on 25/03/2021
<<<<<<< HEAD
  * Student ID: S1908016 */
=======
 * Student ID: S1908016 */
>>>>>>> aeeb3a9aaf837857fafb0b71e2a812b136b9e452

/** Custom array adapter, allows the displaying of multiple items in a single ListItem.
 * the list_item.xml created will be used to display the
 * earthquake's strength and location in a single listItem (list_item.xml).
 */
<<<<<<< HEAD
public class ItemAdapter extends ArrayAdapter implements View.OnClickListener, Serializable {
=======
public class ItemAdapter extends ArrayAdapter implements View.OnClickListener{
>>>>>>> aeeb3a9aaf837857fafb0b71e2a812b136b9e452
    // Set the fields to final so that they don't get accidentally changed later
    private static final String TAG = "FeedAdapter";
    private final int layoutResource;
    private final LayoutInflater layoutInflater;
    private ArrayList<Item> items;

    public ItemAdapter(Context context, int resource){
        super(context, resource);
        this.layoutResource = resource;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public void notifyDataSetChanged() {
        bubbleSort(this.items);
        super.notifyDataSetChanged();
    }

    private void bubbleSort(ArrayList<Item> items){
        // Sort the list in descending oder based on Magnitude
        for (int lastUnsortedIndex = items.size()-1;
             lastUnsortedIndex>0; lastUnsortedIndex--){

            for (int i = 0; i<lastUnsortedIndex; i++){

                if (items.get(i).getDescription().getMagnitude() <
                        items.get(i+1).getDescription().getMagnitude()){
                    swap(items, i, i+1);
                }
            }
        }
    }
    private  void swap(ArrayList<Item> items, int i, int j){
        if (i==j)return;//there is nothing to swap

        Item temp = items.get(i);

        items.set(i, items.get(j));
        items.set(j, temp);
    }

    @Override
    public int getCount() {
        // return the number of items in the list
        return items.size();
    }

<<<<<<< HEAD
    // This method is automatically called by the ListView each time another item
=======
    // this method is automatically called by the ListView each time another item
>>>>>>> aeeb3a9aaf837857fafb0b71e2a812b136b9e452
    // is needs to be displayed
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Create ViewHolder object
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = layoutInflater.inflate(layoutResource, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Get the data item for this position.
        // extract the location and magnitude data from the currentItem.
        // Set these extracted data to their corresponding widgets by using the
        // viewHolder object
        Item currentItem = items.get(position);
        String location = currentItem.getDescription().getLocation().toLowerCase();
        double magnitude = currentItem.getDescription().getMagnitude();
        viewHolder.textViewStrength.setText(String.format(Locale.UK, "%.2f",magnitude));
        viewHolder.textViewLocation.setText(location);

        // Colour code the list items based on the earthquake's strength
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

    /** */
    private static class ViewHolder{
        private final TextView textViewStrength;
        private final TextView textViewLocation;
        public ViewHolder(View v){
            // find the widgets (textViewStrength, textViewLocation) by using
            // the View passed to this constructor and referencing the their IDs
            // then store these widgets to this class's equivalent fields
            this.textViewStrength = (TextView) v.findViewById(R.id.textViewStrength);
            this.textViewLocation = (TextView) v.findViewById(R.id.textViewLocation);
        }
    }

    public List<Item> getItems() {
        return items;
    }
    public void setItems(ArrayList<Item> items){
        this.items = items;
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> aeeb3a9aaf837857fafb0b71e2a812b136b9e452
