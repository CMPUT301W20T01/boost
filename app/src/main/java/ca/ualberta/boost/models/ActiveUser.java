package ca.ualberta.boost.models;

import com.google.android.gms.tasks.OnSuccessListener;

import ca.ualberta.boost.stores.UserStore;

public class ActiveUser {
    private static User user = null;

    private ActiveUser() {} // can't build Active User

    /**
     * Get the user using the app
     * @return current user
     */
    public static User getUser() {
        return user;
    }

    /**
     * Set the user using the app
     * @param username current user's username
     * @param password current user's password
     */
    public static void login(String username, final String password) {
        UserStore.getUser(username)
            .addOnSuccessListener(new OnSuccessListener<User>() {
                @Override
                public void onSuccess(User user) {
                    ActiveUser.user = user;
                }
            });
    }
}
