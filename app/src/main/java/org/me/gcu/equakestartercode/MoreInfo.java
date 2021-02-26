package org.me.gcu.equakestartercode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

public class MoreInfo extends AppCompatActivity {
    private TextView info_location;
    private TextView info_date;
    private TextView info_latitude;
    private TextView info_longitude;
    private TextView info_magnitude;
    private TextView info_depth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // initialize the fields
        info_location = (TextView) findViewById(R.id.info_location);
        info_date = (TextView) findViewById(R.id.info_date);
        info_latitude = (TextView) findViewById(R.id.info_latitude);
        info_longitude = (TextView) findViewById(R.id.info_longitude);
        info_magnitude = (TextView) findViewById(R.id.info_magnitude);
        info_depth = (TextView) findViewById(R.id.info_depth);

        Intent moreInfoIntent = getIntent();

        String data = moreInfoIntent.getStringExtra("item");

        String location = data.split(";")[1].split(":")[1].toLowerCase();
        String date = data.split(";")[0].split(":")[1];
        String latitude = data.split(";")[2].split(":")[1].split(",")[0];
        String longitude = data.split(";")[2].split(":")[1].split(",")[1];
        String depth = data.split(";")[3].split(":")[1];
        String magnitude = data.split(";")[4].split(":")[1];

        info_location.setText(location);
        info_date.setText(date);
        info_latitude.setText(latitude);
        info_longitude.setText(longitude);
        info_depth.setText(depth);
        info_magnitude.setText(magnitude);

        //:::::::::pass item to map::::::::::::
        // initial map_fragment class
        LatLng latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));

        MapsFragmentZoomed map_fragment = new MapsFragmentZoomed(latLng, location, Double.parseDouble(magnitude));
        // Open fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.map_frame_zoomed, map_fragment).commit();
        //:::::::::::pass items to map::::::::::::::::::
    }
    // allows us to navigate back to previous activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }
}