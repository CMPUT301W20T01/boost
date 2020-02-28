package ca.ualberta.boost;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

//implementing onclicklistener
public class QRActivity extends AppCompatActivity implements View.OnClickListener {

    //View Objects
    private Button buttonScan;
    private TextView textViewName, textViewAddress;

    //qr code scanner object
    private IntentIntegrator qrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_generator);

        //View objects
        buttonScan = (Button) findViewById(R.id.button2);
        textViewName = (TextView) findViewById(R.id.scan_result);
//        textViewAddress = (TextView) findViewById(R.id.textViewAddress);

        //intializing scan object
        qrScan = new IntentIntegrator(this);

        //attaching onclick listener
        buttonScan.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);


        if (scanningResult != null) {
            //we have a result
            String scanContent = scanningResult.getContents();
            textViewName.setText(scanContent);
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
//        if (requestCode == 0) {
//
//            if (resultCode == RESULT_OK) {
//                String contents = data.getStringExtra("SCAN_RESULT");
//                scan_result.setText(contents);
//            }
//            if (resultCode == RESULT_CANCELED) {
//                //handle cancel
//            }
//        }
//    }
    @Override
    public void onClick(View view) {
        //initiating the qr code scan
        qrScan.initiateScan();
    }
}





//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.zxing.integration.android.IntentIntegrator;
//import com.google.zxing.integration.android.IntentResult;
//
////NEED TO IMPLEMENT WHEN A USER HAS PRE-DEFINED AMT OF MONEY
////GENERATE A QR CODE (done)
////SCAN QR CODE (in progress)
////EXTRACT INFO FROM SCAN QR CODE TO USER MONEY
//
//public class QRActivity extends AppCompatActivity {
//    private EditText input;
//    private ImageView qr;
//    private Button button1;
//    private Button button2;
//    private TextView scan_result;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.qr_generator);
//        input = findViewById(R.id.input);
//        qr = findViewById(R.id.qr);
//        button1 = findViewById(R.id.button1);
//        button2 = findViewById(R.id.button2);
//
//        int total_val = 100;
//        final QRBuck money = new QRBuck(total_val,this);
//
//
//        button1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String request_val = input.getText().toString();
//                qr.setImageBitmap(money.generateQRCode(request_val));
//
//            }
//        });
//
//        button2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                money.ScanQR();
//            }
//        });
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//
//
//        if (scanningResult != null) {
//            //we have a result
//            String scanContent = scanningResult.getContents();
//            scan_result.setText(scanContent);
//        } else {
//            Toast toast = Toast.makeText(getApplicationContext(), "No scan data received!", Toast.LENGTH_SHORT);
//            toast.show();
//        }
//    }
////        if (requestCode == 0) {
////
////            if (resultCode == RESULT_OK) {
////                String contents = data.getStringExtra("SCAN_RESULT");
////                scan_result.setText(contents);
////            }
////            if (resultCode == RESULT_CANCELED) {
////                //handle cancel
////            }
////        }
////    }
//
//    public void displayInsufficientError(){
//        Toast.makeText(this, "Insufficient Amount of Money", Toast.LENGTH_SHORT).show();
//    }
//}
//
//
