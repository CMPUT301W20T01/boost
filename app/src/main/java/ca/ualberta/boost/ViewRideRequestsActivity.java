package ca.ualberta.boost;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import ca.ualberta.boost.models.Promise;
import ca.ualberta.boost.models.Ride;
import ca.ualberta.boost.models.Rider;
import ca.ualberta.boost.stores.RideStore;

/**
 * ViewRidesRequestsActivity is responsible for allowing drivers to search for
 * open ride requests, and displays these ride requests
 */

public class ViewRideRequestsActivity extends MapActivity {

    // views
    private LinearLayout searchesLayout;
    private EditText searchStartText;
    private Button cancelButton;

    // firebase
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private CollectionReference handler;

    // attributes
    private LatLng startLocation;
    private Collection<Ride> rideList;
    private GoogleMap mMap;
    private RideStore rideStore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get views
        searchesLayout = findViewById(R.id.searchesLayout);
        searchStartText = findViewById(R.id.searchStartEditText);
        cancelButton = findViewById(R.id.cancelButton);

        // firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        handler = db.collection("rides");

        rideStore = rideStore.getInstance();
        rideList = new ArrayList<>();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_view_ride_requests;
    }

    @Override
    protected MapFragment getMapFragment() {
        return (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
    }

    @Override
    protected void init() {
        mMap = getMap();

        // cancel button
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchDriverMainPage();
            }
        });

        // start search bar
        searchStartText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        || event.getAction() == KeyEvent.KEYCODE_ENTER){
                    handleSearch(searchStartText);
                }
                return false;
            }
        });

    }

    /**
     * Handles search bar for searching a start location
     * Moves camera to the searched location
     * @param searchEditText
     *      The EditText of the search bar
     */
    private void handleSearch(EditText searchEditText){
        Log.d("TestingViewRide", "in handleSearch");
        String searchString = searchEditText.getText().toString();
        startLocation = geoLocate(searchString);
        moveCamera(startLocation, 20);
        displayRequests();
    }

    /**
     * Displays open ride requests that are within a certain distance
     * of the Driver's specified start location
     */
    private void displayRequests(){
        Log.d("TestingViewRide", "in displayRequests");
        fillRideList();
        // place marker for each ride
        for (Ride ride : rideList){
            mMap.addMarker(new MarkerOptions()
                    .title(ride.getRider_username())
                    .position(ride.getStartLocation()));
        }

    }



    /**
     * Fills the rideList with pending ride requests from the database
     */
    private void fillRideList(){
        Promise<Collection<Ride>> ridePromise = rideStore.getRequests();
        ridePromise.addOnSuccessListener(new OnSuccessListener<Collection<Ride>>() {

            @Override
            public void onSuccess(Collection<Ride> rides) {
                Log.d("TestingViewRide", "success");
                if (!rides.isEmpty()) {
                    for (Ride ride : rides) {
                        LatLng rideStartLocation = ride.getStartLocation();
                        float[] results = new float[1];
                        Location.distanceBetween(startLocation.latitude, startLocation.latitude,
                                rideStartLocation.latitude, rideStartLocation.longitude, results);
                        // if distance is smaller than 5km add to rideList
                        Log.d("TestingViewRide", Float.toString(results[0]));
                        if (results[0] < 5000) {
                            rideList.add(ride);
                            Log.d("TestingViewRide", "ride added");
                        }
                    }
                } else{
                    Log.d("TestingViewRide", "rides is empty");
                }
            }
        });
        ridePromise.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TestingViewRide", "failure");
            }
        });

    }

    /**
     * Go to DriverMainPage activity and finish this activity
     */
    private void launchDriverMainPage(){
        Intent intent = new Intent(this, DriverMainPage.class);
        startActivity(intent);
        finish();
    }

}