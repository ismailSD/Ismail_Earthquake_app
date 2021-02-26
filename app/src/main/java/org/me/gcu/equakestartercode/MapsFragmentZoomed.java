package org.me.gcu.equakestartercode;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
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


public class MapsFragmentZoomed extends Fragment {

    private GoogleMap mMap;
    private LatLng latLng;
    private String location;
    private double magnitude;
    public MapsFragmentZoomed(LatLng latLng, String location, double magnitude){
        this.latLng = latLng;
        this.location = location;
        this.magnitude = magnitude;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // initialize view
        View view = inflater.inflate(R.layout.fragment_maps_zoomed, container, false);

        // initialize map fragment
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.zoomed_google_map);

        // Async map
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
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
        });
        return view;
    }
}