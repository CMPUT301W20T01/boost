package ca.ualberta.boost;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import javax.annotation.Nonnull;

import ca.ualberta.boost.controllers.RideEventListener;
import ca.ualberta.boost.controllers.RideTracker;
import ca.ualberta.boost.models.ActiveUser;
import ca.ualberta.boost.models.Ride;
import ca.ualberta.boost.models.RideStatus;
import ca.ualberta.boost.models.UserType;
import ca.ualberta.boost.stores.RideStore;

public class CurrentRideActivity extends MapActivity implements RideEventListener, View.OnClickListener {
    // attributes
    private Button finishRideButton;
    public Marker pickupMarker;
    public Marker destinationMarker;
    private RideTracker tracker;
    private Ride ride;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("CurrentRideActivity", "inside OnCreate");
        tracker = new RideTracker(ActiveUser.getCurrentRide());
        tracker.addListener(this);
        ride = ActiveUser.getCurrentRide();
        finishRideButton = findViewById(R.id.finish_ride_button);

        if (ActiveUser.getUser().getType() == UserType.DRIVER) {
            finishRideButton.setVisibility(View.GONE);
            Log.d("CurrentRideActivity", "User is a driver");

        } else {
//            Log.d("CurrentRideActivity", "User is a rider");
            finishRideButton.setOnClickListener(this);
        }
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_driver_ride;
    }

    @Override
    protected MapFragment getMapFragment() {
        return (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
    }

    @Override
    protected void init() {
        GoogleMap mMap = getMap();

        Toast.makeText(CurrentRideActivity.this, "Successfully transferred to CurrentRideActivity", Toast.LENGTH_SHORT).show();
        Log.i("rideListener","CurrentRideActivity started");

        // add the location markers for the ride
        pickupMarker = mMap.addMarker(new MarkerOptions()
                .title("Pickup")
                .position(ride.getStartLocation())
        );

        destinationMarker = mMap.addMarker(new  MarkerOptions()
                .title("Destination")
                .position(ride.getEndLocation())
        );
    }

    @Override
    public void onClick(View v) {
        Log.d("CurrentRideActivity", "FinishedPressed");
        if (ActiveUser.getUser().getType() == UserType.RIDER && v.getId() == R.id.finish_ride_button) {
            Log.d("CurrentRideActivity", "User is a Rider");
            Ride ride = ActiveUser.getCurrentRide();
            ride.finish();
            RideStore.saveRide(ride);
//            Intent intent = new Intent(this, OnCompleteActivity.class);
//            startActivity(intent);
        }
    }

    @Override
    public void onStatusChange(@Nonnull Ride ride) {
        Log.d("CurrentRideActivity", "Status changed to: " + ride.getRideStatus().toString());
        if (ride.getRideStatus() == RideStatus.FINISHED && ActiveUser.getUser().getType() == UserType.DRIVER) {
            Log.d("CurrentRideActivity", "RideStatus == FINISHED");
            Intent intent = new Intent(this, ScannerActivity.class);
            startActivity(intent);
        }
        if (ride.getRideStatus() == RideStatus.FINISHED && ActiveUser.getUser().getType() == UserType.RIDER) {
            Log.d("CurrentRideActivity", "Rider OnStatusChanged");
            Intent intent = new Intent(this, OnCompleteActivity.class);
            startActivity(intent);
        }

    }

    @Override
    public void onLocationChanged() { }
}
