package com.example.assignment3;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.assignment3.networkconnection.NetworkConnection;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

public class GoogleMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng userLocation;                            // user location LatLng
    private HashMap<String, LatLng> cinemaLocationPair = new HashMap<>();     // cinemaName-cinemaLatLng
    // network connection
    NetworkConnection networkConnection = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_maps_fragment);

        // network connection
        networkConnection = new NetworkConnection();

        Intent intent = getIntent();
        // load user data from intent
        String userLocation = intent.getStringExtra("userCoord");
        String[] latlong= userLocation.split(",");
        double latitude = Double.parseDouble(latlong[0]);
        double longitude = Double.parseDouble(latlong[1]);
        this.userLocation = new LatLng(latitude, longitude);

        // load cinema data from intent
        String[] cinemaNames = intent.getStringArrayListExtra("cinemaName").toArray(new String[0]);
        String[] cinemaLatLng = intent.getStringArrayListExtra("cinemaLatLng").toArray(new String[0]);

        for(int i = 0; i < cinemaLatLng.length; i++) {
            String[] latlng = cinemaLatLng[i].split(",");
            double lat = Double.parseDouble(latlng[0]);
            double lon = Double.parseDouble(latlng[1]);
            LatLng cinemaLoction = new LatLng(lat, lon);

            cinemaLocationPair.put(cinemaNames[i], cinemaLoction);
        }


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // load user location
        mMap.addMarker(new MarkerOptions().position(userLocation).title("My location"));

        // load cinema location
        for(String key : cinemaLocationPair.keySet()) {
            mMap.addMarker(new MarkerOptions().position(cinemaLocationPair.get(key)).
                    icon(BitmapDescriptorFactory.defaultMarker(180.0f)).
                    title(key.replace("Australia", "")));
        }

        //icon(BitmapDescriptorFactory.defaultMarker(180.0f)).

        //mMap.moveCamera(CameraUpdateFactory.newLatLng(currentlocation));
         /*Zoom levels
         1: World
         5: Landmass/continent
         10: City
         15: Streets
         20: Buildings
         */
        float zoomLevel = (float) 10.0;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,
                zoomLevel));


        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
