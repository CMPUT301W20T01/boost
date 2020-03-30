package ca.ualberta.boost;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import javax.annotation.Nonnull;

import ca.ualberta.boost.controllers.RideEventListener;
import ca.ualberta.boost.controllers.RideTracker;
import ca.ualberta.boost.models.ActiveUser;
import ca.ualberta.boost.models.Ride;
import ca.ualberta.boost.models.RideStatus;
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
public class DriverAcceptedFragment extends DialogFragment {
    private DriverAcceptedFragment.OnFragmentInteractionListener listener;
    private TextView riderText;
    private RideTracker rideTracker;
    private Context mContext;

    /**
     * Interface that enforces the implementing class to handle
     * what happens when the fragment's
     * positive button (accept) is pressed
     */
    public interface OnFragmentInteractionListener {
        void onAcceptPressed(Ride newRide);
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        if (context instanceof DriverAcceptedFragment.OnFragmentInteractionListener){
            listener = (DriverAcceptedFragment.OnFragmentInteractionListener) context;
            mContext = context;
        } else {
            throw new RuntimeException(context.toString() + "must implement OnFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_driver_pending_rider_request, null);

        Ride activeRide = ActiveUser.getCurrentRide();

        riderText = view.findViewById(R.id.riderText);
        riderText.setText(activeRide.getRiderUsername());

        riderText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(getContext(), UserProfileActivity.class);
                intent.putExtra("someUsername",riderText.getText().toString());
                startActivity(intent);
                return true;
            }
        });

        new RideTracker(activeRide).addListener(new RideEventListener() {
            @Override
            public void onStatusChange(@Nonnull Ride ride) {
                if (ride.getRideStatus() == RideStatus.RIDERACCEPTED) {
                    Log.i("rideListener","status changed to RIDERACCEPTED");
                    ActiveUser.setCurrentRide(ride);
                    Intent intent = new Intent(mContext, CurrentRideActivity.class);
                    mContext.startActivity(intent);

                } else if (ride.getRideStatus() == RideStatus.PENDING) {
                    Toast.makeText(mContext, "Ride offer rejected", Toast.LENGTH_LONG).show();

                    Ride currentRide = ActiveUser.getCurrentRide();
                    currentRide.cancel();
                    RideStore.saveRide(ride);
                    ActiveUser.cancelRide();

                    Intent intent = new Intent(mContext, ViewRideRequestsActivity.class);
                    startActivity(intent);
                }
                // TODO: Ride status is CANCELLED
            }

            @Override
            public void onLocationChanged() { }
        });

        Log.i("rideListener","called ride Listener for: " + activeRide.id());

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setView(view)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // update ride in database
                        Ride ride = ActiveUser.getCurrentRide();
                        ride.setDriverUsername(null);
                        ride.setPending();
                        RideStore.saveRide(ride);

                        ActiveUser.cancelRide();
                    }
                });
        //TODO:NEED TO IMPLEMENT CHANGE RIDE STATUS TO PENDING AGAIN;

        AlertDialog alert = builder.create();
        alert.setCanceledOnTouchOutside(false);

        return alert;
    }
}
