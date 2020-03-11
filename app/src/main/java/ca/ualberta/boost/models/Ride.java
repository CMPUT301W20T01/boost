package ca.ualberta.boost.models;

import android.annotation.SuppressLint;
//import android.location.Location;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.model.LatLng;
import ca.ualberta.boost.models.LatLng;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class Ride {
    private LatLng startLocation;
    private LatLng endLocation;
    private double fare;
    private @Nullable Driver driver = null;
    private Rider rider;
    private RideStatus status;
    private Date requestTime;

    public Ride(LatLng startLocation, LatLng endLocation, double fare, Rider rider) {
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.fare = fare;
        this.rider = rider;
        this.status = RideStatus.PENDING;
        this.requestTime = new Date(); // assigned when ride is requested
    }

    public Map<String, Object> data() {
        Map<String, Object> map = new HashMap<>();
        map.put("start_location", this.startLocation);
        map.put("end_location", this.endLocation);
        map.put("fare", Double.toString(this.fare));
        map.put("driver", this.driver.getUsername());
        map.put("rider", this.rider.getUsername());
        map.put("status", this.status.toString());
        return map;
    }

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

    public void updateFare(double fare) {
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
}
