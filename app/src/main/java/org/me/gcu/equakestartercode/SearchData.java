package org.me.gcu.equakestartercode;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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

        setData(this.items);
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
                    new DatePickerDialog(SearchData.this, date,
                            myCalendar.get(Calendar.YEAR),
                            myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
                return true;
            }

        });
    }
    private void updateLabel(EditText editText) {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
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
        if(v==btn_search){
            String startDate = date_from.getText().toString();
            String endDate = date_to.getText().toString();

            try {
                // Create SimpleDateFormat object
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

                // Get the two dates to be compared
                Date date_from = simpleDateFormat.parse(startDate);
                Date date_to = simpleDateFormat.parse(endDate);

                // Compare the dates using compareTo()
                if (date_from.compareTo(date_to) > 0) {
                    // When Date date_from > Date date_to display invalid date selection message
                    System.out.println("Date_from is greater than Date_to!!");
                }else {
                    // get information based on selected data.................
                    //System.out.println(":::::::::::::Item from first of feburary to 26 feburary::::::::::::::::");
                    ArrayList<Item> selectedData = selectedData(this.items, startDate, endDate);
                    //for (Item feed : selectedData) System.out.println(feed.getDescription().split(";")[0].split(":")[1].trim());
                    //System.out.println(":::::::::::::Item from first of feburary to 26 feburary::::::::::::::::");

                    setData(selectedData);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void setData(ArrayList<Item> items){
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
        ArrayList<Item> tempItems = new ArrayList<>();

        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("EEEE, dd MMMM yyyy");


        for(Item feed : items){
            String feedDate = feed.getDescription().split(";")[0].split(":")[1].trim();
            // Get the two dates to be compared
            try{
                Date date_From = simpleDateFormat1.parse(dateFrom);
                Date date_to = simpleDateFormat1.parse(dateTo);
                Date feed_date = simpleDateFormat2.parse(feedDate);

                if (feed_date.compareTo(date_From) >= 0 && feed_date.compareTo(date_to) <=0) {
                    // When feed_date >= date_from  and <= date_to then add this item to the array list
                    //System.out.println("feed_date is >= date_from");
                    tempItems.add(feed);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return tempItems;
    }
    private double[] getMaxMinData(ArrayList<Item> items){
        // arr 0 = max magnitude
        // arr 1 = min magnitude

        // arr 2 = max depth
        // arr 3 = min depth

        double[] temp = new double[4];
        temp[1] = 100;// starting as min
        temp[3] = 100;// starting as min

        for(Item feed : items){
            double magnitude = Double.parseDouble(feed.getDescription().split(";")[4].split(":")[1]);
            if(temp[0]<magnitude) temp[0] = magnitude;// max
            if(temp[1]>magnitude) temp[1] = magnitude;// min

            double depth = Double.parseDouble(feed.getDescription().split(";")[3].split(":")[1].trim().substring(0,1));
            if(temp[2]<depth) temp[2] = depth;
            if(temp[3]>depth) temp[3] = depth;
        }

        return temp;
    }
    private String[] directions(ArrayList<Item> items){
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
            String latLong = feed.getDescription().split(";")[2].split(":")[1];
            String location = feed.getDescription().split(";")[1].split(":")[1];
            double latitude = Double.parseDouble(latLong.split(",")[0].trim());
            double longitude = Double.parseDouble(latLong.split(",")[1].trim());
            //

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
}