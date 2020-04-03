package ca.ualberta.boost.models;

import android.location.Location;

import java.util.Map;

import javax.annotation.Nullable;

/**
 * This class represents an abstraction of a User. Stores the common fields between
 * the Rider and Driver
 * @see Rider
 * @see Driver
 */

public abstract class User {

    private UserType type;
    private String firstName;
    private String username;
    private String password;
    private String email;
    private String phoneNumber;

    // constructor
    protected User(UserType type, String firstName, String username, String password, String email, String phoneNumber) {
        this.type = type;
        this.username = username;
        this.firstName = firstName;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public abstract Map<String, Object> data();

    // getters
    public UserType getType() {
        return type;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }


    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    // setters
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
