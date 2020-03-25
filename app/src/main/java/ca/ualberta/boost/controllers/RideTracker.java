package ca.ualberta.boost.controllers;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;

import javax.annotation.Nullable;

import ca.ualberta.boost.models.Ride;
import ca.ualberta.boost.models.RideStatus;

public class RideTracker {
    RideEventListener rideEventListener;
    Ride ride;

    //constructor
    public RideTracker(RideEventListener rideEventListener, @Nullable Ride ride){
        this.rideEventListener = rideEventListener;
        this.ride = ride;
    }

    //functions
    public void NotifyDriverAccept(){
        rideEventListener.onDriverAccepted(ride);
    }

    public void NotifyRiderAccept(){
        rideEventListener.onRiderAccepted(ride);
    }

    public void NotifyCancelled(){
        rideEventListener.onCancelled(ride);
    }

    public void NotifyFinished(){
        rideEventListener.onFinished(ride);
    }

    public void updateLocation(){
        rideEventListener.onLocationChanged();
    } //NOT SURE
    //listen to driver location
}
