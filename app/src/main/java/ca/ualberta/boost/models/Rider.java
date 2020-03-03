package ca.ualberta.boost.models;

import android.location.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Rider extends User {

    // both riders and drivers have active rides

    public Rider(String firstName, String username, String password, String email, String phoneNumber) {
        super(firstName, username, password, email, phoneNumber);
    }

    @Override
    public Map<String, String> data() {
        Map<String, String> map = new HashMap<>();
        map.put("username", this.getUsername());
        map.put("firstName", this.getFirstName());
        map.put("password", this.getPassword());
        map.put("email", this.getEmail());
        map.put("phoneNumber", this.getPhoneNumber());
        return map;
    }
}
