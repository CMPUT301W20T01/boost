package ca.ualberta.boost;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Collection;

import ca.ualberta.boost.models.ActiveUser;
import ca.ualberta.boost.models.Promise;
import ca.ualberta.boost.models.Ride;
import ca.ualberta.boost.models.User;
import ca.ualberta.boost.stores.RideStore;

/**
 * DriverMainPage is responsible for displaying all options that the driver has
 * if any of the options (view requests, view profile, view logout) are selected
 * the respective activity is launched
 */

public class DriverMainPage extends MapActivity implements DriverAcceptedFragment.OnFragmentInteractionListener {

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
        checkForActiveRequest();
        checkForPendingDriverAcceptedRequest();
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
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
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

    // method to check if user is involved in an active request
    private void checkForActiveRequest(){
        final User user = ActiveUser.getUser();
        Promise<Collection<Ride>> ridePromise = RideStore.getActiveRides();
        ridePromise.addOnSuccessListener(new OnSuccessListener<Collection<Ride>>() {
            @Override
            public void onSuccess(Collection<Ride> rides) {
                if (!rides.isEmpty()) {
                    for (Ride ride : rides){
                        // user is driver for the active ride
                        if (ride.getDriverUsername().equals(user.getUsername())){
                            ActiveUser.setCurrentRide(ride);
                            launchCurrentRequestActivity();
                        }
                    }
                } else {
                    Log.d("TestingViewRide", "rides is empty");
                }
            }
        });
        ridePromise.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TestingViewRide", "failure");
            }
        });
    }

    // method to check if user is involved in a driver accepted request
    private void checkForPendingDriverAcceptedRequest(){
        final User user = ActiveUser.getUser();
        Promise<Collection<Ride>> ridePromise = RideStore.getDriverAcceptedRequests();
        ridePromise.addOnSuccessListener(new OnSuccessListener<Collection<Ride>>() {
            @Override
            public void onSuccess(Collection<Ride> rides) {
                Log.d("TestingViewRide", "success");
                if (!rides.isEmpty()) {
                    for (Ride ride : rides){
                        // user is rider for the pending ride request
                        if (ride.getDriverUsername().equals(user.getUsername())){
                            ActiveUser.setCurrentRide(ride);
                            new DriverAcceptedFragment().show(getSupportFragmentManager(), "Pending_Driver_Accept");
                            //Toast.makeText(RiderMainPage.this, "Driver accepted", Toast.LENGTH_LONG).show();

                        }
                    }
                } else {
                    Log.d("TestingViewRide", "rides is empty");
                }
            }
        });
        ridePromise.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TestingViewRide", "failure");
            }
        });
    }

    /**
     * Driver accepted ride, send new ride to database
     * @param newRide
     */
    @Override
    public void onAcceptPressed(Ride newRide) {
        // update ride in database
        newRide.setDriverUsername(ActiveUser.getUser().getUsername());
        newRide.driverAccept();
        RideStore.saveRide(newRide);
        ActiveUser.setCurrentRide(newRide);

        new DriverAcceptedFragment().show(getSupportFragmentManager(), "Pending_Rider_Accept");
    }

    private void launchCurrentRequestActivity(){
        Intent intent = new Intent(this, CurrentRideActivity.class);
        startActivity(intent);
    }
}

