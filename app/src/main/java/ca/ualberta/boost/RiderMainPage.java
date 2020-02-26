package ca.ualberta.boost;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* This class is partly based off of code from the YouTube tutorial series
    "Google Maps & Google Places Android Course"
    (https://www.youtube.com/playlist?list=PLgCYzUzKIBE-vInwQhGSdnbyJ62nixHCt)
    by CodingWithMitch (https://www.youtube.com/channel/UCoNZZLhPuuRteu02rh7bzsw) */

public class RiderMainPage extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "RiderMainPage";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final int DEFAULT_ZOOM = 16;

    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private Button requestRideButton;
    private Button viewProfileButton;
    private Button confirmRequestButton;
    private Button cancelRequestButton;
    private EditText searchPickupText;
    private EditText searchDestinationText;
    private LinearLayout searchesLayout;
    private LinearLayout confirmCancelLayout;
    private LinearLayout viewRequestLayout;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_main_page);

        searchPickupText = findViewById(R.id.searchPickupEditText);
        searchDestinationText = findViewById(R.id.searchDestinationEditText);
        searchesLayout = findViewById(R.id.searchesLayout);
        requestRideButton = findViewById(R.id.requestRideButton);
        viewProfileButton = findViewById(R.id.viewProfileButton);
        confirmCancelLayout = findViewById(R.id.confirmCancelLayout);
        viewRequestLayout = findViewById(R.id.viewRequestLayout);
        confirmRequestButton = findViewById(R.id.confirmRequestButton);
        cancelRequestButton = findViewById(R.id.cancelRequestButton);
        
        getLocationPermission();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(mLocationPermissionsGranted){
            getDeviceLocation();
            // makes a blue dot on the map showing current location
            mMap.setMyLocationEnabled(true);
            // get rid of top right corner button to center location
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            // enable all zoom, rotate, tilt, etc gesture
            // mMap.getUiSettings().setAllGesturesEnabled(true);
            
            init();
            
        }
    }

    private void init() {
        requestRideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRequestRideClick();
            }
        });
        cancelRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleCancelRideClick();
            }
        });

    }

    private void handleRequestRideClick() {
        setRequestLocationPageVisibility();
        // pickup search bar
        searchPickupText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        || event.getAction() == KeyEvent.KEYCODE_ENTER){
                    geoLocate(searchPickupText, "Pickup");
                }
                return false;
            }
        });
        // destination search bar
        searchDestinationText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        || event.getAction() == KeyEvent.KEYCODE_ENTER){
                    geoLocate(searchDestinationText, "Destination");
                }
                return false;
            }
        });
    }

    private void geoLocate(EditText searchEditText, String markerTitle) {
        String searchString = searchEditText.getText().toString();
        Geocoder geocoder = new Geocoder(RiderMainPage.this);
        List<Address> results = new ArrayList<>();
        try{
            results = geocoder.getFromLocationName(searchString, 1);
        }catch (IOException e){
            // show error message that location was not found
            // THIS never happens even if no location shows up ?
            Toast.makeText(this, "Could not find location", Toast.LENGTH_SHORT).show();
        }
        if (results.size() > 0){
            Address address = results.get(0);
            //searchEditText.setText(address.getFeatureName());
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            moveCamera(latLng, DEFAULT_ZOOM);

            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(markerTitle);
            mMap.addMarker(options);

        }
    }
    
    private void handleCancelRideClick() {
        setRiderMainPageVisibility();
        searchDestinationText.setText("");
        searchPickupText.setText("");
    }

    private void setRequestLocationPageVisibility() {
        viewRequestLayout.setVisibility(View.GONE);
        confirmCancelLayout.setVisibility(View.VISIBLE);
        searchesLayout.setVisibility(View.VISIBLE);
    }

    private void setRiderMainPageVisibility() {
        viewRequestLayout.setVisibility(View.VISIBLE);
        confirmCancelLayout.setVisibility(View.GONE);
        searchesLayout.setVisibility(View.GONE);
    }


    private void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(RiderMainPage.this);
    }

    
    
    private void getDeviceLocation(){
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
                            Toast.makeText(RiderMainPage.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    private void moveCamera(LatLng latLng, float zoom){
        /* moves the camera to latitude and longitude at zoom level */
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
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
