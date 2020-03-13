package ca.ualberta.boost.stores;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

import ca.ualberta.boost.models.Ride;

/**
 * This class stores a Ride in the FireStore database.
 */

public class RideStore {
    private static final String TAG = "RideStore";
    private CollectionReference rideCollection;

    /**
     * store the data of a Ride in the FireStore database
     * @param ride
     *      A ride to store
     * @see Ride
     */
    public RideStore(Ride ride) {
        rideCollection = FirebaseFirestore.getInstance().collection("rides");
        Map<String, Object> data = ride.data();
    }
}
