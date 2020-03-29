package ca.ualberta.boost.models;

import com.google.android.gms.tasks.OnSuccessListener;

import ca.ualberta.boost.stores.RideStore;
import ca.ualberta.boost.stores.UserStore;

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

    public static Ride getCurrentRide() {
        if (user == null) {
            return null;
        }
        return currentRide;
    }

    public static boolean setCurrentRide(Ride ride) {
        if (currentRide == null) {
            currentRide = ride;
            RideStore.saveRide(ride);
            return true;
        }
        return false;
    }

    public static void cancelRide() {
        currentRide = null;
    }
    
    public static boolean isOnRide() {
        return currentRide != null;
    }

    /**
     *
     * @param user
     */
    public static void login(User user) {
        ActiveUser.user = user;
    }

    public static void logout() {
        ActiveUser.user = null;
    }
}
