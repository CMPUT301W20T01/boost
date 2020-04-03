package ca.ualberta.boost.controllers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import ca.ualberta.boost.R;
import ca.ualberta.boost.models.Ride;

/**
 * The RideListAdapter is responsible for displaying the ride in the list view
 */

public class RideListAdapter extends ArrayAdapter<Ride> {

    private static final DateFormat dateFormat = new SimpleDateFormat("MMM dd   h:mm a", Locale.CANADA);

    public RideListAdapter(@NonNull Context context, ArrayList<Ride> rides) {
        super(context, R.layout.ride_details, rides);
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.ride_details, parent, false);

        Ride individualRide = getItem(position);
        TextView riderName = customView.findViewById(R.id.past_rider_username);
        TextView rideDate = customView.findViewById(R.id.past_date);
        TextView rideFare = customView.findViewById(R.id.past_fare);

        riderName.setText(individualRide.getRiderUsername());
        rideDate.setText(dateFormat.format(individualRide.getRequestTime()));

        BigDecimal roundedFare = individualRide.getRoundedFare();
        rideFare.setText(NumberFormat.getCurrencyInstance().format(roundedFare));

        return customView;
    }
}
