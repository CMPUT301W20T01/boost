package ca.ualberta.boost.models;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class RideStore {
    private static final String TAG = "RideStore";
    private CollectionReference rideCollection;

    public RideStore(Ride ride) {
        rideCollection = FirebaseFirestore.getInstance().collection("rides");
        Map<String, String> data = ride.data();

    }
}
