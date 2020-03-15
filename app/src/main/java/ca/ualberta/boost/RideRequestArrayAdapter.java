package ca.ualberta.boost;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.ualberta.boost.models.Driver;
import ca.ualberta.boost.models.Ride;

/**
 * RideRequestArrayAdapter is responsible for displaying the information in the ArrayList<RideRequest>
 * Also allows a driver to click accept (for now only a toast is displayed that says accepted)
 */

public class RideRequestArrayAdapter extends ArrayAdapter<RideRequest> {

    //firebase stuff
    //firebase
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private CollectionReference handler;
    FirebaseUser user;
    DocumentReference documentReference;
    public int i = 0;

    TextView riderUserName;

    private Context adapterContext;
    private int adapterResource;
    public RideRequestArrayAdapter(@NonNull Context context, int resource, @NonNull List<RideRequest> objects) {
        super(context, resource, objects);
        adapterContext = context;
        adapterResource = resource;

        //firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        handler = db.collection("rides");
        user = FirebaseAuth.getInstance().getCurrentUser();
//        documentReference = db.collection("rides");

    }

    //gets view and attaches it to layout
//    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final String riderName = getItem(position).getRiderUserName();
        String fare = getItem(position).getFare();
        String startLocation = getItem(position).getStartLocation();
        String endLocation = getItem(position).getEndLocation();

        LayoutInflater inflater = LayoutInflater.from(adapterContext);
        convertView = inflater.inflate(adapterResource, parent, false);

        riderUserName = convertView.findViewById(R.id.riderUserName);
        TextView rideFare = convertView.findViewById(R.id.amountOfferedRide);
        TextView rideStart = convertView.findViewById(R.id.rideStart);
        TextView rideEnd = convertView.findViewById(R.id.rideEnd);

        riderUserName.setText("username: "+riderName);
        rideFare.setText("fare: "+fare);
        rideStart.setText("start: "+startLocation);
        rideEnd.setText("end: "+endLocation);

        Button acceptRideButton = convertView.findViewById(R.id.acceptRideButton);


        acceptRideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(adapterContext, "Accepted Ride", Toast.LENGTH_SHORT).show();
                Log.i("value",riderUserName.getText().toString());
                DocumentReference documentReferencee = db.collection("rides").document(riderUserName.getText().toString().trim());
                Map<String, Object> map = new HashMap<>();
                map.put("status","Accepted");
                documentReferencee.update(map);
//                FirebaseAuth auth1 = FirebaseAuth.getInstance();
//                FirebaseFirestore db1 = FirebaseFirestore.getInstance();
//                FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
//                DocumentReference documentReference1 = db1.collection("rides").document("random");
//                Map<String, Object> map = new HashMap<>();
//                map.put("status","Accepted");
//                documentReference1.update(map);
            }
        });

        return convertView;
    }

    private void setDriver(String riderEmail){
        FirebaseAuth auth1 = FirebaseAuth.getInstance();
        FirebaseFirestore db1 = FirebaseFirestore.getInstance();
        FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference documentReference1 = db1.collection("rides").document(riderEmail);
        Map<String, Object> map = new HashMap<>();
//        map.put("Driver",user1.getEmail());
        map.put("status","Accepted");
        documentReference1.update(map);
    }
}

