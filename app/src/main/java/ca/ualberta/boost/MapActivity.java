package ca.ualberta.boost;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class MapActivity extends FragmentActivity implements OnMapReadyCallback {
    // constants
    private static final String TAG = "MapActivity";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final int DEFAULT_ZOOM = 16;

    // attributes
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        getLocationPermission();
    }

    /* abstract methods for subclass to define */
    // get layout resource id of subclass
    protected abstract int getLayoutResourceId();
    // get map fragment in subclass
    protected abstract MapFragment getMapFragment();
    // initialize everything else in subclass
    protected abstract void init();

    /**
     * Initializes the map object
     *
     * @param googleMap
     *      the map object
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(mLocationPermissionsGranted){
            getDeviceLocation();
            // makes a blue dot on the map showing current location
            mMap.setMyLocationEnabled(true);
            // get rid of top right corner button to center location
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mMap.getUiSettings().setMapToolbarEnabled(false);
            // enable all zoom, rotate, tilt, etc gesture
            // mMap.getUiSettings().setAllGesturesEnabled(true);
            init();
        }
    }


    public GoogleMap getMap(){
        return mMap;
    }

    /**
     * Moves an existing marker
     * @param marker
     *      The marker to move
     * @param latLng
     *      the new LatLng position for the marker
     */
    public void moveMarker(Marker marker, LatLng latLng) {
        marker.setPosition(latLng);
        marker.setVisible(true);
        moveCamera(latLng, DEFAULT_ZOOM);
    }

    /**
     * Returns a location given a string
     * @param name
     *      The string of the name/address of the location
     * @return
     *      Returns a LatLng for the location
     */
    public LatLng geoLocate(String name) {
        Geocoder geocoder = new Geocoder(MapActivity.this);
        List<Address> results = new ArrayList<>();
        // get a list of results from the search location string
        try{
            results = geocoder.getFromLocationName(name, 20);
        }catch (IOException e){
            Toast.makeText(MapActivity.this,
                    "Unable to find location", Toast.LENGTH_SHORT).show();
        }
        // successful results
        if (results.size() > 0){
            Address address = results.get(0);
            return new LatLng(address.getLatitude(), address.getLongitude());
        }
        return null;
    }

    /**
     * Returns an address given a LatLng
     * @param latLng
     *      The LatLng of the address
     * @return
     *      Returns a String of the address
     */
    public String reverseGeoLocate(LatLng latLng){
        Geocoder geocoder = new Geocoder(MapActivity.this);
        List<Address> results = new ArrayList<>();
        // get a list of results from the search location string
        try{
            results = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 20);
        }catch (IOException e){
            Toast.makeText(MapActivity.this,
                    "unable to find location", Toast.LENGTH_SHORT).show();
        }
        // successful results
        if (results.size() > 0){
            Address address = results.get(0);
            return address.getAddressLine(0);
        }
        return null;
    }

    /**
     * Builds a url that is used to get the directions between two locations
     * @param startLocation
     *      LatLng of the start location
     * @param endLocation
     *      LatLng of the end location
     * @return
     *      A string of the url
     */
    public String buildUrl(LatLng startLocation, LatLng endLocation){
        String start_str = "origin=" + startLocation.latitude + "," + startLocation.longitude;
        String end_str = "destination=" + endLocation.latitude + "," + endLocation.longitude;
        String url = "https://maps.googleapis.com/maps/api/directions/json?" + start_str + "&"
                + end_str + "&key=" + getString(R.string.google_api_key);
        return url;
    }

    /**
     * Moves the map camera so that it shows all markers that are on the map
     *
     * This method uses code by andr https://stackoverflow.com/users/1820695/andr
     * from an answer to the stack overflow post
     * https://stackoverflow.com/questions/14828217/android-map-v2-zoom-to-show-all-the-markers
     */
    public void zoomToMarkers(Marker marker1, Marker marker2){
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(marker1.getPosition());
        builder.include(marker2.getPosition());
        LatLngBounds bounds = builder.build();
        int padding = 100; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.animateCamera(cu);
    }

    /**
     * Moves the map camera to latitude and longitude at zoom level
     * @param latLng
     *      The LatLng to move to
     * @param zoom
     *      The zoom level of the camera
     */
    public void moveCamera(LatLng latLng, float zoom){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    /**
     * Links the map object to the map fragment
     */
    private void initMap(){
        MapFragment mapFragment = getMapFragment();
        mapFragment.getMapAsync(MapActivity.this);
    }


    /* The following methods are based off of code from the YouTube tutorial series
    "Google Maps & Google Places Android Course"
    (https://www.youtube.com/playlist?list=PLgCYzUzKIBE-vInwQhGSdnbyJ62nixHCt)
    by CodingWithMitch (https://www.youtube.com/channel/UCoNZZLhPuuRteu02rh7bzsw) */

    /**
     *  Gets the device's current location
     */
    public void getDeviceLocation(){
        /* get the device's current location */
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{
            if(mLocationPermissionsGranted){
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()){
                            Location currentLocation = (Location) task.getResult();
                            moveCamera(new LatLng(currentLocation.getLatitude(),
                                    currentLocation.getLongitude()), DEFAULT_ZOOM);
                        }else{
                            Toast.makeText(MapActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        /* look for request permission result */
        mLocationPermissionsGranted = false;
        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0 ){
                    for (int grantResult : grantResults) {
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            return;
                        }
                    }
                    mLocationPermissionsGranted = true;
                    // now we can initialize the map
                    initMap();
                }
            }
        }
    }
}
