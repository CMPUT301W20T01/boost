package ca.ualberta.boost.controllers;

import android.view.View;

import com.google.android.gms.maps.MapView;
import com.google.firebase.firestore.GeoPoint;

import javax.annotation.Nullable;

import ca.ualberta.boost.models.Driver;
import ca.ualberta.boost.models.Ride;
import ca.ualberta.boost.models.RideStatus;
import ca.ualberta.boost.models.Rider;

public interface RideEventListener {
    // you can define any parameter as per your requirement
    public void onStatusChange(@Nullable Ride ride);
    public void onLocationChanged(); //NOT SURE
}
