package ca.ualberta.boost;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

/**
 * DriverMainPage is responsible for displaying all options that the driver has
 * if any of the options (view requests, view profile, view logout) are selected
 * the respective activity is launched
 */

public class DriverMainPage extends MapActivity {

    private static final String TAG = "DriverMainPage";


    private Button viewRequestsButton;
    private Button logoutButton;
    private Button viewProfileButton;

    //firebase
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        viewProfileButton = findViewById(R.id.viewProfileButton);
        viewRequestsButton = findViewById(R.id.viewRequestsButton);
        logoutButton = findViewById(R.id.logoutButton);

    }

    @Override
    protected MapFragment getMapFragment() {
        return (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_driver_main_page;
    }

    @Override
    protected void init() {
        viewRequestsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayRequests();
            }
        });
        viewProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchProfileScreen();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                launchHomeScreen();
            }
        });

    }

    //function to launch the ViewRideRequests Activity
    private void displayRequests(){
        Intent intent = new Intent(this, ViewRideRequestsActivity.class);
        startActivity(intent);
    }

    //function to launch the home screen
    private void launchHomeScreen(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void launchProfileScreen() {
        Intent intent = new Intent(this, PrivateUserProfileActivity.class);
        startActivity(intent);
    }

}

