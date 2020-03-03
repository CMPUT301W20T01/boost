package ca.ualberta.boost.models;

public enum RideStatus {
    PENDING,
    ACCEPTED,
    FINISHED,
    CANCELLED;

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
