package ca.ualberta.boost;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import ca.ualberta.boost.models.ActiveUser;
import ca.ualberta.boost.models.Ride;
import ca.ualberta.boost.stores.RideStore;


/**
 * RiderMainPage defines the Home Page activity for Riders
 * This class presents the map and necessary views for Riders to
 * view their profile, request a ride, view current ride, and
 * complete a ride.
 */

public class RiderMainPage extends MapActivity implements RideRequestSummaryFragment.OnFragmentInteractionListener {

    // constant values
    private static final String TAG = "RiderMainPage";

    //firebase
    private FirebaseAuth auth;

    // views
    private Button viewRequestButton;
    private Button requestRideButton;
    private Button viewProfileButton;
    private Button confirmRequestButton;
    private Button cancelRequestButton;
    private Button logoutButton;
    private EditText searchPickupText;
    private EditText searchDestinationText;
    private LinearLayout searchesLayout;
    private LinearLayout confirmCancelLayout;
    private LinearLayout viewRequestLayout;

    // attributes
    private Ride ride;
    public Marker pickupMarker;
    public Marker destinationMarker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();

        // get views
        searchPickupText = findViewById(R.id.searchPickupEditText);
        searchDestinationText = findViewById(R.id.searchDestinationEditText);
        searchesLayout = findViewById(R.id.searchesLayout);
        requestRideButton = findViewById(R.id.requestRideButton);
        viewProfileButton = findViewById(R.id.viewProfileButton);
        logoutButton = findViewById(R.id.logoutButton);
        confirmCancelLayout = findViewById(R.id.confirmCancelLayout);
        viewRequestLayout = findViewById(R.id.viewRequestLayout);
        confirmRequestButton = findViewById(R.id.confirmRequestButton);
        cancelRequestButton = findViewById(R.id.cancelRequestButton);
        viewRequestButton = findViewById(R.id.viewRideRequestButton);
    }

    /**
     *  Creates a pending ride for the rider and sends it to the database.
     *  This method is run when "accept" is pressed from the RideRequestSummaryFragment
     */
    @Override
    public void onAcceptPressed() {
        setRiderMainPageVisibility();
        // makes a ride with pending status and automatic date
        Ride finalRide = new Ride(ride.getStartLocation(), ride.getEndLocation(),
                ride.getFare(), ride.getRiderUsername());
        ActiveUser.setCurrentRide(finalRide);

        //RUN PENDING FRAGMENT
        new RiderAcceptedFragment(ride).show(getSupportFragmentManager(), "Pending_Driver_Accept");

    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_rider_main_page;
    }

    @Override
    protected MapFragment getMapFragment() {
        return (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
    }

    @Override
    protected void init() {
        GoogleMap mMap = getMap();

        // init markers and make them invisible
        pickupMarker = mMap.addMarker(new  MarkerOptions()
                .title("Pickup")
                .position(new LatLng(0,0))
                .draggable(true)
                .visible(false)
        );

        destinationMarker = mMap.addMarker(new  MarkerOptions()
                .title("Destination")
                .position(new LatLng(0,0))
                .draggable(true)
                .visible(false)
        );

        // listener for marker drag
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                updateRideLocation(marker);
                // TODO: update the text in the search bar to match the marker's new position
                updateSearchBar(marker);

            }
        });

        viewProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchProfileScreen();
            }
        });

        viewRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCurrentRequestActivity();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                ActiveUser.logout();
                launchHomeScreen();
            }
        });

        requestRideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRequestRideClick();
            }
        });
        cancelRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleCancelRideClick();
            }
        });

        confirmRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ride.setEndLocation(destinationMarker.getPosition());
                ride.setStartLocation(pickupMarker.getPosition());
                ride.setFare(ride.baseFare());
                new RideRequestSummaryFragment(ride).show(getSupportFragmentManager(), "RIDE_SUM");
            }
        });
    }


    /**
     * Allows user to choose start and end location, price, and request a ride
     * This function is run when the "Request Ride" button is clicked.
     */
    private void handleRequestRideClick() {
        ride = new Ride(0.00, ActiveUser.getUser().getUsername());
        setRequestLocationPageVisibility();
        // pickup search bar
        searchPickupText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        || event.getAction() == KeyEvent.KEYCODE_ENTER){
                    Log.d("RIDER", "Got input");
                    handleSearch(searchPickupText, "Pickup");
                }
                return false;
            }
        });
        // destination search bar
        searchDestinationText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        || event.getAction() == KeyEvent.KEYCODE_ENTER){
                    handleSearch(searchDestinationText, "Destination");
                }
                return false;
            }
        });
    }

    /**
     * Handles the search bars for requesting a ride
     * Moves camera and places/moves a marker on the map if search is successful
     * @param searchEditText
     *      The EditText of the search bar
     * @param markerTitle
     *      The marker to place/move
     */
    private void handleSearch(EditText searchEditText, String markerTitle) {
        String searchString = searchEditText.getText().toString();
        LatLng latLng = geoLocate(searchString);
            if (markerTitle.equals("Pickup")){
                moveMarker(pickupMarker, latLng);
                updateRideLocation(pickupMarker);
            } else {
                moveMarker(destinationMarker, latLng);
                updateRideLocation(destinationMarker);
            }
            // if both markers are visible
            if (pickupMarker.isVisible() && destinationMarker.isVisible()){
                zoomToMarkers(pickupMarker, destinationMarker);
            }
    }

    /**
     * Update the ride with the marker's new position
     * @param marker
     *      the marker to get the position with which we update ride
     */
    private void updateRideLocation(Marker marker){
        if (marker.getTitle().equals(pickupMarker.getTitle())){
            ride.setStartLocation(marker.getPosition());
            Toast.makeText(RiderMainPage.this, "updated start location", Toast.LENGTH_SHORT).show();
        } else{
            ride.setEndLocation(marker.getPosition());
            Toast.makeText(RiderMainPage.this, "updated end location", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Updates the search bar corresponding to the marker
     * with the new address
     * @param marker
     *      Marker that has the new location
     */
    private void updateSearchBar(Marker marker){
        if (marker.getTitle().equals(pickupMarker.getTitle())){
            String address = reverseGeoLocate(marker.getPosition());
            searchPickupText.setText(address);
        } else {
            String address = reverseGeoLocate(marker.getPosition());
            searchDestinationText.setText(address);
        }
    }

    /**
     * Hides views associated with ride requesting
     */
    private void handleCancelRideClick() {
        setRiderMainPageVisibility();
    }

    /**
     * Shows views associated with ride requesting
     * and hides views associated with the main home page
     */
    private void setRequestLocationPageVisibility() {
        viewRequestLayout.setVisibility(View.GONE);
        confirmCancelLayout.setVisibility(View.VISIBLE);
        searchesLayout.setVisibility(View.VISIBLE);
    }

    /**
     * Shows views associated with the main home page
     * and hides views associated with ride requesting
     */
    private void setRiderMainPageVisibility() {
        searchDestinationText.setText("");
        searchPickupText.setText("");
        viewRequestLayout.setVisibility(View.VISIBLE);
        confirmCancelLayout.setVisibility(View.GONE);
        searchesLayout.setVisibility(View.GONE);
        pickupMarker.setVisible(false);
        destinationMarker.setVisible(false);
    }

    private void launchCurrentRequestActivity(){
        Intent intent = new Intent(this, RiderCurrentRideRequestActivity.class);
        startActivity(intent);
    }

    private void launchHomeScreen(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void launchProfileScreen() {
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }

}
