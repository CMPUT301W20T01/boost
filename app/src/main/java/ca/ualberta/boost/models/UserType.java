package ca.ualberta.boost.models;

/**
 * This enum represents User Types, which can be
 * Rider or Driver
 */
public enum UserType {
    RIDER,
    DRIVER;

    static String toString(UserType type) {
        switch (type) {
            case RIDER:
                return "RIDER";
            case DRIVER:
                return "DRIVER";
            default:
                throw new IllegalArgumentException("Bad type");
        }
    }
}
