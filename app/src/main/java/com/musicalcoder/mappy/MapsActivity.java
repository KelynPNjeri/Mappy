package com.musicalcoder.mappy;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        //Checking for connectivity.
        if(mLocationManager.isProviderEnabled(mLocationManager.NETWORK_PROVIDER)) {
            mLocationManager.requestLocationUpdates(mLocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    //Getting Location Longitude.
                    double longitude = location.getLongitude();
                    //Getting Location Latitude.
                    double latitude = location.getLatitude();

                    settingLocationDetails(latitude, longitude);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                    Toast.makeText(MapsActivity.this, "Failed 1", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }

            });

        }else if(mLocationManager.isProviderEnabled(mLocationManager.GPS_PROVIDER)) {
            mLocationManager.requestLocationUpdates(mLocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    //Getting Location Longitude.
                    double longitude = location.getLongitude();
                    //Getting Location Latitude.
                    double latitude = location.getLatitude();

                    settingLocationDetails(latitude, longitude);
                    Toast.makeText(MapsActivity.this, "GPS Provided Location.", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }

            });

        }

    }

    //Setting
    public void settingLocationDetails(double latitude, double longitude) {
        //Instantiating LatLng class.
        LatLng myLocation = new LatLng(latitude, longitude);
        //Instantiating Geocode Object.
        Geocoder geocoder = new Geocoder(getApplicationContext());
        try {
            List<Address> locations= geocoder.getFromLocation(latitude, longitude, 1);
            String exactLocation = locations.get(0).getLocality() + ",";
            exactLocation += locations.get(0).getCountryName();
            mMap.addMarker(new MarkerOptions().position(myLocation).title(exactLocation));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15f));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

    }
}
