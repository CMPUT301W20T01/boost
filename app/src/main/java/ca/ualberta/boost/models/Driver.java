package ca.ualberta.boost.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static ca.ualberta.boost.models.UserType.DRIVER;

/**
 * This class represents a Driver. It handles building a Map object that represents
 * the Driver, which can be received by the UserStore class to send the Driver to firebase.
 */
public class Driver extends User {

    private ArrayList<Ride> allRides = new ArrayList<>();
    private int rating = 0;
    private int numRates;

    /**
     * Driver constructor
     * @param firstName
     * @param username
     * @param password
     * @param email
     * @param phoneNumber
     */
    public Driver(String firstName, String username, String password, String email, String phoneNumber) {
        super(firstName, username, password, email, phoneNumber);
    }

    /**
     * @return all rides that the driver has completed
     * @see Ride
     */
    public Collection<Ride> getAllRides() {
        return allRides;
    }

    public int getRating() {
        return rating;
    }

    /**
     * @return number of times the driver has been rated
     */
    public int getNumRates() {
        return numRates;
    }

    /**
     * adds a ride to the list of rides the driver has completed
     * @param ride ride to add
     * @see Ride
     */
    public void addRide(Ride ride) {
        allRides.add(ride);
    }

    /**
     * updates the driver's rating based on an additional rating and
     * increments the number of ratings the driver has
     * @param newRating
     */
    public void updateRating(int newRating) {
        // rating = old + (1 / (num + 1)) * (new - old)
        numRates++;
        rating += ((1.0 / (double) numRates) * (newRating - rating));
    }

    /**
     * creates map of the driver's data
     * @return map of all driver data
     */
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

    /**
     * build a driver from a map of data
     * @param data map of all driver data
     * @return new Driver
     */
    public static Driver build(Map<String, Object> data) {
        return new Driver(
                (String) data.get("firstName"),
                (String) data.get("username"),
                (String) data.get("password"),
                (String) data.get("email"),
                (String) data.get("phoneNumber"));
    }

}
