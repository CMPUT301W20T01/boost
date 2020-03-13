package ca.ualberta.boost.models;

import android.annotation.SuppressLint;
import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

/**
 * This class represents a Ride. It handles building a Map object that represents the Ride,
 * which can be put in a database, and building a Ride from a map object.
 */

public class Ride {
    private LatLng startLocation;
    private LatLng endLocation;
    private double fare;
    private @Nullable Driver driver = null;
    private Rider rider;
    private RideStatus status;
    private Date requestTime;

    /**
     * Ride constructor with known start and end locations
     * @param startLocation
     * @param endLocation
     * @param fare
     * @param rider
     */
    public Ride(LatLng startLocation, LatLng endLocation, double fare, Rider rider) {
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.fare = fare;
        this.rider = rider;
        this.status = RideStatus.PENDING;
        this.requestTime = new Date(); // assigned when ride is requested
    }

    /**
     * Ride constructor with unknown start and end locations
     * @param fare
     * @param rider
     */
    public Ride(double fare, Rider rider) {
        this.fare = fare;
        this.rider = rider;
    }

    /**
     * creates map of ride data for database
     * @return
     *      A map of all ride data
     */
    public Map<String, Object> data() {
        Map<String, Object> map = new HashMap<>();
        map.put("start_location", this.startLocation);
        map.put("end_location", this.endLocation);
        map.put("fare", this.fare);
        map.put("driver", this.driver.getUsername());
        map.put("rider", this.rider.getUsername());
        map.put("status", this.status.toString());
        return map;
    }

    /**
     * generate the ride id based on the rider's username and timestamp of
     * when the ride request was created
     * @return
     *      A string id of ride
     */
    @SuppressLint("DefaultLocale")
    public String id() {
        return String.format("%s_%d", rider.getUsername(), requestTime.getTime());
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

    /**
     * calculate the base fare based on the Manhattan distance of
     * the start and end locations of the ride
     * @return
     *      The base fare amount
     */
    public double baseFare() {
        double latDiff = Math.abs(startLocation.latitude - endLocation.latitude);
        double longDiff = Math.abs(startLocation.longitude - endLocation.longitude);
        double fare = 5 + ((latDiff + longDiff) * 300); // base price of $5
        // round fare to 2 decimal places
        BigDecimal bdFare = new BigDecimal(fare).setScale(2, RoundingMode.HALF_UP);
        return bdFare.doubleValue();
    }
}
