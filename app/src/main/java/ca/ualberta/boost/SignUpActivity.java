package ca.ualberta.boost;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import ca.ualberta.boost.controllers.ActiveUser;
import ca.ualberta.boost.models.Driver;
import ca.ualberta.boost.models.Rider;
import ca.ualberta.boost.models.User;
import ca.ualberta.boost.stores.UserStore;

/**
 * SignUpActivity is responsible for signing up the user
 * on a successful registration the user is taken to the correct page depending on their role
 */

public class SignUpActivity extends AppCompatActivity{

    private FirebaseAuth auth;
    private EditText firstName;
    private EditText userName;
    private EditText email;
    private EditText phoneNumber;
    private EditText password;
    private Button signUpButton;
    private Switch typeSwitch;
    private TextView switchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);


        //get references to fireStore
        auth = FirebaseAuth.getInstance();

        //get reference to EditTexts
        firstName = findViewById(R.id.sign_up_first_name);
        userName = findViewById(R.id.sign_up_username);
        email = findViewById(R.id.sign_up_email);
        phoneNumber = findViewById(R.id.sign_up_phone_number);
        password = findViewById(R.id.sign_up_password);

        signUpButton = findViewById(R.id.confirm_sign_up_button);

        //Reference switch
        switchText = findViewById(R.id.switch_text);
        typeSwitch = findViewById(R.id.type_switch);
        typeSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switchText.setText("Driver");
                } else {
                    switchText.setText("Rider");
                }
            }
        });

        //Create and add user when button clicked
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uniqueUsername(userName.getText().toString());
            }
        });
    }

    /**
     * Adds the user signing up to the database after checking if their
     * input is valid
     */
    private void addUser() {
        if(isValidInput()) {
            auth.createUserWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim())
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(SignUpActivity.this, "User account created", Toast.LENGTH_SHORT).show();
                            finish();
                            storeUser();
                            signInUser();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignUpActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    /**
     * Launches the main page for a rider
     */
    private void launchHomeRider(){
        Intent intent = new Intent(this, RiderMainPage.class);
        startActivity(intent);
    }

    /**
     * Launches the main page for a driver
     */
    private void launchHomeDriver(){
        Intent intent = new Intent(this, DriverMainPage.class);
        startActivity(intent);
    }

    /**
     * Stores the user to teh database
     */
    private void storeUser() {
        User user;

        if (typeSwitch.isChecked()) {
            //driver
            user = new Driver(firstName.getText().toString(), userName.getText().toString(),
                         password.getText().toString(), email.getText().toString(),
                         phoneNumber.getText().toString());
        } else {
            //rider
            user = new Rider(firstName.getText().toString(), userName.getText().toString(),
                       password.getText().toString(), email.getText().toString(),
                       phoneNumber.getText().toString());
        }
        UserStore.saveUser(user);
        ActiveUser.login(user);
    }

    /**
     * Signs in the user and launches the appropriate main page
     */
    private void signInUser () {
        if (isValidInput()) {
            auth.signInWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim())
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            if (typeSwitch.isChecked()) {
                                finish();
                                launchHomeDriver();
                            } else {
                                finish();
                                launchHomeRider();
                            }
                        }
                    });
        }
    }

    /**
     * Checks if the information entered is valid
     * @return
     *      true if all input is valid, false otherwise
     */
    private boolean isValidInput() {
        if(firstName.getText().toString().matches("")){
            Toast.makeText(this, "Enter a First Name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(userName.getText().toString().matches("")){
            Toast.makeText(this, "Enter a username", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(email.getText().toString().matches("")){
            Toast.makeText(this, "Enter a Email", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(phoneNumber.getText().toString().matches("")){
            Toast.makeText(this, "Enter a phone number", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(password.getText().toString().matches("")){
            Toast.makeText(this, "Enter a password", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(email.getText().toString().length() < 6){
            Toast.makeText(this, "Password must be 6 characters or longer", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Tells the user if the username entered is already taken
     * @param username
     *      username the user has entered, to be checked
     */
    public void uniqueUsername(String username) {
        if (!username.equals("")) {
            UserStore.getUser(username).addOnSuccessListener(new OnSuccessListener<User>() {
                @Override
                public void onSuccess(User user) {
                    Toast.makeText(getApplicationContext(), "Username is taken", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    addUser();
                }
            });
        }
    }
}