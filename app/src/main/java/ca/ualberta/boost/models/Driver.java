package ca.ualberta.boost.models;

import android.location.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static ca.ualberta.boost.models.UserType.DRIVER;

public class Driver extends User {

    private ArrayList<Ride> allRides;
    private int rating;
    private int numRates;

    public Driver(String firstName, String username, String password, String email, String phoneNumber) {
        super(firstName, username, password, email, phoneNumber);
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
    public Map<String, Object> data() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", DRIVER);
        map.put("username", this.getUsername());
        map.put("firstName", this.getFirstName());
        map.put("password", this.getPassword());
        map.put("email", this.getEmail());
        map.put("phoneNumber", this.getPhoneNumber());
        map.put("rating", this.rating);
        map.put("rates", this.numRates);
        return map;
    }

    public static Driver build(Map<String, Object> data) {
        return new Driver(
                (String) data.get("firstName"),
                (String) data.get("username"),
                (String) data.get("password"),
                (String) data.get("email"),
                (String) data.get("phoneNumber"));
    }

}
