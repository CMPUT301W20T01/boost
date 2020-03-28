package ca.ualberta.boost;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.core.Tag;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Collection;

import ca.ualberta.boost.controllers.RideEventListener;
import ca.ualberta.boost.controllers.RideTracker;
import ca.ualberta.boost.models.ActiveUser;
import ca.ualberta.boost.models.Promise;
import ca.ualberta.boost.models.Ride;
import ca.ualberta.boost.models.RideStatus;
import ca.ualberta.boost.models.User;
import ca.ualberta.boost.stores.RideStore;
import ca.ualberta.boost.stores.UserStore;

/**
 * ViewRidesRequestsActivity is responsible for allowing drivers to search for
 * open ride requests, and displays these ride requests
 */

public class ViewRideRequestsActivity extends MapActivity implements RequestDetailsFragment.OnFragmentInteractionListener, RiderAcceptedFragment.OnFragmentInteractionListener{

    // constants
    BitmapDescriptor SPECIAL = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);

    // views
    private LinearLayout searchesLayout;
    private EditText searchStartText;
    private Button cancelButton;
    private Button detailsButton;

    // attributes
    private LatLng startLocation;
    private ArrayList<Ride> rideList;
    private GoogleMap mMap;
    private ArrayList<Marker> startMarkers;
    private Marker endMarker;
    private Ride chosenRide;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    //RIDE EVENT LISTENER
    RideTracker rideTracker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get views
        searchesLayout = findViewById(R.id.searchesLayout);
        searchStartText = findViewById(R.id.searchStartEditText);
        cancelButton = findViewById(R.id.cancelButton);
        detailsButton = findViewById(R.id.detailsButton);


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
                                .title("Destination")
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

                    new RequestDetailsFragment(chosenRide, pickupAddress, destinationAddress).show(getSupportFragmentManager(), "REQ_SUM");
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
               // show both markers titles
               marker.showInfoWindow();
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
        fillRideList();
    }

    /**
     * Displays open ride requests that are within a certain distance
     * of the Driver's specified start location
     */
    private void displayRequests(){
        // clear map of previously searched requests
        for (int i = 0; i < startMarkers.size(); i++){
            Marker m = startMarkers.get(i);
            m.remove();
        }
        startMarkers.clear();
        // place markers for rides
        for (int i = 0; i < rideList.size(); i++){
            Log.d("TestingViewRide", "hi");
            Ride ride = rideList.get(i);
            Log.d("TestingViewRide", ride.getRiderUsername());
            startMarkers.add(mMap.addMarker(new MarkerOptions()
                    .position(ride.getStartLocation())
                    .title("Pickup")));
            }
    }



    /**
     * Fills the rideList with pending ride requests from the database
     */
    private void fillRideList(){
        rideList = new ArrayList<>();
        Promise<Collection<Ride>> ridePromise = RideStore.getRequests();
        ridePromise.addOnSuccessListener(new OnSuccessListener<Collection<Ride>>() {
            @Override
            public void onSuccess(Collection<Ride> rides) {
                Log.d("TestingViewRide", "success");
                if (!rides.isEmpty()) {
                    rideList.addAll(rides);
                    displayRequests();
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
     * Driver accepted ride, send new ride to database
     * @param newRide
     */
    @Override
    public void onAcceptPressed(Ride newRide) {
        User activeUser = ActiveUser.getUser();

        // update ride in database
        newRide.setDriverUsername(activeUser.getUsername());
        newRide.driverAccept();
        RideStore.saveRide(newRide);

        // set driver's current ride to this ride
        activeUser.setActiveRide(newRide);
        UserStore.saveUser(activeUser);

        new DriverAcceptedFragment(newRide).show(getSupportFragmentManager(), "Pending_Rider_Accept");

    }

    /**
     * Rider accepts the driver request offer
     * move over to CurrentRideActivity
     */
    @Override
    public void onRiderAcceptPressed(Ride newRide) {
        Intent intent = new Intent(this, CurrentRideActivity.class);
        startActivity(intent);
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