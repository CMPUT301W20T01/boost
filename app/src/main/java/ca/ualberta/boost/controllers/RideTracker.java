package ca.ualberta.boost.controllers;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;

import ca.ualberta.boost.models.Ride;
import ca.ualberta.boost.models.RideStatus;

public class RideTracker {
    RideEventListener rideEventListener;
    Ride ride;
    MapView mapView; //UNSURE

    //constructor
    RideTracker(RideEventListener rideEventListener, Ride ride){
        this.rideEventListener = rideEventListener;
        this.ride = ride;
        this.mapView = mapView;
    }

    //functions
    public void NotifyAccept(){
        rideEventListener.onAccepted(ride);
    }

    public void NotifyCancelled(){
        rideEventListener.onCancelled(ride);
    }

    public void NotifyFinished(){
        rideEventListener.onFinished(ride);
    }

    public void updateLocation(){
        rideEventListener.onLocationChanged(mapView);
    }
}