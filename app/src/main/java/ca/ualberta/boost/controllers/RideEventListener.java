package ca.ualberta.boost.controllers;

import android.view.View;

import com.google.android.gms.maps.MapView;
import com.google.firebase.firestore.GeoPoint;

import ca.ualberta.boost.models.Driver;
import ca.ualberta.boost.models.Ride;
import ca.ualberta.boost.models.RideStatus;
import ca.ualberta.boost.models.Rider;

public interface RideEventListener {
    // you can define any parameter as per your requirement
    public void onAccepted(Ride ride);
    public void onCancelled(Ride ride);
    public void onFinished(Ride ride);
    public void onLocationChanged(MapView view);
}
