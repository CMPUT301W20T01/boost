package ca.ualberta.boost.controllers;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import ca.ualberta.boost.RiderMainPage;
import ca.ualberta.boost.models.ActiveUser;
import ca.ualberta.boost.models.Ride;
import ca.ualberta.boost.models.RideStatus;
import ca.ualberta.boost.stores.RideStore;

public class RideTracker {
    RideEventListener rideEventListener;
    Ride ride;
    DocumentReference docRef = null;
    boolean checkDriver = false;

    //constructor
    public RideTracker(Ride ride) {
        this.ride = ride;
//        Log.d("RideTracker", "creating RideTracker for ride: " + ride.id());

        docRef = FirebaseFirestore.getInstance().collection("rides").document(ride.id());
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                RideTracker.this.onEvent(documentSnapshot, e);
            }
        });

    }

    //functions

    public boolean NotifyDriverAccepted() {
        return checkDriver;
    }


    private void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
        // code to get document and finding what changed in here
        //in case of error
        if (e!=null){
            Log.i("rideListener Error","Listen failed: "+e);
            return;
        }

        if (documentSnapshot != null && documentSnapshot.exists()) {
            Ride newRide = Ride.build(documentSnapshot.getData());
            Log.d("RideTracker", "OnEvent");
//            if (newRide.getRideStatus() != ride.getRideStatus()) {
                rideEventListener.onStatusChange(newRide);
//            }

//            ActiveUser.setCurrentRide(newRide);
//            ride = newRide;
            ride.setStatus(newRide.getRideStatus());
            ride.setDriverUsername(newRide.getDriverUsername());

//            if ("DRIVERACCEPTED".equals(documentSnapshot.getData().get("status").toString())){
//                Log.i("rideListener","tracking status driver");
//                checkDriver = true;
//                ride.driverAccept();
//                ActiveUser.setCurrentRide(ride);
//                rideEventListener.onStatusChange(ride);
//            }
//
//            if ("RIDERACCEPTED".equals(documentSnapshot.getData().get("status").toString())){
//                Log.i("rideListener","tracking status rider");
//                checkDriver = false;
//                ride.riderAccept();
//                ActiveUser.setCurrentRide(ride);
//                rideEventListener.onStatusChange(ride);
//            }

        } else {
            Log.i("rideListener","null");
        }

    }
    public void addListener(RideEventListener listener) {
        rideEventListener = listener;
    }
}