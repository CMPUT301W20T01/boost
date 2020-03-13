package ca.ualberta.boost.models;

/**
 * This enum represents Ride Statuses for a Ride, which can be
 * Pending (at the ride request stage), Accepted (once accepted by a Driver),
 * Finished (once confirmed as completed by the Rider) or Cancelled (cancelled by the Rider)
 */
public enum RideStatus {
    PENDING,
    ACCEPTED,
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
            case ACCEPTED:
                return "accepted";
            case FINISHED:
                return "finished";
            case CANCELLED:
                return "cancelled";
            default:
                throw new IllegalArgumentException("Bad status");
        }
    }
}
