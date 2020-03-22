package ca.ualberta.boost;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.MapFragment;
import com.google.firebase.auth.FirebaseAuth;

/**
 * DriverMainPage is responsible for displaying all options that the driver has
 * if any of the options (view requests, view profile, view logout) are selected
 * the respective activity is launched
 */

public class DriverMainPage extends MapActivity {

    private static final String TAG = "DriverMainPage";

    // views
    private Button viewRequestsButton;
    private Button logoutButton;
    private Button viewProfileButton;

    // firebase
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
        //finish();
    }

    //function to launch the home screen
    private void launchHomeScreen(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void launchProfileScreen() {
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }

}

