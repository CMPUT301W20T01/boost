package ca.ualberta.boost.models;

import java.util.HashMap;
import java.util.Map;

/**
 * This enum represents User Types, which can be RIDER or DRIVER.
 */

public enum UserType {
    RIDER(0),
    DRIVER(1);

    private final int value;
    private static Map<Integer, UserType> map = new HashMap<>();

    UserType(int value) {
        this.value = value;
    }

    static {
        for (UserType userType : UserType.values()) {
            map.put(userType.value, userType);
        }
    }

    public int getValue() {
        return value;
    }

    public static UserType valueOf(int userType) {
        return map.get(userType);
    }

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
