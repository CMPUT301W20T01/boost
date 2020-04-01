package ca.ualberta.boost;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nullable;

import ca.ualberta.boost.controllers.RideListAdapter;
import ca.ualberta.boost.models.ActiveUser;
import ca.ualberta.boost.models.Promise;
import ca.ualberta.boost.models.Ride;
import ca.ualberta.boost.models.Rider;
import ca.ualberta.boost.models.User;
import ca.ualberta.boost.stores.RideStore;

import static ca.ualberta.boost.stores.RideStore.getPastRides;

public class RideHistoryActivity extends AppCompatActivity {

    ImageButton backButton;
    TextView username;
    ListView listView;
    User user;

    ArrayList<Ride> rides = new ArrayList<Ride>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_history);

        backButton = findViewById(R.id.history_go_back_button);
        username = findViewById(R.id.history_username);
        listView = findViewById(R.id.ride_history);

        user = ActiveUser.getUser();
        username.setText(user.getUsername());

        getHistory();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    /**
     * Retrieves and displays ride requests that the driver was a part of
     */
    private void getHistory(){
        getPastRides(user.getUsername()).addOnSuccessListener(new OnSuccessListener<Collection<Ride>>() {
            @Override
            public void onSuccess(Collection<Ride> rideCollection) {
                Log.d("TestingViewRide", "success");
                if (!rideCollection.isEmpty()) {
                    rides.addAll(rideCollection);
                    // add fnxality to remove rides that were canceled?  just go thru rides and throw out ones w/ status 5 or w.e it is

                    ListAdapter adapter = new RideListAdapter(RideHistoryActivity.this, rides);
                    listView.setAdapter(adapter);

                } else {
                    Log.d("TestingViewRide", "no past rides complete");
                    Toast.makeText(RideHistoryActivity.this, "no past rides completed", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TestingViewRide", "finding past rides failure");
            }
        });
    }
}
