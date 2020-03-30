package ca.ualberta.boost.models;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This class represents a Ride. It handles building a Map object that represents the Ride,
 * which can be put in a database, and building a Ride from a map object.
 */

public class Ride {
    private LatLng startLocation;
    private LatLng endLocation;
    private double fare;
    private @Nullable String driver_username = null;
    private String rider_username;
    private RideStatus status;
    private Date requestTime;

    private Ride(LatLng startLocation, LatLng endLocation, double fare, @Nonnull String driver, String rider,
                 RideStatus status, Date requestTime) {
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.fare = fare;
        this.driver_username = driver;
        this.rider_username = rider;
        this.status = status;
        this.requestTime = requestTime;
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Log.d("Ride", dateFormat.format(requestTime));
        Log.d("Ride", "creating new ride with id: " + this.id());

    }

    public Ride(LatLng startLocation, LatLng endLocation, double fare, String rider) {
        this(startLocation, endLocation, fare, null, rider, RideStatus.PENDING, new Date());
    }

    /**
     * Ride constructor with unknown start and end locations
     * @param fare
     * @param rider
     */
    public Ride(double fare, String rider) {
        this(null, null, fare, rider);
    }

    /**
     * Creates map of ride data for database
     * @return
     *      A map of all ride data
     */
    public Map<String, Object> data() {
        Map<String, Object> map = new HashMap<>();
        // Firestore take GeoPoints for location
        map.put("start_location", toGeoPoint(this.startLocation));
        map.put("end_location", toGeoPoint(this.endLocation));
        map.put("fare", this.fare);
        map.put("driver", this.driver_username);
        map.put("rider", this.rider_username);
        map.put("status", this.status.getValue());
        map.put("request_time", this.requestTime);
        return map;
    }

    /**
     * Generate the ride id based on the rider's username and timestamp of
     * when the ride request was created
     * @return
     *      A string id of ride
     */
    @SuppressLint("DefaultLocale")
    public String id() {
        return String.format("%s_%d", rider_username, requestTime.getTime());
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

    public String getDriverUsername() {
        return driver_username;
    }

    public String getRiderUsername() {
        return rider_username;
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

    public void setDriverUsername(String driver) {
        this.driver_username = driver;
    }

    public void setRiderUsername(String rider) {
        this.rider_username = rider;
    }

    public void setPending() {
        this.status = RideStatus.PENDING;
    }

    public void driverAccept() {
        this.status = RideStatus.DRIVERACCEPTED;
    }

    public void riderAccept() {
        this.status = RideStatus.RIDERACCEPTED;
    }

    public void finish() {
        this.status = RideStatus.FINISHED;
    }

    public void cancel() {
        this.status = RideStatus.CANCELLED;
    }

    public void payDriver() {
        this.status = RideStatus.PAID;
    }

    /**
     * Calculates the base fare based on the Manhattan distance of
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

    /**
     * Converts LatLng to GeoPoint
     * @param location
     * @return GeoPoint of location
     */
    private static GeoPoint toGeoPoint(LatLng location) {
        return new GeoPoint(location.latitude, location.longitude);
    }

    /**
     * Converts GeoPoint to LatLng
     * @param point
     * @return LatLng of point
     */
    private static LatLng toLatLng(GeoPoint point) {
        return new LatLng(point.getLatitude(), point.getLongitude());
    }

    public void setStatus(RideStatus status) {
        this.status = status;
    }

    /**
     * Creates a Ride object from a Map of string, object pairs
     * @param data
     *      The Map of data that represents the Ride
     * @return
     *      A new Ride object
     */
    public static Ride build(Map<String, Object> data) {
        Timestamp timestamp = (Timestamp) data.get("request_time");
        Long status = (Long) data.get("status");
        Log.d("Ride", data.get("status").toString());
        return new Ride(
                // convert GeoPoints to LatLng
                toLatLng((GeoPoint) data.get("start_location")),
                toLatLng((GeoPoint) data.get("end_location")),
                (double) data.get("fare"),
                (String) data.get("driver"),
                (String) data.get("rider"),
                RideStatus.valueOf(status.intValue()),
                timestamp.toDate());
    }
}

