package ca.ualberta.boost;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

//NEED TO IMPLEMENT WHEN A USER HAS PRE-DEFINED AMT OF MONEY
//GENERATE A QR CODE (done)
//SCAN QR CODE (in progress)
//EXTRACT INFO FROM SCAN QR CODE TO USER MONEY

public class MainActivity extends AppCompatActivity {
    private EditText input;
    private ImageView qr;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        input = findViewById(R.id.input);
        qr = findViewById(R.id.qr);
        button = findViewById(R.id.button);

        int total_val = 100;
        final QRBuck money = new QRBuck(total_val);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String request_val = input.getText().toString();
                qr.setImageBitmap(money.generateQRCode(request_val));

            }
        });


    }

    public void displayInsufficientError() {
        Toast.makeText(this, "Insufficient Amount of Money", Toast.LENGTH_SHORT).show();
    }
}
