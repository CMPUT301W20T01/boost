package ca.ualberta.boost;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.nfc.Tag;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
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
import ca.ualberta.boost.models.UserType;
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
public class RiderAcceptedFragment extends DialogFragment implements RideEventListener {
    private RiderAcceptedFragment.OnFragmentInteractionListener listener;
    private TextView driverText;
    private Ride ride;

    private TextView message;
    private Context mContext;
    Button positiveButton;
    private String driver;
    private ProgressBar progressBar;

    RiderAcceptedFragment(Ride ride){
        this.ride = ride;
    }
    RiderAcceptedFragment(){}

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

        driverText = view.findViewById(R.id.driverText);
        message = view.findViewById(R.id.riderDesc);
        progressBar = view.findViewById(R.id.request_progressbar);

        //MAKING PENDING CONFIRMATION
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setView(view)
                .setPositiveButton("Accept Driver", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("RiderAcceptedFragment", "Accept Driver Clicked");
                        Ride ride = ActiveUser.getCurrentRide();
                        ride.riderAccept();
                        RideStore.saveRide(ride);
                        Intent intent = new Intent(mContext, CurrentRideActivity.class);
                        mContext.startActivity(intent);
                    }
                })
                .setNeutralButton("Reject Driver", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("RiderAcceptedFragment", "Reject Driver Clicked");
                        Ride ride = ActiveUser.getCurrentRide();
                        ride.setPending();
                        ride.setDriverUsername("");
                        RideStore.saveRide(ride);
                        driverText.setText("");
                    }
                })
                .setNegativeButton("Cancel Request", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Ride ride = ActiveUser.getCurrentRide();
                        ride.cancel();
                        RideStore.saveRide(ride);
                        ActiveUser.cancelRide();
                    }
                });

        AlertDialog alert = builder.create();

        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button neutButton = ((AlertDialog ) dialog).getButton(AlertDialog.BUTTON_NEUTRAL);
                neutButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("RiderAcceptedFragment", "Reject Driver Clicked");
                        Ride ride = ActiveUser.getCurrentRide();
                        ride.setPending();
                        RideStore.saveRide(ride);
                        driverText.setText("");
                    }
                });

            }
        });

        return alert;
    }

    @Override
    public void onStart() {
        super.onStart();
        Ride currentRide = ActiveUser.getCurrentRide();
        new RideTracker(currentRide).addListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        AlertDialog dialog = (AlertDialog) getDialog();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setVisibility(View.GONE);
        dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setVisibility(View.GONE);
        dialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void onStatusChange(@NonNull Ride ride) {
        if (ride.getRideStatus() == RideStatus.RIDERACCEPTED) {
            Log.d("RideAcceptedFragment","status changed to RIDERACCEPTED");
            Intent intent = new Intent(mContext, CurrentRideActivity.class);
            mContext.startActivity(intent);
        }

        if (ride.getRideStatus() == RideStatus.PENDING) {
            Log.d("RideAcceptedFragment", "status changed to PENDING");
            AlertDialog dialog = (AlertDialog) getDialog();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setVisibility(View.GONE);
            dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setVisibility(View.GONE);
            driverText.setText("");
            message.setText("Waiting for a driver...");
            progressBar.setVisibility(View.VISIBLE);
        }

        if (ride.getRideStatus() == RideStatus.DRIVERACCEPTED) {
            Log.d("RideAcceptedFragment","status changed to DRIVERACCEPTED");
            AlertDialog dialog = (AlertDialog) getDialog();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setVisibility(View.VISIBLE);
            dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setVisibility(View.VISIBLE);
            message.setText("Driver found: ");
            progressBar.setVisibility(View.GONE);

            RideStore.getRide(ride.id()).addOnSuccessListener(new OnSuccessListener<Ride>() {
                @Override
                public void onSuccess(Ride ride) {
                    Log.d("RideAcceptedFragment","onSuccess RideStore get driver:" + ride.getDriverUsername());
                    driver = ride.getDriverUsername();

                    Log.d("RideAcceptedFragment","got driver:" + driver);

                    if (ride.getRiderUsername() != null) {
                        Toast.makeText(getContext(), driver + " has accepted your ride request", Toast.LENGTH_SHORT).show();

                        driverText.setText(driver);

                        driverText.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                Intent intent = new Intent(getContext(), UserProfileActivity.class);
                                intent.putExtra("username", driverText.getText().toString());
                                startActivity(intent);
                                return true;
                            }
                        });
                    }
                }
            });
        }
    }

    @Override
    public void onLocationChanged() {

    }
}

