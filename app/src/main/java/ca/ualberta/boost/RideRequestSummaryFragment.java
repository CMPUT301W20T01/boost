package ca.ualberta.boost;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class RideRequestSummaryFragment extends DialogFragment{
    private OnFragmentInteractionListener listener;
    private Ride ride;
    private TextView fromText;
    private TextView toText;
    private TextView costText;
    private TextView durationText;


    RideRequestSummaryFragment(Ride ride){
        this.ride = ride;
    }

    public interface OnFragmentInteractionListener {
        void onOkPressed();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener){
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "must implement OnFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_ride_request_summary, null);
        fromText = view.findViewById(R.id.fromText);
        toText = view.findViewById(R.id.toText);
        costText = view.findViewById(R.id.costText);
        durationText = view.findViewById(R.id.durationText);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        // building the dialog
        return builder
                .setView(view)
                .setTitle("Ride Summary")
                .setNegativeButton("Cancel", null) // null -> does nothing
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fromText.setText(ride.getStartLocation().toString());
                        toText.setText(ride.getEndLocation().toString());
                        costText.setText(Float.toString(ride.getFare()));

                        listener.onOkPressed();


                    }
                }).create();
    }
}