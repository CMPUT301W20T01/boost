package ca.ualberta.boost;

/**
 * RideRequest is responsible displaying relevant information about a ride request
 */

public class RideRequest {
    String riderUserName;
    String startLocation;
    String endLocation;
    String fare;

    //constructor that takes a rider username, start location, end location, and fare
    public RideRequest(String riderUserName, String startLocation, String endLocation, String fare) {
        this.riderUserName = riderUserName;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.fare = fare;
    }

    //getters
    public String getRiderUserName() {
        return riderUserName;
    }

    public void setRiderUserName(String riderUserName) {
        this.riderUserName = riderUserName;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    public String getFare() {
        return fare;
    }

    public void setFare(String fare) {
        this.fare = fare;
    }
}

