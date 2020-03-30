package ca.ualberta.boost.models;

import java.util.HashMap;
import java.util.Map;

/**
 * This enum represents the Ride Status for a Ride, which can be
 * PENDING (at the ride request stage), ACCEPTED (once accepted by a Driver),
 * FINISHED (once confirmed as completed by the Rider) or CANCELLED (cancelled by the Rider).
 */

public enum RideStatus {
    PENDING(0),
    DRIVERACCEPTED(1),
    RIDERACCEPTED(2),
    FINISHED(3),
    CANCELLED(4);

    private final int value;
    private static Map<Integer, RideStatus> map = new HashMap<>();

    RideStatus(int value) {
        this.value = value;
    }

    static {
        for (RideStatus rideStatus : RideStatus.values()) {
            map.put(rideStatus.value, rideStatus);
        }
    }

    public int getValue() {
        return value;
    }

    public static RideStatus valueOf(int rideStatus) {
        return map.get(rideStatus);
    }

    /**
     * Returns a string of the status given an enum
     * @param status
     *      The enum of the status
     * @return
     *      Returns a string of the status
     */
    static String toString(RideStatus status) {
        switch(status) {
            case PENDING:
                return "pending";
            case DRIVERACCEPTED:
                return "driver accepted";
            case RIDERACCEPTED:
                return "rider accepted";
            case FINISHED:
                return "finished";
            case CANCELLED:
                return "cancelled";
            default:
                throw new IllegalArgumentException("Bad status");
        }
    }
}
