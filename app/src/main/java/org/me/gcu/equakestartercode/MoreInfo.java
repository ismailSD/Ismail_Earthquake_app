package org.me.gcu.equakestartercode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Locale;
/**Created by ismail adam on 25/03/2021
 * Student ID: S1908016 */
public class MoreInfo extends AppCompatActivity {
    private TextView info_location;
    private TextView info_date;
    private TextView info_latitude;
    private TextView info_longitude;
    private TextView info_magnitude;
    private TextView info_depth;
    private Item item;
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

        this.item = (Item) moreInfoIntent.getSerializableExtra("item");

        info_location.setText(item.getDescription().getLocation());
        info_date.setText(item.getDescription().getDateTime());
        info_latitude.setText(String.format(Locale.UK, "%.3f",item.getDescription().getLatitude()));
        info_longitude.setText(String.format(Locale.UK, "%.3f",item.getDescription().getLongitude()));
        info_depth.setText(String.format(Locale.UK, "%.3f",item.getDescription().getDepth()));
        info_magnitude.setText(String.format(Locale.UK,"%.3f",item.getDescription().getMagnitude()));
        MapsFragmentZoomed map_fragment = new MapsFragmentZoomed(
                item.getDescription().getLatitude(),
                item.getDescription().getLongitude(),
                item.getDescription().getLocation(),
                item.getDescription().getMagnitude());
        // Open fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.map_frame_zoomed, map_fragment).commit();
    }

    // allows us to navigate back to previous activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

}