package ca.ualberta.boost;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import javax.annotation.Nonnull;

import ca.ualberta.boost.controllers.RideEventListener;
import ca.ualberta.boost.controllers.RideTracker;
import ca.ualberta.boost.controllers.ActiveUser;
import ca.ualberta.boost.models.Ride;
import ca.ualberta.boost.models.RideStatus;
import ca.ualberta.boost.models.UserType;
import ca.ualberta.boost.stores.RideStore;

public class CurrentRideActivity extends MapActivity implements RideEventListener, View.OnClickListener {
    // attributes
    private Button finishRideButton;
    private Button cancelRideButton;
    private Button confirmPickupButton;
    private Button viewProfileButton;
    public Marker pickupMarker;
    public Marker destinationMarker;
    private RideTracker tracker;
    private Ride ride;
    private Boolean isDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isDriver = ActiveUser.getUser().getType() == UserType.DRIVER;
        super.onCreate(savedInstanceState);
        Log.d("CurrentRideActivity", "inside OnCreate");


    }

    @Override
    protected int getLayoutResourceId() {
        // user is the driver
        if (isDriver) {
            return R.layout.activity_driver_ride;
        }
        // user is the rider
        return R.layout.activity_rider_ride;

    }

    @Override
    protected MapFragment getMapFragment() {
        return (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
    }

    @Override
    protected void init() {
        GoogleMap mMap = getMap();

        tracker = new RideTracker(ActiveUser.getCurrentRide());
        tracker.addListener(this);
        ride = ActiveUser.getCurrentRide();

        // add the location markers for the ride
        pickupMarker = mMap.addMarker(new MarkerOptions()
                .title("Pickup")
                .position(ride.getStartLocation())
        );

        destinationMarker = mMap.addMarker(new  MarkerOptions()
                .title("Destination")
                .position(ride.getEndLocation())
        );

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                zoomToMarkers(pickupMarker, destinationMarker);
            }
        });

        viewProfileButton = findViewById(R.id.viewProfileButton);

        if (isDriver){ // user is driver
            confirmPickupButton = findViewById(R.id.confirm_pickup_button);

            confirmPickupButton.setOnClickListener(this);

            viewProfileButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                  profile: https://stackoverflow.com/users/4755700/atiar-talukdar, link to question: https://stackoverflow.com/questions/6707900/pass-a-string-from-one-activity-to-another-activity-in-android
                    Intent intent = new Intent(CurrentRideActivity.this, UserProfileActivity.class);
                    intent.putExtra("username",ride.getRiderUsername());
                    startActivity(intent);
                }
            });
        } else{ // user is rider
            finishRideButton = findViewById(R.id.finish_ride_button);
            cancelRideButton = findViewById(R.id.cancel_ride_button);

            cancelRideButton.setOnClickListener(this);
            finishRideButton.setOnClickListener(this);

            viewProfileButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CurrentRideActivity.this, UserProfileActivity.class);
                    intent.putExtra("username",ride.getDriverUsername());
                    startActivity(intent);
                }
            });
        }

        Toast.makeText(CurrentRideActivity.this, "Successfully transferred to CurrentRideActivity", Toast.LENGTH_SHORT).show();
        Log.i("rideListener","CurrentRideActivity started");

    }

    @Override
    public void onClick(View v) {
        Log.d("CurrentRideActivity", "FinishedPressed");

        //Rider Pressed Finished Button
        if (ActiveUser.getUser().getType() == UserType.RIDER && v.getId() == R.id.finish_ride_button) {
            Log.d("CurrentRideActivity", "User is a Rider");
            Ride ride = ActiveUser.getCurrentRide();
            ride.finish();
            RideStore.saveRide(ride);
//            Intent intent = new Intent(this, OnCompleteActivity.class);
//            startActivity(intent);
        }

        //Rider Pressed Cancel Button
        if (ActiveUser.getUser().getType() == UserType.RIDER && v.getId() == R.id.cancel_ride_button) {
            Log.d("CurrentRideActivity", "User is a Rider");
            Ride ride = ActiveUser.getCurrentRide();
            ride.cancel();
            RideStore.saveRide(ride);
            ActiveUser.cancelRide();

        }

        //Driver pressed confirm pickup button
        if (ActiveUser.getUser().getType() == UserType.DRIVER && v.getId() == R.id.confirm_pickup_button) {
            Log.d("CurrentRideActivity", "User is a Driver");
            //set status to pickup
            ride.driverPickup();
            RideStore.saveRide(ride);

            //Hide the Button
            confirmPickupButton.setVisibility(View.GONE);
        }

    }

    @Override
    public void onStatusChange(@Nonnull Ride ride) {
        Log.d("CurrentRideActivity", "Status changed to: " + ride.getRideStatus().toString());
        if (ride.getRideStatus() == RideStatus.FINISHED && ActiveUser.getUser().getType() == UserType.DRIVER) {
            Log.d("CurrentRideActivity", "RideStatus == FINISHED");
            Toast.makeText(this, "Ride completed. Transferring to payment", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(this, ScannerActivity.class);
            startActivity(intent);
        }
        if (ride.getRideStatus() == RideStatus.FINISHED && ActiveUser.getUser().getType() == UserType.RIDER) {
            Log.d("CurrentRideActivity", "Rider OnStatusChanged");
            Toast.makeText(this, "Ride completed. Transferring to payment", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(this, OnCompleteActivity.class);
            startActivity(intent);
        }

        if (ride.getRideStatus() == RideStatus.PICKEDUP && ActiveUser.getUser().getType() == UserType.RIDER) {
            Log.d("CurrentRideActivity", "RideStatus == PICKEDUP");
            Toast.makeText(this, "Driver arrived at pickup location. Cannot cancel ride from now on", Toast.LENGTH_LONG).show();
            cancelRideButton.setVisibility(View.GONE);
        }

        if (ride.getRideStatus() == RideStatus.CANCELLED && ActiveUser.getUser().getType() == UserType.RIDER) {
            Log.d("CurrentRideActivity", "RideStatus == PICKEDUP");
            Toast.makeText(this, "The ride has been cancelled", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(this, RiderMainPage.class);
            startActivity(intent);
        }

        if (ride.getRideStatus() == RideStatus.CANCELLED && ActiveUser.getUser().getType() == UserType.DRIVER) {
            Log.d("CurrentRideActivity", "RideStatus == PICKEDUP");
            Toast.makeText(this, "Sorry, the ride has been cancelled", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(this, DriverMainPage.class);
            startActivity(intent);
        }
    }
}
