package ca.ualberta.boost.models;

public class ActiveUser {
    private static User user = null;

    private ActiveUser() {} // can't build Active User

    public static User getUser() {
        return user;
    }

    public static boolean login(String username, String password) {
        return true;
    }
}
