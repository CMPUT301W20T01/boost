package ca.ualberta.boost.models;

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
