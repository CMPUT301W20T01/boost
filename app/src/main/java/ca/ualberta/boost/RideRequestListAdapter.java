package ca.ualberta.boost;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class RideRequestListAdapter extends ArrayAdapter<RideRequest> {

    private Context layoutContext;
    private int layoutResource;

    public RideRequestListAdapter(@NonNull Context context, int resource, @NonNull List<RideRequest> objects) {
        super(context, resource, objects);
        layoutContext = context;
        layoutResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String start = getItem(position).getPickup();
        String end = getItem(position).getDestination();
        String amount = getItem(position).getAmount();
        String email = getItem(position).getEmail();

        LayoutInflater inflater = LayoutInflater.from(layoutContext);
        convertView = inflater.inflate(layoutResource, parent, false);

        TextView tStart = convertView.findViewById(R.id.rideStart);
        TextView tEnd = convertView.findViewById(R.id.rideEnd);
        TextView tAmount = convertView.findViewById(R.id.amountOfferedRide);
        TextView tEmail = convertView.findViewById(R.id.riderEmail);

        tStart.setText(start);
        tEnd.setText(end);
        tAmount.setText(amount);
        tEmail.setText(email);

        return convertView;
    }
}
