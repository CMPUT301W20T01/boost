package ca.ualberta.boost.controllers;

import com.google.firebase.firestore.GeoPoint;

import ca.ualberta.boost.models.Driver;
import ca.ualberta.boost.models.Ride;
import ca.ualberta.boost.models.RideStatus;
import ca.ualberta.boost.models.Rider;

public interface RController {
    public Ride CreateRide();
    public void setDriver();
    public void CancelRide();
    public RideStatus checkStatus();
    public void setStatus();
}
