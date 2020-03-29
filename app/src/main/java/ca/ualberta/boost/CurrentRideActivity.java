package ca.ualberta.boost;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import ca.ualberta.boost.models.ActiveUser;
import ca.ualberta.boost.models.Ride;

// TODO: Rename class to RideActivity because this class will be called by both Drivers and Riders
public class CurrentRideActivity extends MapActivity {
    // attributes
    private Ride ride;
    public Marker pickupMarker;
    public Marker destinationMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        ride = ActiveUser.getCurrentRide();
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
        // move camera to show both markers
      //  zoomToMarkers(pickupMarker, destinationMarker);
    }
}
