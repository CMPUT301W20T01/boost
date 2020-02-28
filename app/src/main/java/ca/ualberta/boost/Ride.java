package ca.ualberta.boost;

import android.location.Location;

public class Ride {
    private Location startLocation;
    private Location endLocation;
    private float fare;
    private float distance;
    private Driver driver;
    private Rider rider;
    RideStatus rideStatus;

    // constructor without distance
    public Ride(Location startLocation, Location endLocation, float fare, Driver driver, Rider rider, RideStatus rideStatus) {
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.fare = fare;
        this.driver = driver;
        this.rider = rider;
        this.rideStatus = rideStatus;
        this.distance = calculateDistance(startLocation, endLocation);
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
    }

    public Location getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(Location endLocation) {
        this.endLocation = endLocation;
    }

    public float getFare() {
        return fare;
    }

    public void setFare(float fare) {
        this.fare = fare;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Rider getRider() {
        return rider;
    }

    public void setRider(Rider rider) {
        this.rider = rider;
    }

    public RideStatus getRideStatus() {
        return rideStatus;
    }

    public void setRideStatus(RideStatus rideStatus) {
        this.rideStatus = rideStatus;
    }

    public float calculateDistance(Location startLocation, Location endLocation){
        return startLocation.distanceTo(endLocation);
    }
}
