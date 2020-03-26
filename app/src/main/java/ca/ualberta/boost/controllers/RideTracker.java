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
import ca.ualberta.boost.models.Ride;
import ca.ualberta.boost.models.RideStatus;

public class RideTracker {
    RideEventListener rideEventListener;
    Ride ride;

    public static final String TAG1 = "rides/";
    public static final String TAG2 = "status";
    //constructor
    public RideTracker(Ride ride) {
        this.ride = ride;
        Log.i("rideListener","Snapshot listener");
        FirebaseFirestore.getInstance()
                .collection("rides")
                .document(ride.id())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        Log.i("rideListener","Snapshot listener");
                        RideTracker.this.onEvent(documentSnapshot, e);
                    }
                });



        FirebaseDatabase.getInstance().getReference(TAG1).child(FirebaseAuth.getInstance().getUid()).child(ride.id()).child(TAG2).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i("rideListener","Snapshot listener");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //functions
    public void NotifyDriverAccept(){
        rideEventListener.onDriverAccepted(ride);
    }
    public void NotifyRiderAccept(){
        rideEventListener.onRiderAccepted(ride);
    }
    public void NotifyCancelled(){
        rideEventListener.onCancelled(ride);
    }
    public void NotifyFinished(){
        rideEventListener.onFinished(ride);
    }
    private void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
        // code to get document and finding what changed in here
        //in case of error
        if (e!=null){
            Log.i("rideListener Error","Listen failed: "+e);
            return;
        }

        if (documentSnapshot != null && documentSnapshot.exists()){
            Log.i("rideListener","Current data: " + documentSnapshot.getData());
        } else {
            Log.i("rideListener","null");
        }

    }
    public void addListener(RideEventListener listener) {
        rideEventListener = listener;
    }
}