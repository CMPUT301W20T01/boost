package ca.ualberta.boost.models;

/**
 * This enum represents User Types, which can be
 * Rider or Driver
 */
public enum UserType {
    RIDER,
    DRIVER;

    /**
     * Returns a string of the User Type given an enum
     * @param type
     *      The enum of the status
     * @return
     *      Returns a string of the User Type
     */
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
