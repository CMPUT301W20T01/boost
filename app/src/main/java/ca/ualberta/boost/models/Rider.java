package ca.ualberta.boost.models;

import android.location.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static ca.ualberta.boost.models.UserType.RIDER;

public class Rider extends User {

    // both riders and drivers have active rides
    public Rider(String firstName, String username, String password, String email, String phoneNumber, String userType ) {
        super(firstName, username, password, email, phoneNumber, userType);

    }

    @Override
    public Map<String, Object> data() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", RIDER);
        map.put("username", this.getUsername());
        map.put("firstName", this.getFirstName());
        map.put("password", this.getPassword());
        map.put("email", this.getEmail());
        map.put("phoneNumber", this.getPhoneNumber());
        map.put("userType", this.getUserType());
        map.put("rating", "");
        map.put("rates", "");

        return map;
    }

    public static Rider build(Map<String, Object> data) {
        return new Rider(
                (String) data.get("firstName"),
                (String) data.get("username"),
                (String) data.get("password"),
                (String) data.get("email"),
                (String) data.get("phoneNumber"));
    }
}
