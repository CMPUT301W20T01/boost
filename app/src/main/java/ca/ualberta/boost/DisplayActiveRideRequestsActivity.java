package ca.ualberta.boost;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DisplayActiveRideRequestsActivity extends AppCompatActivity {

    public ArrayList<RideRequest> arr;
    ListView requests;

    FirebaseFirestore db;
    CollectionReference ref1;
    Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_active_ride_requests);

        db = FirebaseFirestore.getInstance();
        ref1 = db.collection("ride_requests");
//        auth = FirebaseAuth.getInstance();

        requests = findViewById(R.id.activeRequestsListView);
        arr = new ArrayList<>();
//        arr.add(new RideRequest("edmonton", "calgary", "20","alex"));
        RideRequestListAdapter adapter = new RideRequestListAdapter(this, R.layout.riderequestlayout, arr);
        requests.setAdapter(adapter);

        ref1.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document: task.getResult()){
                                String test1 = document.get("start_location").toString();
                                Log.i("testValue",document.get("start_location").toString());
                                String test2 = document.get("destination").toString();
                                Log.i("testValue",document.get("destination").toString());
                                String test3 = document.get("amount").toString();
                                Log.i("testValue",document.get("amount").toString());
                                String test4 = document.get("email").toString();
                                Log.i("testValue",document.get("email").toString());
                                RideRequest rideRequest = new RideRequest(test1, test2, test3, test4);
                                arr.add(new RideRequest("vancouver", "toronto", "200","alex"));
                                arr.add(rideRequest);
                            }
                        }
                    }
                });

//        ref1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                for(QueryDocumentSnapshot document: task.getResult()){
//                    ArrayList<RideRequest> tRides = new ArrayList<>();
//                    String test1 = document.get("start_location").toString();
//                    Log.i("testValue",document.get("start_location").toString());
//                    String test2 = document.get("destination").toString();
//                    Log.i("testValue",document.get("destination").toString());
//                    String test3 = document.get("amount").toString();
//                    Log.i("testValue",document.get("amount").toString());
//                    String test4 = document.get("email").toString();
//                    Log.i("testValue",document.get("email").toString());
//                    RideRequest rideRequest = new RideRequest(test1, test2, test3, test4);
//                    tRides.add(rideRequest);
//                    arr.add(new RideRequest("vancouver", "toronto", "200","alex"));
//                    Log.i("testValue",tRides.get(0).getEmail());
//                    arr.add(rideRequest);
//
//                }
//            }
//        });
        adapter.notifyDataSetChanged();

    }

    public void getRideRequests(){
        ref1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(QueryDocumentSnapshot document: task.getResult()){
                    String test1 = document.get("start_location").toString();
                    Log.i("testValue",document.get("start_location").toString());
                    String test2 = document.get("destination").toString();
                    String test3 = document.get("amount").toString();
                    String test4 = document.get("email").toString();
                    arr.add(new RideRequest(test1, test2, test3, test4));
                }

            }
        });
    }

    public void addRequest(RideRequest ride){
        arr.add(ride);
    }


}
