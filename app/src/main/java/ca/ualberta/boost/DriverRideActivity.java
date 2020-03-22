package ca.ualberta.boost;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.maps.MapFragment;

public class DriverRideActivity extends MapActivity {

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

    }
}
