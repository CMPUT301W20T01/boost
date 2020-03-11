package ca.ualberta.boost;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class RiderCurrentRideRequestActivity extends AppCompatActivity {

    TextView startLocation;
    TextView endLocation;
    TextView fare;
    TextView status;
    TextView driverUserName;

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private CollectionReference handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_current_ride_request);
        startLocation = findViewById(R.id.requestStartLocation);
        endLocation = findViewById(R.id.requestEndLocation);
        fare = findViewById(R.id.requestFare);
        status = findViewById(R.id.requestStatus);
        driverUserName = findViewById(R.id.driverUserName);

        //firebase stuff
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        handler = db.collection("rides");
        setRideRequest();
//        noRideRequest();

    }

    private void setRideRequest(){

        handler.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document: task.getResult()){
                                if(auth.getUid().matches(document.get("riderID").toString())){
                                    startLocation.setText(document.get("start_location").toString());
                                    endLocation.setText(document.get("end_location").toString());
                                    fare.setText(document.get("fare").toString());
                                    status.setText(document.get("status").toString());
                                    driverUserName.setText(document.get("driver").toString());
                                }

                            }
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RiderCurrentRideRequestActivity.this, "Currently no active request", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void noRideRequest(){
        if(fare.getText().toString().matches("")){
            Toast.makeText(this, "No active ride bro", Toast.LENGTH_LONG).show();
        }
    }
}
