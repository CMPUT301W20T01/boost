package ca.ualberta.boost;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import ca.ualberta.boost.models.Ride;

/**
 * RideRequestFragment defines a fragment that displays
 * a preliminary summary for a Ride request, showing the Ride's start location,
 * end location, cost and rider. The Driver accepts a Ride request using this fragment.
 */
public class RequestDetailsFragment extends DialogFragment {
    private RequestDetailsFragment.OnFragmentInteractionListener listener;
    private Ride ride;
    private String startAddress;
    private String endAddress;
    private TextView riderText;
    private TextView startText;
    private TextView endText;
    private TextView fareText;



    RequestDetailsFragment(Ride ride, String pickup, String destination){
        this.ride = ride;
        this.startAddress = pickup;
        this.endAddress = destination;
    }

    /**
     * Interface that enforces the implementing class to handle
     * what happens when the fragment's
     * positive button (accept) is pressed
     */
    public interface OnFragmentInteractionListener {
        void onAcceptPressed(Ride newRide);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RequestDetailsFragment.OnFragmentInteractionListener){
            listener = (RequestDetailsFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "must implement OnFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_ride_request, null);
        View titleView = LayoutInflater.from(getActivity()).inflate(R.layout.title_ride_summary, null);
        fareText = view.findViewById(R.id.fareText);
        riderText = view.findViewById(R.id.riderText);
        startText = view.findViewById(R.id.startText);
        endText = view.findViewById(R.id.endText);

        fareText.setText(Double.toString(ride.getFare()));
        riderText.setText(ride.getRiderUsername());

        riderText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//                Bundle bundle = new Bundle();
//                //send username to contact information fragment
//                bundle.putString("username",riderText.getText().toString());
//                UserContactInformationFragment userContactInformationFragment = new UserContactInformationFragment();
//                userContactInformationFragment.setArguments(bundle);
//                userContactInformationFragment.show(getFragmentManager(), "my fragment");
//                return true;
                Intent intent = new Intent(getContext(), UserProfileActivity.class);
                intent.putExtra("someUsername",riderText.getText().toString());
                startActivity(intent);
                return true;
            }
        });

        startText.setText(startAddress);
        endText.setText(endAddress);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // building the dialog
        return builder

                .setView(view)
                .setCustomTitle(titleView)
                .setNegativeButton("Cancel", null) // null -> does nothing
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // send ride to parent activity
                        listener.onAcceptPressed(ride);


                    }
                }).create();
    }

}
