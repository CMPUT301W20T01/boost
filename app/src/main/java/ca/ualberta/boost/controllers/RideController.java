package ca.ualberta.boost.controllers;

import com.google.firebase.firestore.GeoPoint;

import ca.ualberta.boost.models.Driver;
import ca.ualberta.boost.models.Ride;
import ca.ualberta.boost.models.RideStatus;
import ca.ualberta.boost.models.Rider;

public class RideController implements RController {
    Ride currentRide = null;

    public RideController(Ride currentRide){
        this.currentRide = currentRide;
    }

    @Override
    public Ride CreateRide() {
        return null;
    }

    @Override
    public void setDriver() {

    }

    @Override
    public void CancelRide() {

    }

    @Override
    public RideStatus checkStatus() {
        return null;
    }

    @Override
    public void setStatus() {

    }
}
