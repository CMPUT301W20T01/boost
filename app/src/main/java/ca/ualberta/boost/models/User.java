package ca.ualberta.boost.models;

import android.location.Location;

import java.util.Map;

import javax.annotation.Nullable;

/**
 * This class represents an abstraction of a User. Stores the common fields between
 * the Rider and Driver
 * @see Rider
 * @see Driver
 */

public abstract class User {

    private String firstName;
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private Location currentLocation;
    private double qrBalance;
    private @Nullable Ride activeRide;

    // constructor
    protected User(String firstName, String username, String password, String email, String phoneNumber) {
        this.username = username;
        this.firstName = firstName;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public abstract Map<String, Object> data();

    // getters
    public String getFirstName() {
        return firstName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }


    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public double getQrBalance() {
        return qrBalance;
    }

    @Nullable
    public Ride getActiveRide() {
        return activeRide;
    }

    // setters
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public void setQrBalance(double qrBalance) {
        this.qrBalance = qrBalance;
    }

    public void setActiveRide(@Nullable Ride activeRide) {
        this.activeRide = activeRide;
    }
}
