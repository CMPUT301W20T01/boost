package ca.ualberta.boost;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import ca.ualberta.boost.models.ActiveUser;
import ca.ualberta.boost.models.Promise;
import ca.ualberta.boost.models.Ride;
import ca.ualberta.boost.models.User;
import ca.ualberta.boost.stores.RideStore;

/**
 * RiderCurrentRideRequestActivity is responsible for displaying the current request of a rider
 * also allows the rider to cancel an active ride request
 */

public class RiderCurrentRideRequestActivity extends AppCompatActivity {

    TextView startLocation;
    TextView endLocation;
    TextView fare;
    TextView status;
    TextView driverUserName;
    TextView riderUserName;
    Button cancelButton;

    //firebase
//    private FirebaseAuth auth;
//    private FirebaseFirestore db;
//    private CollectionReference handler;
//    DocumentReference documentReference;
//    FirebaseUser user;

    private Promise<Collection<Ride>> requests;
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_current_ride_request);
        startLocation = findViewById(R.id.requestStartLocation);
        endLocation = findViewById(R.id.requestEndLocation);
        fare = findViewById(R.id.requestFare);
        status = findViewById(R.id.requestStatus);
        driverUserName = findViewById(R.id.driverUserName);
        riderUserName = findViewById(R.id.usernameRideRequest);
        cancelButton = findViewById(R.id.cancelRideRequestButton);

        //firebase
//        auth = FirebaseAuth.getInstance();
//        db = FirebaseFirestore.getInstance();
//        handler = db.collection("rides");
//        user = FirebaseAuth.getInstance().getCurrentUser();
//        documentReference = db.collection("rides").document(user.getEmail());

        requests = RideStore.getRequests();
        currentUser = ActiveUser.getUser();

        setRideRequest2();
        //setRideRequest();
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelRideRequest();
            }
        });

    }

    //function to retrieve the relevant information about a ride request for the current user
    private void setRideRequest2(){
        Log.i("RESULT","Attempt to retrieve requests");
        requests.addOnSuccessListener(new OnSuccessListener<Collection<Ride>>() {
            @Override
            public void onSuccess(Collection<Ride> rides) {
                Log.i("RESULT","onSuccess to retrieve requests");
                Log.i("RESULT","rides numbers: "+rides.size());
                for (Ride currentRide : rides) {
                    if (currentRide.getRiderUsername().equals(currentUser.getUsername())){
                        startLocation.setText(currentRide.getStartLocation().toString());
                        endLocation.setText(currentRide.getEndLocation().toString());
                        fare.setText(Double.toString(currentRide.getFare()));
                        status.setText(currentRide.getRideStatus().toString());
                        driverUserName.setText(currentRide.getDriverUsername());
                        Log.i("testValue",currentRide.getRiderUsername());
                        Log.i("testValue",currentUser.getUsername());
                        riderUserName.setText(currentRide.getRiderUsername());
                }
            }
        }
        });
        requests.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("RESULT","onFailure to retrieve requests");
                Toast.makeText(RiderCurrentRideRequestActivity.this, "Currently no active request", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //function to retrieve the relevant information about a ride request for the current user
//    private void setRideRequest(){
//        handler.get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if(task.isSuccessful()){
//                            for(QueryDocumentSnapshot document: task.getResult()){
//                                if((user.getEmail().matches(document.get("rider").toString()))&& document.get("status").toString().matches("Pending")){
//                                    startLocation.setText("Start Location: "+ document.get("start_location").toString());
//                                    endLocation.setText("End Location: "+ document.get("end_location").toString());
//                                    fare.setText("Fare: "+ document.get("fare").toString());
//                                    status.setText("Status: "+ document.get("status").toString());
////                                    driverUserName.setText(document.get("driver").toString());
//                                    Log.i("testValue",document.get("rider").toString());
//                                    Log.i("testValue",user.getEmail());
//                                    riderUserName.setText("Username: "+document.get("rider").toString());
//                                }
//
//                            }
//                        }
//
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(RiderCurrentRideRequestActivity.this, "Currently no active request", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }


    //function to cancel a ride request
    private void cancelRideRequest() {
//        Map<String, Object> map = new HashMap<>();
//        map.put("status", "Cancelled");
//        documentReference.update(map)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        Toast.makeText(RiderCurrentRideRequestActivity.this, "Ride Request Cancelled", Toast.LENGTH_LONG).show();
//                    }
//                });
        requests.addOnSuccessListener(new OnSuccessListener<Collection<Ride>>() {
            @Override
            public void onSuccess(Collection<Ride> rides) {
                for (Ride currentRide : rides) {
                    if (currentRide.getRiderUsername().equals(currentUser.getUsername())) {
                        currentRide.cancel(); //DOESNT ACTUALLY CHANGE FIREBASE
                    }
                }
            }
        });
        clear();
    }

    //function to clear all TextViews
    private void clear(){
        startLocation.setText("");
        endLocation.setText("");
        fare.setText("");
        status.setText("");
        riderUserName.setText("");
    }
}

