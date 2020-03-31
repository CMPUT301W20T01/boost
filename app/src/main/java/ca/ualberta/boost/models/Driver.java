package ca.ualberta.boost.models;

import java.math.BigDecimal;
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

    private int thumbsUp = 0;
    private int thumbsDown = 0;
    private double balance = 0.00;

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

    private Driver(String firstName, String username, String password, String email, String phoneNumber, int thumbsUp, int thumbsDown, double balance) {
        super(UserType.DRIVER, firstName, username, password, email, phoneNumber);
        this.thumbsUp = thumbsUp;
        this.thumbsDown = thumbsDown;
        this.balance = balance;
    }

    /**
     * @return
     *      Returns the number of thumbs up ratings the driver has
     */
    public int getPositiveRating() {
        return thumbsUp;
    }

    /**
     * @return
     *      Returns the number of thumbs down ratings the driver has
     */
    public int getNegativeRating() {
        return thumbsDown;
    }

    public void giveThumbsUp() {
        thumbsUp++;
    }

    public void giveThumbsDown() {
        thumbsDown++;
    }

    public void addToBalance(double payment) {
        balance += payment;
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
        map.put("thumbsUp", this.thumbsUp);
        map.put("thumbsDown", this.thumbsDown);
        map.put("balance", this.balance);
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
        Long positive = (Long) data.get("thumbsUp");
        Long negative = (Long) data.get("thumbsDown");
        return new Driver(
                (String) data.get("firstName"),
                (String) data.get("username"),
                (String) data.get("password"),
                (String) data.get("email"),
                (String) data.get("phoneNumber"),
                positive.intValue(),
                negative.intValue(),
                (double) data.get("balance"));
    }

}
