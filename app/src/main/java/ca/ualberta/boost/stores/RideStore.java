package ca.ualberta.boost.stores;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

import ca.ualberta.boost.models.Ride;

public class RideStore {
    private static final String TAG = "RideStore";
    private CollectionReference rideCollection;

    public RideStore(Ride ride) {
        rideCollection = FirebaseFirestore.getInstance().collection("rides");
       // Map<String, Object> data = ride.data();
    }
}
