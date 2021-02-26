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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.N)
public class SearchData extends AppCompatActivity implements View.OnClickListener{
    final Calendar myCalendar = Calendar.getInstance();
    private static final String TAG = "SearchData";
    private ArrayList<Item> items;
    private EditText date_from;
    private EditText date_to;
    private Button btn_search;

    double maxMagnitude, minMagnitude, maxDepth, minDepth;
    String furthestNorth, furthestSouth, furthestEast, furthestWest;

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


    @Override
    public void onClick(View v) {
        if(v==btn_search){
            System.out.println("Date from: "+date_from.getText().toString());
            System.out.println("Date to: "+date_to.getText().toString());
            double[] maxMinData = getMaxMinData(items);
            System.out.println(Arrays.toString(maxMinData));
        }
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
}