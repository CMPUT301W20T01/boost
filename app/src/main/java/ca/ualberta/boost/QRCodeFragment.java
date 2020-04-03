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
    private double fare;
    private ImageView QRCode;

    /**
     * QRCodeFragment constructor
     * @param fare
     *      amount owed to the driver by the rider
     */
    QRCodeFragment(double fare){
        this.fare = fare;
        Log.i("QRCodeFragment", Double.valueOf(fare).toString());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_qr_code, null);

        //generate QR
        QRCode =view.findViewById(R.id.qr_code);

        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        int QRsize = 800;

        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(Double.toString(fare), BarcodeFormat.QR_CODE, QRsize, QRsize);
            Bitmap bitmap = Bitmap.createBitmap(QRsize,QRsize, Bitmap.Config.RGB_565);

            for (int x = 0; x < QRsize; x++) {
                for (int y = 0; y < QRsize; y++) {
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
                .setView(view).create();
    }
}