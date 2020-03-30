package ca.ualberta.boost.stores;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import ca.ualberta.boost.models.Driver;
import ca.ualberta.boost.models.Promise;
import ca.ualberta.boost.models.PromiseImpl;
import ca.ualberta.boost.models.Ride;
import ca.ualberta.boost.models.RideStatus;
import ca.ualberta.boost.models.Rider;
import ca.ualberta.boost.models.User;
import ca.ualberta.boost.models.UserType;

/**
 * This class stores a Ride in the FireStore database.
 */

public class RideStore {
    private static final String TAG = "RideStore";
    private static RideStore instance = null;

    private CollectionReference rideCollection;

    /**
     * Constructor
     */
    private RideStore() {
        rideCollection = FirebaseFirestore.getInstance().collection("rides");
    }

    /**
     * Singleton, creates an instance of a RideStore once
     * @return
     *      The instance of the RideStore
     */
    public static RideStore getInstance() {
        if (instance == null) {
            instance = new RideStore();
        }
        return instance;
    }

    /**
     * Saves a ride in the ride table of the database
     * @param ride ride to save
     * @see Ride
     */
    public static void saveRide(Ride ride) {
//        Log.d("saveRide", ride.getDriverUsername());
        RideStore store = getInstance();

        Map<String, Object> data = ride.data();

        store.rideCollection
                .document(ride.id())
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG,"Ride Data Save Success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Ride Data Save Failed");
                    }
                });
    }

    /**
     * Tries to retrieve the data of a specified Ride
     * @param rideID
     *      ID of the Ride wanted from database
     * @return
     *      Promise of the data for the requested Ride
     * @see Ride
     * @see Promise
     * @see PromiseImpl
     */
    public static Promise<Ride> getRide(final String rideID) {
        RideStore store = getInstance();

        final PromiseImpl<Ride> ridePromise = new PromiseImpl<>();
        store.rideCollection
                .document(rideID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (!task.isSuccessful()) {
                            ridePromise.reject(new Exception("Ride does not exist"));
                            return;
                        }

                        DocumentSnapshot result = task.getResult();
                        if (result == null) {
                            ridePromise.reject(new Exception("Something went wrong"));
                            return;
                        }

                        Map<String, Object> data = result.getData();

                        if (data == null) {
                            ridePromise.reject(new Exception("Something went wrong"));
                            return;
                        }

                        Ride ride = Ride.build(data);

                        ridePromise.resolve(ride);
                    }
                });

        return ridePromise;
    }

    public static Promise<Collection<Ride>> getPastRides(final String driver_username) {
        RideStore store = getInstance();

        final PromiseImpl<Collection<Ride>> ridesPromise = new PromiseImpl<>();
        store.rideCollection
                .whereEqualTo("driver", driver_username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (!task.isSuccessful()) {
                            ridesPromise.reject(new Exception("No past Rides"));
                            return;
                        }

                        QuerySnapshot result = task.getResult();
                        if (result == null) {
                            ridesPromise.reject(new Exception("Something went wrong"));
                            return;
                        }

                        Collection<Ride> rides = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            rides.add(Ride.build(document.getData()));
                        }

                        ridesPromise.resolve(rides);
                    }
                });

        return ridesPromise;
    }

    public static Promise<Collection<Ride>> getRequests() {
        RideStore store = getInstance();

        final PromiseImpl<Collection<Ride>> ridesPromise = new PromiseImpl<>();
        store.rideCollection
                .whereEqualTo("status", RideStatus.PENDING.getValue())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (!task.isSuccessful()) {
                            ridesPromise.reject(new Exception("No pending rides"));
                            return;
                        }

                        QuerySnapshot result = task.getResult();
                        if (result == null) {
                            ridesPromise.reject(new Exception("Something went wrong"));
                            return;
                        }

                        Collection<Ride> requests = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            requests.add(Ride.build(document.getData()));
                        }

                        ridesPromise.resolve(requests);
                    }
                });

        return ridesPromise;
    }
}
