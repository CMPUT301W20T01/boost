package ca.ualberta.boost.models;

import android.location.Location;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public abstract class User {

    private String firstName;
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private Location currentLocation;
    private double qrBalance;
    private @Nullable Ride activeRide; //TODO: create Ride class

    // constructor
    public User(String firstName, String lastName, String username, String password, String email, String phoneNumber, Location currentLocation) {
        this.username = username;
        this.firstName = firstName;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.currentLocation = currentLocation;
    }

    public abstract Map<String, String> data();

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
}
