package ca.ualberta.boost;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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

public class ViewRideRequestsActivity extends AppCompatActivity {

    private ListView rideRequestListView;
    ArrayList<RideRequest> rideArrayList;

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private CollectionReference handler;

    RideRequestArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ride_requests);

        //firebase stuff
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        handler = db.collection("rides");

        rideRequestListView = findViewById(R.id.activeRequestsListView);
        rideArrayList = new ArrayList<>();
//        rideArrayList.add(new RideRequest("alex","yeg","yyc","67"));

        adapter = new RideRequestArrayAdapter(this, R.layout.riderequestlayout,rideArrayList);
        rideRequestListView.setAdapter(adapter);


        handler.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document: task.getResult()){
                                if(document.get("status").toString().matches("Pending")){
                                    String startLocation = (document.get("start_location").toString());
                                    String endLocation = (document.get("end_location").toString());
                                    String fare = (document.get("fare").toString());
                                    String status = (document.get("status").toString());
                                    String riderUserName = (document.get("rider").toString());
                                    rideArrayList.add(new RideRequest(riderUserName, startLocation, endLocation, fare));
                                    adapter.notifyDataSetChanged();
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

}

