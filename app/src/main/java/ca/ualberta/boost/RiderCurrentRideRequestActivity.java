package ca.ualberta.boost;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

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

import java.util.ArrayList;
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

        driverUserName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(driverUserName.getText().toString().matches("")){
                    Toast.makeText(RiderCurrentRideRequestActivity.this, "No Driver Yet", Toast.LENGTH_SHORT).show();
                }
                else {
                    //Launch the profile page with the driver's information
                    Bundle bundle = new Bundle();
                    //send username to contact information fragment
                    bundle.putString("username",driverUserName.getText().toString());
                    UserContactInformationFragment userContactInformationFragment = new UserContactInformationFragment();
                    userContactInformationFragment.setArguments(bundle);
                    userContactInformationFragment.show(getSupportFragmentManager(), "my fragment");
//                    FragmentManager manager = getSupportFragmentManager();

                }
                return false;
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
                        startLocation.setText("Start Location: "+currentRide.getStartLocation().toString());
                        endLocation.setText("End Location: "+currentRide.getEndLocation().toString());
                        fare.setText("Fare: "+Double.toString(currentRide.getFare()) + "QR Bucks");
                        status.setText("Status: "+currentRide.getRideStatus().toString());
                        driverUserName.setText(currentRide.getDriverUsername());
                        Log.i("testValue",currentRide.getRiderUsername());
                        Log.i("testValue",currentUser.getUsername());
                        riderUserName.setText("Rider: "+currentRide.getRiderUsername());
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

    //function to cancel a ride request
    private void cancelRideRequest() {
        Log.i("TAG","..........cancelling ride.......");
        Promise<Collection<Ride>> requests = RideStore.getRequests();
        requests.addOnSuccessListener(new OnSuccessListener<Collection<Ride>>() {
            @Override
            public void onSuccess(Collection<Ride> rides) {
                for (Ride currentRide : rides) {
                    if (currentRide.getRiderUsername().equals(currentUser.getUsername())) {
                        Log.i("TEST","Cancelling "+currentRide.getRiderUsername());
                        currentRide.cancel();
                        RideStore.saveRide(currentRide);
                        ActiveUser.cancelRide();
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
        driverUserName.setText("");
    }
}

