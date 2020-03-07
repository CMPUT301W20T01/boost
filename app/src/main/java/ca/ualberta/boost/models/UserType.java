package ca.ualberta.boost.models;

public enum UserType {
    RIDER,
    DRIVER;

    static String toString(UserType type) {
        switch (type) {
            case RIDER:
                return "rider";
            case DRIVER:
                return "driver";
            default:
                throw new IllegalArgumentException("Bad type");
        }
    }
}
