package ca.ualberta.boost.models;

import android.location.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Driver extends User {

    private ArrayList<Ride> allRides;
    private int rating;
    private int numRates;

    public Driver(String firstName, String username, String password, String email, String phoneNumber, String userType) {
        super(firstName, username, password, email, phoneNumber, userType);
    }

    public ArrayList<Ride> getAllRides() {
        return allRides;
    }

    public int getRating() {
        return rating;
    }

    public void setAllRides(ArrayList<Ride> allRides) {
        this.allRides = allRides;
    }

    public void updateRating(int newRating) {
        // rating = old + (1 / (num + 1)) * (new - old)
        this.rating = this.rating + ((1 / (numRates + 1)) * (newRating - this.rating));
        this.numRates++;
    }

    @Override
    public Map<String, String> data() {
        Map<String, String> map = new HashMap<>();
        map.put("username", this.getUsername());
        map.put("firstName", this.getFirstName());
        map.put("password", this.getPassword());
        map.put("email", this.getEmail());
        map.put("phoneNumber", this.getPhoneNumber());
        map.put("userType", this.getUserType());
        map.put("rating", Integer.toString(this.rating));
        map.put("rates", Integer.toString(this.numRates));
        return map;
    }
}
