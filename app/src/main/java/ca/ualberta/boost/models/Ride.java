package ca.ualberta.boost.models;


import com.google.android.gms.maps.model.LatLng;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class Ride {
    private LatLng startLocation;
    private LatLng endLocation;
    private double fare;
    private Driver driver;
    private Rider rider;
    private RideStatus status;

    public Ride(LatLng startLocation, LatLng endLocation, double fare, Driver driver, Rider rider) {
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.fare = fare;
        this.driver = driver;
        this.rider = rider;
        this.status = RideStatus.PENDING;
    }

    // empty constructor
    public Ride(){

    }

    public Map<String, String> data() {
        Map<String, String> map = new HashMap<>();
//        map.put("start_location", this.startLocation);
//        map.put("end_location", this.endLocation);
        map.put("fare", Double.toString(this.fare));
        map.put("driver", this.driver.getUsername());
        map.put("rider", this.rider.getUsername());
        map.put("status", this.status.toString());
        return map;
    }

    public LatLng getStartLocation() {
        return startLocation;
    }

    public LatLng getEndLocation() {
        return endLocation;
    }

    public double getFare() {
        return fare;
    }

    public Driver getDriver() {
        return driver;
    }

    public Rider getRider() {
        return rider;
    }

    public RideStatus getRideStatus() {
        return status;
    }

    public void setStartLocation(LatLng startLocation) {
        this.startLocation = startLocation;
    }

    public void setEndLocation(LatLng endLocation) {
        this.endLocation = endLocation;
    }

    public void setFare(double fare) {
        this.fare = fare;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public void setRider(Rider rider) {
        this.rider = rider;
    }

//    public void setRideStatus(RideStatus rideStatus) {
//        this.rideStatus = rideStatus;
//    }

    public void setPending() {
        this.status = RideStatus.PENDING;
    }

    public void accept() {
        this.status = RideStatus.ACCEPTED;
    }

    public void finish() {
        this.status = RideStatus.FINISHED;
    }

    public void cancel() {
        this.status = RideStatus.CANCELLED;
    }

    /* calculates and sets the fare according to the
    ride's start and end locations
     */
    public void calculateAndSetFare(){
        double latDiff = Math.abs(startLocation.latitude - endLocation.latitude);
        double longDiff = Math.abs(startLocation.longitude - endLocation.longitude);
        double fare = (latDiff + longDiff) * 125;
        // round fare to 2 decimal places
        BigDecimal bdFare = new BigDecimal(fare).setScale(2, RoundingMode.HALF_UP);
        setFare(bdFare.doubleValue());
    }
}
