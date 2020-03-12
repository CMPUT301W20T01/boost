package ca.ualberta.boost;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import ca.ualberta.boost.models.Driver;
import ca.ualberta.boost.models.Ride;


public class RideRequestArrayAdapter extends ArrayAdapter<RideRequest> {

    private Context adapterContext;
    private int adapterResource;
    public RideRequestArrayAdapter(@NonNull Context context, int resource, @NonNull List<RideRequest> objects) {
        super(context, resource, objects);
        adapterContext = context;
        adapterResource = resource;
    }

    //gets view and attaches it to layout
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String riderName = getItem(position).getRiderUserName();
        String fare = getItem(position).getFare();
        String startLocation = getItem(position).getStartLocation();
        String endLocation = getItem(position).getEndLocation();

        LayoutInflater inflater = LayoutInflater.from(adapterContext);
        convertView = inflater.inflate(adapterResource, parent, false);

        TextView riderUserName = convertView.findViewById(R.id.riderUserName);
        TextView rideFare = convertView.findViewById(R.id.amountOfferedRide);
        TextView rideStart = convertView.findViewById(R.id.rideStart);
        TextView rideEnd = convertView.findViewById(R.id.rideEnd);

        riderUserName.setText("username: "+riderName);
        rideFare.setText("fare: "+fare);
        rideStart.setText("start: "+startLocation);
        rideEnd.setText("end: "+endLocation);

        return convertView;
    }
}