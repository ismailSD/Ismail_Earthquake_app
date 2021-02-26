package org.me.gcu.equakestartercode;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class map_fragment extends Fragment {
    // creating array list for adding all our locations.
    private final ArrayList<LatLng> locationArrayList;
    private final List<Item> items;
    private GoogleMap mMap;
    public map_fragment(List<Item> items){
        this.items = items;
        locationArrayList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        for(Item feed : items){
            String latLong = feed.getDescription().split(";")[2].split(":")[1];

            double latitude = Double.parseDouble(latLong.split(",")[0].trim());
            double longitude = Double.parseDouble(latLong.split(",")[1].trim());
            this.locationArrayList.add(new LatLng(latitude, longitude));
        }

        // initialize view
        View view = inflater.inflate(R.layout.fragment_map_fragment, container, false);

        // initialize map fragment
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.google_map);

        // Async map
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                for(Item feed : items){
                    String latLong = feed.getDescription().split(";")[2].split(":")[1];
                    String location = feed.getDescription().split(";")[1].split(":")[1];
                    double magnitude = Double.parseDouble(feed.getDescription().split(";")[4].split(":")[1]);

                    double latitude = Double.parseDouble(latLong.split(",")[0].trim());
                    double longitude = Double.parseDouble(latLong.split(",")[1].trim());
                    LatLng latLng = new LatLng(latitude, longitude);

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title(location);

                    if(magnitude>=3){
                        // red
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    }else if(magnitude>=2){
                        // orange red
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                    }else {
                        // yellow
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                    }

                    // below line is use to add marker to each location of our array list.
                    mMap.addMarker(markerOptions);

                    // below lin is use to zoom our camera on map.
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(12.2f));

                    // below line is use to move our camera to the specific location.
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                }



                // when map is loaded
//                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//                    @Override
//                    public void onMapClick(LatLng latLng) {
//                        // when clicked on map
//                        // initialize marker
//                        MarkerOptions markerOptions = new MarkerOptions();
//                        // set position of marker
//                        markerOptions.position(latLng);
//                        // set title of marker
//                        markerOptions.title(latLng.latitude+" : "+ latLng.longitude);
//                        // remove all markers
//                        googleMap.clear();
//                        // Animating to zoom the marker
//                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
//                        // add marker on map
//                        googleMap.addMarker(markerOptions);
//                    }
//                });
            }
        });
        return view;
    }
}