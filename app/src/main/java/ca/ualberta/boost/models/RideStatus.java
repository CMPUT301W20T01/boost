package ca.ualberta.boost.models;

/**
 * This enum represents the Ride Status for a Ride, which can be
 * PENDING (at the ride request stage), ACCEPTED (once accepted by a Driver),
 * FINISHED (once confirmed as completed by the Rider) or CANCELLED (cancelled by the Rider).
 */

public enum RideStatus {
    PENDING,
    DRIVERACCEPTED,
    RIDERACCEPTED,
    FINISHED,
    CANCELLED;

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
