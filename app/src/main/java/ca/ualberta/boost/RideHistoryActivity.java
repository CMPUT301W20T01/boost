package ca.ualberta.boost;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.Nullable;

import ca.ualberta.boost.controllers.RideListAdapter;
import ca.ualberta.boost.models.ActiveUser;
import ca.ualberta.boost.models.Ride;
import ca.ualberta.boost.models.User;
import ca.ualberta.boost.models.UserType;
import ca.ualberta.boost.stores.RideStore;

public class RideHistoryActivity extends AppCompatActivity {

    ImageButton backButton;
    TextView username;
    ListView listView;
    User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_history);

        backButton = findViewById(R.id.history_go_back_button);
        username = findViewById(R.id.history_username);
        listView = findViewById(R.id.ride_history_list);

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
    private void getHistory() {
        RideStore.getPastRides(user.getUsername()).addOnSuccessListener(new OnSuccessListener<Collection<Ride>>() {
            @Override
            public void onSuccess(Collection<Ride> rideCollection) {
                Log.d("TestingViewRide", "success");
                if (!rideCollection.isEmpty()) {
                    ArrayList<Ride> past_rides = new ArrayList<>();
                    past_rides.addAll(rideCollection);

                    ListAdapter adapter = new RideListAdapter(RideHistoryActivity.this, past_rides);
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
