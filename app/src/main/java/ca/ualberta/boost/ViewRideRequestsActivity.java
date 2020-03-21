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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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

import ca.ualberta.boost.models.Ride;
import ca.ualberta.boost.models.Rider;
import ca.ualberta.boost.models.User;
import ca.ualberta.boost.stores.UserStore;

/**
 * ViewRidesRequestsActivity is responsible for allowing drivers to search for
 * open ride requests, and displays these ride requests
 */

public class ViewRideRequestsActivity extends MapActivity implements RideRequestFragment.OnFragmentInteractionListener {

    // constants
    BitmapDescriptor SPECIAL = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);

    // views
    private LinearLayout searchesLayout;
    private EditText searchStartText;
    private Button cancelButton;
    private Button detailsButton;

    // firebase
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private CollectionReference handler;

    // attributes
    private LatLng startLocation;
    private ArrayList<Ride> rideList;
    private GoogleMap mMap;
    private ArrayList<Marker> startMarkers;
    private Marker endMarker;
    private Ride chosenRide;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get views
        searchesLayout = findViewById(R.id.searchesLayout);
        searchStartText = findViewById(R.id.searchStartEditText);
        cancelButton = findViewById(R.id.cancelButton);
        detailsButton = findViewById(R.id.detailsButton);

        // firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        handler = db.collection("rides");
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
        startMarkers = new ArrayList<>();
        endMarker = mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(0,0))
                                .visible(false));

        // cancel button
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchDriverMainPage();
            }
        });

        // view ride details button
        detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chosenRide != null){
                    String pickupAddress = reverseGeoLocate(chosenRide.getStartLocation());
                    String destinationAddress = reverseGeoLocate(chosenRide.getEndLocation());
                    new RideRequestFragment(chosenRide, pickupAddress, destinationAddress).show(getSupportFragmentManager(), "REQ_SUM");
                }
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

        // on marker click
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
               // if the marker is a start location marker
               if (startMarkers.contains(marker)){
                   // make markers less opaque
                   for (Marker m : startMarkers){
                       m.setAlpha(0.5f);
                   }
                   int index = startMarkers.indexOf(marker);
                   chosenRide = rideList.get(index);
                   // highlight start marker
                   marker.setAlpha(1.0f);
                   // place end location marker
                    endMarker.setPosition(chosenRide.getEndLocation());
                    endMarker.setVisible(true);
                   // zoom camera to both markers
                   zoomToMarkers(marker, endMarker);
                   // show button that says VIEW DETAILS
                   detailsButton.setVisibility(View.VISIBLE);
               }
                return true;
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
        // hide VIEW DETAILS button
        detailsButton.setVisibility(View.GONE);
        // set end marker as invisible
        endMarker.setVisible(false);
        String searchString = searchEditText.getText().toString();
        startLocation = geoLocate(searchString);
        moveCamera(startLocation, 15);
        displayRequests();
    }

    /**
     * Displays open ride requests that are within a certain distance
     * of the Driver's specified start location
     */
    private void displayRequests(){
        rideList = new ArrayList<>(); // comment this out later, its in fillridelist
        // clear map of previously searched requests
        for (int i = 0; i < startMarkers.size(); i++){
            Marker m = startMarkers.get(i);
            m.remove();
        }
        startMarkers.clear();
       // fillRideList();
        // test ride from wem to misericordia
        rideList.add(new Ride(new LatLng(53.522515, -113.624191), new LatLng(53.5209, -113.6120), 13.5, "username"));
        // test ride from thorncliffe school to aldergrove school
        rideList.add(new Ride(new LatLng(53.517, -113.624), new LatLng(53.517497, -113.631613), 13.5, "username2"));
        // place markers for rides
        for (int i = 0; i < rideList.size(); i++){
            Ride ride = rideList.get(i);
            startMarkers.add(mMap.addMarker(new MarkerOptions()
                    .position(ride.getStartLocation())));
            }
    }



    /**
     * Fills the rideList with pending ride requests from the database
     */
    private void fillRideList(){
        rideList = new ArrayList<>();
        handler.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.get("status").toString().matches("Pending")) {
                                    Ride ride = Ride.build(document.getData());
                                    float[] results = new float[1];
                                    final LatLng rideStartLocation = ride.getStartLocation();
                                    Location.distanceBetween(startLocation.latitude, startLocation.latitude,
                                            rideStartLocation.latitude, rideStartLocation.longitude, results);
                                    // if distance is smaller than 5km add to ride list
                                    if (results[0] < 5000){
                                        rideList.add(ride);
                                    }
                                }
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ViewRideRequestsActivity.this, "Please contact your database administrator", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Driver accepted ride, send new ride to database
     * @param newRide
     */
    @Override
    public void onAcceptPressed(Ride newRide) {
        /*
         TODO: set newRide.driver_username to current user's username
         set newRide's status to accepted
         send ride to database
         goto driver ride page activity
         */
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