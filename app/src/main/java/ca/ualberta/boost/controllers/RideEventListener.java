package ca.ualberta.boost.controllers;

import androidx.annotation.NonNull;
import ca.ualberta.boost.models.Ride;

public interface RideEventListener {
    /**
     * Interface for listeners when the status of a ride changes in the database
     * @param ride
     *      The changed ride
     */
    void onStatusChange(@NonNull Ride ride);
}
