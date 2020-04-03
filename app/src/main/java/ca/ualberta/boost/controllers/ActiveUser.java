package ca.ualberta.boost.controllers;

import android.util.Log;

import ca.ualberta.boost.models.Ride;
import ca.ualberta.boost.models.User;

/**
 * This class represents the user currently using the app. It is used to set and get the
 * current user.
 */

public class ActiveUser {
    private static User user = null;
    private static Ride currentRide = null;

    private ActiveUser() {} // can't build Active User

    /**
     * Get the user using the app
     * @return
     *      Returns the current user
     */
    public static User getUser() {
        return user;
    }

    /**
     * Get the current user's current Ride
     * @return
     *      Returns a Ride if the current user is on a ride, null otherwise
     */
    public static Ride getCurrentRide() {
        if (user == null) {
            return null;
        }
        return currentRide;
    }

    public static void setCurrentRide(Ride ride) {
        currentRide = ride;
        Log.d("ActiveUser", "setting ride: " + ride.id());
    }

    public static void cancelRide() {
        currentRide = null;
    }

    /**
     * Assigns a current user
     * @param user
     *      the current user
     */
    public static void login(User user) {
        ActiveUser.user = user;
    }

    /**
     * Sets the current user to null
     */
    public static void logout() {
        ActiveUser.user = null;
    }
}
