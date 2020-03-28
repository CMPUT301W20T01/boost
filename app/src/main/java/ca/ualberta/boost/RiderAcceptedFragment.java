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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

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
public class RiderAcceptedFragment extends DialogFragment {
    private RiderAcceptedFragment.OnFragmentInteractionListener listener;
    private Ride ride;
    private TextView driverText;
    private Context mContext;
    Button positiveButton;
    private String driver;

    RiderAcceptedFragment(Ride ride){
        this.ride = ride;
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
            this.mContext = context;
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

        Log.d("rideListener","add listener");
        new RideTracker(ride).addListener(new RideEventListener() {
            @Override
            public void onStatusChange( Ride ride) {
                if (ride.getRideStatus()== RideStatus.RIDERACCEPTED){
                    //START INTENT
                    Log.i("rideListener","status changed to RIDERACCEPTED");
                    Intent intent = new Intent(mContext, CurrentRideActivity.class);

                    mContext.startActivity(intent);

                }

                if (ride.getRideStatus()==RideStatus.DRIVERACCEPTED){
                    Log.i("rideListener","status changed to DRIVERACCEPTED");

                    RideStore.getRide(ride.id()).addOnSuccessListener(new OnSuccessListener<Ride>() {
                        @Override
                        public void onSuccess(Ride ride) {
                            Log.i("rideListener","onSuccess RideStore in RiderFragment: "+ride.getDriverUsername());
                            driver = ride.getDriverUsername();
                        }

                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("rideListener","onFailure RideStore in RiderFragment"+e);

                        }
                    });


                    Log.i("rideListener","Driver acquired: "+driver);

                    driverText = getActivity().findViewById(R.id.driverText);
                    driverText.setText(driver);
                    Toast.makeText(mContext, "Driver: "+ride.getDriverUsername()+" has accepted.", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onLocationChanged() {

            }
        });


        //MAKING PENDING CONFIRMATION
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setCustomTitle(titleView)
                .setView(view)
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ride.riderAccept();

                        RideStore.saveRide(ride);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //CANCEL THE RIDE OFFER
                        // update ride in database
                        ride.cancel();
                        RideStore.saveRide(ride);
                        ActiveUser.cancelRide();

                    }
                });
        //NEED TO IMPLEMENT CHANGE RIDE STATUS TO PENDING AGAIN;

        AlertDialog alert = builder.create();
        alert.setCanceledOnTouchOutside(false);
        return alert;
    }
}

