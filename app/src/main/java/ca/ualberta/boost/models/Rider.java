package ca.ualberta.boost.models;

import android.location.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static ca.ualberta.boost.models.UserType.RIDER;

public class Rider extends User {

    // both riders and drivers have active rides

    public Rider(String firstName, String username, String password, String email, String phoneNumber) {
        super(firstName, username, password, email, phoneNumber);
    }

    @Override
    public Map<String, String> data() {
        Map<String, String> map = new HashMap<>();
        map.put("type", RIDER.toString());
        map.put("username", this.getUsername());
        map.put("firstName", this.getFirstName());
        map.put("password", this.getPassword());
        map.put("email", this.getEmail());
        map.put("phoneNumber", this.getPhoneNumber());
        map.put("rating", "");
        map.put("rates", "");
        return map;
    }
}
