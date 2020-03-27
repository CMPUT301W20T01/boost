package ca.ualberta.boost;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import ca.ualberta.boost.controllers.RideTracker;
import ca.ualberta.boost.models.ActiveUser;
import ca.ualberta.boost.models.Ride;
import ca.ualberta.boost.models.User;
import ca.ualberta.boost.stores.RideStore;
import ca.ualberta.boost.stores.UserStore;

import static com.firebase.ui.auth.AuthUI.TAG;

/**
 * DriverAcceptedRidePendingFragment defines a fragment after
 * driver accepted the request
 * waiting for rider to accept the ride offer
 * if yes, move driver to CurrentRide Activity
 * if no, move driver back to ViewRideRequest
 */
public class RiderAcceptedFragment extends DialogFragment {
    private RiderAcceptedFragment.OnFragmentInteractionListener listener;
    private Ride ride;
    private TextView driverText;
    Button positiveButton;

    RiderAcceptedFragment(Ride ride){
        this.ride = ride;
        new RideTracker(this.ride);
    }

    /**
     * Interface that enforces the implementing class to handle
     * what happens when the fragment's
     * positive button (accept) is pressed
     */
    public interface OnFragmentInteractionListener {
        void onRiderAcceptPressed(Ride newRide);
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        if (context instanceof RiderAcceptedFragment.OnFragmentInteractionListener){
            listener = (RiderAcceptedFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "must implement OnFragmentInteractionListener");
        }

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_rider_pending_driver_request, null);
        View titleView = LayoutInflater.from(getActivity()).inflate(R.layout.title_pending, null);

        driverText = view.findViewById(R.id.driverText);
        driverText.setText(ride.getDriverUsername());

        //WHEN DRIVER ACCEPTED, TEXTVIEW WILL SHOW UP DRIVER NAME
        //CLICK ON THE NAME WILL POP UP PROFILE INFO FRAGMENT
        //DOES NOT WORK???
        driverText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UserContactInformationFragment();
            }
        });

        //MAKING PENDING CONFIRMATION
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setCustomTitle(titleView)
                .setView(view)
                .setPositiveButton("Accept",null)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //CANCEL THE RIDE OFFER
                        //-set driver to null
                        //-set status to pending
                        User activeUser = ActiveUser.getUser();

                        // update ride in database
                        ride.cancel();
                        RideStore.saveRide(ride);
                        ActiveUser.cancelRide();

                        // set driver's current ride to this ride
                        //activeUser.setActiveRide(null);
                    }
                });
        //NEED TO IMPLEMENT CHANGE RIDE STATUS TO PENDING AGAIN;

        AlertDialog alert = builder.create();
        alert.setCanceledOnTouchOutside(false);

        return alert;
    }
}

