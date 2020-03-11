package ca.ualberta.boost.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static ca.ualberta.boost.models.UserType.DRIVER;

public class Driver extends User {

    private ArrayList<Ride> allRides = new ArrayList<>();
    private int rating = 0;
    private int numRates;

    public Driver(String firstName, String username, String password, String email, String phoneNumber, String userType) {
        super(firstName, username, password, email, phoneNumber, userType);
    }

    public Collection<Ride> getAllRides() {
        return allRides;
    }

    public int getRating() {
        return rating;
    }

    public int getNumRates() {
        return numRates;
    }

    public void addRide(Ride ride) {
        allRides.add(ride);
    }

    public void updateRating(int newRating) {
        // rating = old + (1 / (num + 1)) * (new - old)
        numRates++;
        rating += ((1.0 / (double) numRates) * (newRating - rating));
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
        map.put("rating", Integer.toString(this.rating));
        map.put("rates", Integer.toString(this.numRates));

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
