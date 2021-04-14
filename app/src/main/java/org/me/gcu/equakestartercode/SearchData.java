package org.me.gcu.equakestartercode;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
/**Created by ismail adam on 25/03/2021
 * Student ID: S1908016 */

/** This Activity class allows the searching and displaying of specific
 * earthquake filtered by date*/
@RequiresApi(api = Build.VERSION_CODES.N)
public class SearchData extends AppCompatActivity implements View.OnClickListener{
    final Calendar myCalendar = Calendar.getInstance();
    private static final String TAG = "SearchData";
    private ArrayList<Item> items;
    private EditText date_from;
    private EditText date_to;
    private Button btn_search;

    private TextView furthest_north;
    private TextView furthest_south;
    private TextView furthest_west;
    private TextView furthest_east;

    private TextView max_magnitude;
    private TextView min_magnitude;
    private TextView max_depth;
    private TextView min_depth;

    private LocalData localData;
    private String SAVED_RESULT="saved_result";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_data);
        Intent searchData = getIntent();
        this.items = new ArrayList<>();
        this.items =  (ArrayList<Item>) searchData.getSerializableExtra("items");

        date_from = (EditText) findViewById(R.id.date_from);
        date_to = (EditText) findViewById(R.id.date_to);
        date_from.setFocusable(false);
        date_to.setFocusable(false);
        btn_search = (Button) findViewById(R.id.btn_search);
        btn_search.setOnClickListener(this);
        showCalendar(date_from);
        showCalendar(date_to);

        furthest_north = (TextView) findViewById(R.id.furthest_north);
        furthest_south = (TextView) findViewById(R.id.furthest_south);
        furthest_west = (TextView) findViewById(R.id.furthest_west);
        furthest_east = (TextView) findViewById(R.id.furthest_east);

        max_magnitude = (TextView) findViewById(R.id.max_magnitude);
        min_magnitude = (TextView) findViewById(R.id.min_magnitude);
        max_depth = (TextView) findViewById(R.id.max_depth);
        min_depth = (TextView) findViewById(R.id.min_depth);
    }
    @SuppressLint("ClickableViewAccessibility")
    private void showCalendar(EditText editText){
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int  dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(editText);
            }
        };
        editText.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    DatePickerDialog dDialog = new DatePickerDialog(SearchData.this, date,
                            myCalendar.get(Calendar.YEAR),
                            myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH));

                    long now = System.currentTimeMillis();
                    Calendar fiftyDaysAgo = (Calendar) Calendar.getInstance();
                    fiftyDaysAgo.setTimeInMillis(now);

                    // allow the user to select from the last fifty days only
                    fiftyDaysAgo.add(Calendar.DATE, -50);

                    // set the maximum date the user can select to the current date
                    dDialog.getDatePicker().setMaxDate(now);

                    if(editText == date_to){

                        if(date_from.getText().toString().isEmpty()){
                            displayError("Please select From date first!");
                        }else {
                            dDialog.getDatePicker().setMinDate(myCalendar.getTimeInMillis());
                            dDialog.show();
                        }
                    }else {
                        dDialog.getDatePicker().setMinDate(fiftyDaysAgo.getTimeInMillis());
                        dDialog.show();
                    }
                }
                return true;
            }

        });
    }
    private void updateLabel(EditText editText) {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
        editText.setText(sdf.format(myCalendar.getTime()));
    }


    // allows us to navigate back to previous activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        // check if the btn_search button is clicked
        // if so, get the dates selected from the fields and validate
        // if validation failed, display error message by using the displayError() method created.
        if(v==btn_search){
            String startDate = date_from.getText().toString();
            String endDate = date_to.getText().toString();

            if(date_from.getText().toString().isEmpty() || date_to.getText().toString().isEmpty()){
                displayError("Please select date ranges first!");
                return;
            }

            try {
                // Create SimpleDateFormat object.
                // Get the two dates to be compared.
                // Compare the dates, if date_from > Date date_to,
                // then display invalid date selection error message
                // otherwise get information within the selected dates
                // if no information found within the selected dates then display
                // "no date found within the selected dates" message
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date date_from = simpleDateFormat.parse(startDate);
                Date date_to = simpleDateFormat.parse(endDate);

                if (date_from.compareTo(date_to) > 0) {
                    displayError("Date from should not be greater than date to!");
                }else {
                    ArrayList<Item> selectedData = selectedData(this.items, startDate, endDate);
                    if(selectedData.size()<=0){
                        displayError("No information found within the selected date range!");

                    }else {
                        setData(selectedData);
                    }
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void setData(ArrayList<Item> items){
        // assign the search results to their corresponding fields
        this.max_magnitude.setText("");
        this.min_magnitude.setText("");
        this.max_depth.setText("");
        this.min_depth.setText("");

        this.furthest_north.setText("");
        this.furthest_south.setText("");
        this.furthest_west.setText("");
        this.furthest_east.setText("");

        double[] maxMinData = getMaxMinData(items);
        this.max_magnitude.setText(String.format(Locale.UK, "%.3f", maxMinData[0]));
        this.min_magnitude.setText(String.format(Locale.UK, "%.3f", maxMinData[1]));
        this.max_depth.setText(String.format(Locale.UK, "%.3f km", maxMinData[2]));
        this.min_depth.setText(String.format(Locale.UK, "%.3f km", maxMinData[3]));

        String[] directions = directions(items);
        this.furthest_north.setText(directions[0]);
        this.furthest_south.setText(directions[1]);
        this.furthest_west.setText(directions[2]);
        this.furthest_east.setText(directions[3]);
    }

    private ArrayList<Item> selectedData(ArrayList<Item> items, String dateFrom, String dateTo){
        // compare and select only the items within the specified dates

        ArrayList<Item> tempItems = new ArrayList<>();

        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("EEEE, dd MMMM yyyy");


        for(Item feed : items){
            String feedDate = feed.getDescription().getDateTime().trim();
            // Get the two dates to be compared
            try{
                Date date_From = simpleDateFormat1.parse(dateFrom);
                Date date_to = simpleDateFormat1.parse(dateTo);
                Date feed_date = simpleDateFormat2.parse(feedDate);

                if (feed_date.compareTo(date_From) >= 0 && feed_date.compareTo(date_to) <=0) {
                    // When feed_date >= date_from  and <= date_to then add this item to the array list
                    tempItems.add(feed);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return tempItems;
    }
    private double[] getMaxMinData(ArrayList<Item> items){
        // get the maximum, minimum magnitudes as well as depths
        // arr 0 = max magnitude
        // arr 1 = min magnitude

        // arr 2 = max depth
        // arr 3 = min depth

        double[] temp = new double[4];
        temp[1] = 100;// starting as min
        temp[3] = 100;// starting as min

        for(Item feed : items){
            double magnitude = feed.getDescription().getMagnitude();
            if(temp[0]<magnitude) temp[0] = magnitude;// max
            if(temp[1]>magnitude) temp[1] = magnitude;// min

            double depth = feed.getDescription().getDepth();
            if(temp[2]<depth) temp[2] = depth;
            if(temp[3]>depth) temp[3] = depth;
        }

        return temp;
    }
    private String[] directions(ArrayList<Item> items){
        // get most northerly, southerly, westerly and easterly
        // earthquakes
        String furthestNorth = "";
        String furthestSouth = "";

        String furthestWest = "";
        String furthestEast = "";
        String[] temp = new String[4];


        double mostPositiveLatitude = 0.0;// furthest north
        double mostNegativeLatitude = 100.0;// furthest south

        double mostPositiveLongitude =  0.0;// furthest east
        double mostNegativeLongitude = 100.0;// furthest west

        for(Item feed : items){
            String location = feed.getDescription().getLocation();
            double latitude = feed.getDescription().getLatitude();
            double longitude = feed.getDescription().getLongitude();

            if(mostPositiveLatitude < latitude){
                //Furthest north
                mostPositiveLatitude = latitude;
                furthestNorth = location.toLowerCase()+" "+latitude;
            }

            if(mostNegativeLatitude > latitude){
                //Furthest south
                mostNegativeLatitude = latitude;
                furthestSouth = location.toLowerCase()+" "+latitude;
            }

            if(mostPositiveLongitude < longitude){
                mostPositiveLongitude = longitude;
                //Furthest North
                furthestEast = location.toLowerCase()+" "+longitude;
            }
            if(mostNegativeLongitude > longitude){
                //Furthest South
                mostNegativeLongitude = longitude;
                furthestWest = location.toLowerCase()+" "+longitude;
            }

        }

        temp[0] = furthestNorth;
        temp[1] = furthestSouth;

        temp[2] = furthestWest;
        temp[3] = furthestEast;

        return temp;
    }
    private void restoreDate(){
        // reassign the fields with the saved data
        this.max_magnitude.setText(localData.getMax_magnitude());
        this.min_magnitude.setText(localData.getMin_magnitude());
        this.max_depth.setText(localData.getMax_depth());
        this.min_depth.setText(localData.getMin_depth());

        this.furthest_north.setText(localData.getFurthest_north());
        this.furthest_south.setText(localData.getFurthest_south());
        this.furthest_west.setText(localData.getFurthest_west());
        this.furthest_east.setText(localData.getFurthest_east());
    }
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.localData = (LocalData)savedInstanceState.getSerializable(SAVED_RESULT);
        restoreDate();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        // save the search results of earthquake data in SAVED_RESULT
        // so that they can be restored from it when the device rotated
        localData = new LocalData(furthest_north.getText().toString(),
                furthest_south.getText().toString(),
                furthest_west.getText().toString(),
                furthest_east.getText().toString(),
                max_magnitude.getText().toString(),
                min_magnitude.getText().toString(),
                max_depth.getText().toString(),
                min_depth.getText().toString());
        outState.putSerializable(SAVED_RESULT, localData);
        super.onSaveInstanceState(outState);
    }

    /** allows the storing of currently displayed earthquake's filtered data
        in a state so that when the device is rotated the data is not lost
     */
    private static class LocalData implements Serializable{
        private String furthest_north;
        private String furthest_south;
        private String furthest_west;
        private String furthest_east;

        private String max_magnitude;
        private String min_magnitude;
        private String max_depth;
        private String min_depth;

        public LocalData(String furthest_north,
                         String furthest_south,
                         String furthest_west,
                         String furthest_east,
                         String max_magnitude,
                         String min_magnitude,
                         String max_depth,
                         String min_depth) {
            this.furthest_north = furthest_north;
            this.furthest_south = furthest_south;
            this.furthest_west = furthest_west;
            this.furthest_east = furthest_east;
            this.max_magnitude = max_magnitude;
            this.min_magnitude = min_magnitude;
            this.max_depth = max_depth;
            this.min_depth = min_depth;
        }
        public String getFurthest_north() {
            return furthest_north;
        }

        public void setFurthest_north(String furthest_north) {
            this.furthest_north = furthest_north;
        }

        public String getFurthest_south() {
            return furthest_south;
        }

        public void setFurthest_south(String furthest_south) {
            this.furthest_south = furthest_south;
        }

        public String getFurthest_west() {
            return furthest_west;
        }

        public void setFurthest_west(String furthest_west) {
            this.furthest_west = furthest_west;
        }

        public String getFurthest_east() {
            return furthest_east;
        }

        public void setFurthest_east(String furthest_east) {
            this.furthest_east = furthest_east;
        }

        public String getMax_magnitude() {
            return max_magnitude;
        }

        public void setMax_magnitude(String max_magnitude) {
            this.max_magnitude = max_magnitude;
        }

        public String getMin_magnitude() {
            return min_magnitude;
        }

        public void setMin_magnitude(String min_magnitude) {
            this.min_magnitude = min_magnitude;
        }

        public String getMax_depth() {
            return max_depth;
        }

        public void setMax_depth(String max_depth) {
            this.max_depth = max_depth;
        }

        public String getMin_depth() {
            return min_depth;
        }

        public void setMin_depth(String min_depth) {
            this.min_depth = min_depth;
        }

    }


    public void displayError(String message){
        // display error message in a Toast widget
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.custom_toast_layout));
        TextView tv = (TextView) layout.findViewById(R.id.txtvw);
        tv.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL | Gravity.START, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}