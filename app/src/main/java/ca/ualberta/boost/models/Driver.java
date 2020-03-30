package ca.ualberta.boost.models;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static ca.ualberta.boost.models.UserType.DRIVER;

/**
 * This class represents a Driver. It handles building a Map object that represents the Driver,
 * which can be put in a database, and building a Driver from a Map object.
 * @see User
 */

public class Driver extends User {

    private double rating = 0.00;
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
        super(UserType.DRIVER, firstName, username, password, email, phoneNumber);
    }

    public double getRating() {
        return rating;
    }

    /**
     * @return
     *      Returns the number of times the driver has been rated
     */
    public int getNumRates() {
        return numRates;
    }

    /**
     * Updates the driver's rating based on an additional rating and
     * increments the number of ratings the driver has
     * @param newRating
     */
    public void updateRating(int newRating) {
        // rating = old + (1 / (num + 1)) * (new - old)
        numRates++;
        rating += ((1.0 / (double) numRates) * (newRating - rating));
    }

    /**
     * Creates map of the driver's data
     * @return
     *      Returns the map of all driver data
     */
    @Override
    public Map<String, Object> data() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", DRIVER.getValue());
        map.put("username", this.getUsername());
        map.put("firstName", this.getFirstName());
        map.put("password", this.getPassword());
        map.put("email", this.getEmail());
        map.put("phoneNumber", this.getPhoneNumber());
        map.put("positiveRating", 0);
        map.put("negativeRating", 0);
        return map;
    }

    /**
     * Build a driver from a map of string, object pairs
     * @param data
     *      The Map of data that represents the Driver
     * @return
     *      A new Driver object
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
