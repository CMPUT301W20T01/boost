package ca.ualberta.boost;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


/* Reference
         =>YouTube tutorial by 'Coding in Flow' on 'How to Make a Phone Call from Your App (+ Permission Request) - Android Studio Tutorial'
           URI to Video = https://www.youtube.com/watch?v=UDwj5j4tBYg
           URI to username = https://www.youtube.com/channel/UC_Fh8kvtkVPkeihBs42jGcA
*/

/**
 * CallActivity is responsible for handling phone calls between Rider and Driver
 */

public class CallActivity extends AppCompatActivity {
    private static final int REQUEST_CALL =1;
    private EditText editTextNumber;
    private String driverNumberFromProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        //initialize EditText and ImageView Buttons
        editTextNumber = findViewById(R.id.edit_text_number);

        //get driver's number from UserProfileActivity and set it as number to call
        driverNumberFromProfile = getIntent().getExtras().getString("call");
        editTextNumber.setText(driverNumberFromProfile);

        //go back to previous page once the back button is clicked
        ImageView backButton = findViewById(R.id.call_activity_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //call driver once the call button is clicked
        ImageView imageCall = findViewById(R.id.image_call);
        imageCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();
            }
        });

    }

    /**
     * this function starts the phone call
     */
    private void makePhoneCall(){
        String number = editTextNumber.getText().toString();

        //check if number is not null and remove empty spaces using trim()
        if(number.trim().length()>0){
            //check if user has the permission to make phone call
            //if permission is not granted, request permission
            if(ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CALL_PHONE ) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,
                        new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CALL);
             //if permission granted, make phone call
            }else{
                String dial = "tel:"+number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }

        }else {
            Toast.makeText(this,"Enter phone Number", Toast.LENGTH_SHORT).show();;
        }
    }

    /**
     * this method is a call back for the result from requesting permissions
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CALL){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                makePhoneCall();
            }else{
                Toast.makeText(this, "permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}