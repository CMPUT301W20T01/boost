package ca.ualberta.boost.models;

import com.google.android.gms.tasks.OnSuccessListener;

import ca.ualberta.boost.stores.UserStore;

/**
 * This class represents the user currently using the app. It is used to set and get the
 * current user.
 */

public class ActiveUser {
    private static User user = null;

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
