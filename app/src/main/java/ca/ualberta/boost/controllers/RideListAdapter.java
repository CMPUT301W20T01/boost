package ca.ualberta.boost.controllers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ca.ualberta.boost.R;
import ca.ualberta.boost.models.Ride;

public class RideListAdapter extends ArrayAdapter<Ride> {

    public RideListAdapter(@NonNull Context context, ArrayList<Ride> rides) {
        super(context, R.layout.ride_details, rides);
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.ride_details, parent, false);

        Ride individualRide = getItem(position);
        TextView driverName = customView.findViewById(R.id.rideDetailsDriver);
        TextView riderName = customView.findViewById(R.id.rideDetailsRider);
        TextView startLoc = customView.findViewById(R.id.rideDetailsPickup);
        TextView endLoc = customView.findViewById(R.id.rideDetailsDropoff);

        driverName.setText(individualRide.getDriverUsername());
        riderName.setText(individualRide.getRiderUsername());
        startLoc.setText(individualRide.getStartLocation().toString());
        endLoc.setText(individualRide.getEndLocation().toString());

        return customView;
    }
}
