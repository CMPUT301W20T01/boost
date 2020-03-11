package ca.ualberta.boost;

/* This class class is used to sign in user into the app
* //todo: ask alex for citation */

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Views
    private EditText loginEmail;
    private EditText loginPassword;
    private Button signUpButton;
    private Button signInButton;

    // Declare firebase Auth variable
    private FirebaseAuth auth;

    //Stores User's generated ID from firebase
    private String currentUserId;

    /**
     *  Check if user is signed in using currentUserId
     */
    @Override
    protected void onStart() {
        super.onStart();
        if (currentUserId == null) {
            Toast.makeText(this, "Not signed In", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    /**
     *  Initialize listeners
     *  initialize views
     *  openSignUpActivity when signUpButton is clicked
     *  signInUser when signInButton is clicked
     */
    private void init(){

        // Initialize firebase Auth
        auth = FirebaseAuth.getInstance();

        //initialize views
        loginEmail = findViewById(R.id.sign_in_email);
        loginPassword = findViewById(R.id.sign_in_password);
        signUpButton = findViewById(R.id.sign_up_button);
        signInButton = findViewById(R.id.sign_in_button);

        //open SignUp activity when the sign_up_button is clicked by calling openSignUpActivity
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSignUpActivity();
            }
        });

        //Call signInUser when signInButton is clicked
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.i("values","works");
                signInUser();
            }
        });

    }

    /**
     *  opens a new SignUpActivity
     */
    public void openSignUpActivity(){
        Intent intent = new Intent(this, SignUpActivity.class );
        startActivity(intent);
    }

    /**
     * Sign in user with Email and password
     * launchHome for Driver or Rider
     */
    private void signInUser() {
        if (authenticate()) {
            auth.signInWithEmailAndPassword(loginEmail.getText().toString().trim(), loginPassword.getText().toString().trim())
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(MainActivity.this, "Sign In Successful!", Toast.LENGTH_SHORT).show();
                            FirebaseUser currentUser = auth.getCurrentUser();

                            //todo: ask how to get the userType and launch different main pages for either of them
                            launchHome();
                        }
                    });
        }
    }

    /**
     * launch RiderMainPage or DriverMainPage based on userType
     */
    //todo: should take in a parameter userType
    private void launchHome(){
        Intent intent = new Intent(this, RiderMainPage.class);
        startActivity(intent);
    }


    /**
     * Authenticate user using Email and password
     */
    private boolean authenticate(){
        if(loginEmail.getText().toString().matches("")){
            Toast.makeText(this, "Enter a Email", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(loginPassword.getText().toString().matches("")){
            Toast.makeText(this, "Enter a password", Toast.LENGTH_SHORT).show();
            return false;
        }
        // password should not be less than 6 characters
        if(loginEmail.getText().toString().length() < 6){
            Toast.makeText(this, "password has to be 6 character or more", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
