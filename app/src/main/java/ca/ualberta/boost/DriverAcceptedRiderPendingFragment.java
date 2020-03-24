package ca.ualberta.boost;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import ca.ualberta.boost.models.Ride;

/**
 * DriverAcceptedRidePendingFragment defines a fragment after
 * driver accepted the request
 * waiting for rider to accept the ride offer
 * if yes, move driver to CurrentRide Activity
 * if no, move driver back to ViewRideRequest
 */
public class DriverAcceptedRiderPendingFragment extends DialogFragment {
    private RequestDetailsFragment.OnFragmentInteractionListener listener;
    private Ride ride;
    private TextView riderText;

    DriverAcceptedRiderPendingFragment(Ride ride){
        this.ride = ride;
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
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_driver_pending_rider_request, null);
        View titleView = LayoutInflater.from(getActivity()).inflate(R.layout.title_pending, null);

        riderText = view.findViewById(R.id.riderText);
        riderText.setText(ride.getRiderUsername());
        //MAKING PENDING CONFIRMATION

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // building the dialog
        return builder
                .setView(view)
                .setCustomTitle(titleView)
                .setNegativeButton("Cancel", null).create();
    }
}
