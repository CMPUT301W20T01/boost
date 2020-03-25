package ca.ualberta.boost;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import ca.ualberta.boost.models.Ride;

/**
 * QRCodeFragment defines a fragment after
 * QR Code with the fare amount for driver to scan
 */
public class QRCodeFragment extends DialogFragment {
    private RequestDetailsFragment.OnFragmentInteractionListener listener;
    private double fare;
    private ImageView QRCode;

    QRCodeFragment(double fare){
        this.fare = fare;
        Log.i("RESULT","FRAGMENT QR");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_qr_code, null);
        View titleView = LayoutInflater.from(getActivity()).inflate(R.layout.title_qr, null);
        Log.i("RESULT","FRAGMENT QR");

        //generate QR
        QRCode =view.findViewById(R.id.qr_code);

        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(Double.toString(fare), BarcodeFormat.QR_CODE, 200, 200);
            Bitmap bitmap = Bitmap.createBitmap(200,200, Bitmap.Config.RGB_565);

            for (int x = 0; x < 200; x++) {
                for (int y = 0; y < 200; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }

            QRCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // building the dialog
        return builder
                .setView(view)
                .setCustomTitle(titleView).create();
    }
}