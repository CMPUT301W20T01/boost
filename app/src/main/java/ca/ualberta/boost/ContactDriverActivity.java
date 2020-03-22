package ca.ualberta.boost;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.security.PublicKey;

public class ContactDriverActivity extends AppCompatActivity {

    //declare button variables
    private Button emailDriver;
    private Button callDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_driver);

        //initialize buttons
        emailDriver = findViewById(R.id.email_driver_button);
        callDriver = findViewById(R.id.call_driver_button);

        emailDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSendEmailActivity();
            }
        });

        callDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCallDriverActivity();
            }
        });
    }

    /**
     * this method opens sendEmailActivity
     */
    private void openSendEmailActivity(){
        Intent intent = new Intent(this, SendEmailActivity.class);
        startActivity(intent);
        finish();

    }


    /**
     * this method opens CallDriverActivity
     */
    private void openCallDriverActivity(){
        Intent intent = new Intent(this, CallDriverActivity.class);
        startActivity(intent);
        finish();

    }


}

