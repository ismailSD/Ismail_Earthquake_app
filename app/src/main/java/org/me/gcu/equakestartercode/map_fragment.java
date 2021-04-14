package org.me.gcu.equakestartercode;

import android.os.Bundle;

import androidx.annotation.Nullable;
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

import java.util.List;
/**Created by ismail adam on 25/03/2021
 * Student ID: S1908016 */

/**Map fragment class allows the displaying of the earthquake locations */
public class map_fragment extends Fragment{
    // Creating array list for adding all of the locations.
    private  static  List<Item> items;
    private GoogleMap mMap;
    public map_fragment(){}
    public map_fragment(List<Item> items){
        map_fragment.items = items;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // initialize the view and the map fragment
        View view = inflater.inflate(R.layout.fragment_map_fragment, container, false);
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.google_map);

        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                if(items !=null){
                    for(Item feed : items){

                        String location = feed.getDescription().getLocation();
                        double magnitude = feed.getDescription().getMagnitude();

                        double latitude = feed.getDescription().getLatitude();
                        double longitude = feed.getDescription().getLongitude();
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

                        // below line is use to zoom our camera on map.
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(12.2f));

                        // below line is use to move our camera to the specific location.
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    }

                }

            }
        });
        return view;
    }

}