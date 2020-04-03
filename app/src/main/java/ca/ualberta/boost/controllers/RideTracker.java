package ca.ualberta.boost.controllers;

import android.util.Log;
import androidx.annotation.Nullable;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import ca.ualberta.boost.models.Ride;

/**
 * Tracks a specified ride in the database
 */
public class RideTracker {
    private RideEventListener rideEventListener;
    private Ride ride;
    private DocumentReference docRef;

    /**
     * RideTracker constructor
     * @param ride
     *      Ride to get data of
     */
    public RideTracker(Ride ride) {
        this.ride = ride;

        docRef = FirebaseFirestore.getInstance().collection("rides").document(ride.id());
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                RideTracker.this.onEvent(documentSnapshot, e);
            }
        });
    }

    /**
     * Gets a document in the rides collection of the database and creates a new ride
     * @param documentSnapshot
     *      data of the document to create a new ride
     * @param e
     *      FireBaseException
     */
    private void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
        if (e!=null){
            Log.d("RideTracker","Listen failed: " + e);
            return;
        }

        if (documentSnapshot != null && documentSnapshot.exists()) {
            Ride newRide = Ride.build(documentSnapshot.getData());
            rideEventListener.onStatusChange(newRide);

            ride.setStatus(newRide.getRideStatus());
            ride.setDriverUsername(newRide.getDriverUsername());

        } else {
            Log.i("RideTracker","null");
        }
    }

    /**
     * Adds a listener to the ride
     * @param listener
     */
    public void addListener(RideEventListener listener) {
        rideEventListener = listener;
    }
}