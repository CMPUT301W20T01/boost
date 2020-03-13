package ca.ualberta.boost.models;

import android.location.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static ca.ualberta.boost.models.UserType.RIDER;

/**
 * This class represents a Rider. It handles building a Map object that represents
 * the Rider, which can be received by the UserStore class to send the Rider to firebase.
 */
public class Rider extends User {

    /**
     * Rider constructor
     * @param firstName
     * @param username
     * @param password
     * @param email
     * @param phoneNumber
     */
    public Rider(String firstName, String username, String password, String email, String phoneNumber) {
        super(firstName, username, password, email, phoneNumber);
    }

    /**
     * Creates a Map of strings that represents the Rider
     * @return
     *      Returns a Map object of strings that represents the Rider
     */
    @Override
    public Map<String, Object> data() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", RIDER);
        map.put("username", this.getUsername());
        map.put("firstName", this.getFirstName());
        map.put("password", this.getPassword());
        map.put("email", this.getEmail());
        map.put("phoneNumber", this.getPhoneNumber());
        map.put("rating", null);
        map.put("rates", null);
        return map;
    }

    /**
     * Creates a Rider object from a Map of strings
     * @param data
     *      The Map of Strings that represents the Rider
     * @return
     *      A Rider object
     */
    public static Rider build(Map<String, Object> data) {
        return new Rider(
                (String) data.get("firstName"),
                (String) data.get("username"),
                (String) data.get("password"),
                (String) data.get("email"),
                (String) data.get("phoneNumber"));
    }
}
