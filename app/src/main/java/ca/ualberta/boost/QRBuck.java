package ca.ualberta.boost;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

//generate QR code
public class QRBuck {
    private int money; //total money a User has

    QRBuck(int money){
        this.money = money;
    }

    public Integer getMoney(){
        return this.money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public boolean checkBalanace(int request_value){ //check to see if request money is smaller than what user has
        return request_value < getMoney();
    }

    //generate QR code
    //assuming money_request is within sufficient amount
    //need to check before generate QR Code
    @Nullable
    public Bitmap generateQRCode (String request_value) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Bitmap QRcode = null;
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(request_value.toString(), BarcodeFormat.QR_CODE, 200, 200);
            QRcode = Bitmap.createBitmap(200, 200, Bitmap.Config.RGB_565);

            for (int x = 0; x < 200; x++) {
                for (int y = 0; y < 200; y++) {
                    QRcode.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }

            return QRcode;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return QRcode;
    }
}