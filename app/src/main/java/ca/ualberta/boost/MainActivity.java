package ca.ualberta.boost;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button signUpButton;
    private Button signInButton;

    Button test;

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    String currentUserId;

    //check if user is already signed in
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

        //get references to firestore
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        //test
        test = findViewById(R.id.button1);

        //initialize views
        editTextEmail = findViewById(R.id.sign_in_email);
        editTextPassword = findViewById(R.id.sign_in_password);



        //open SignUp activity when the sign_up_button is clicked by calling openSignUpActivity
        signUpButton = findViewById(R.id.sign_up_button);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSignUpActivity();
            }
        });

        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("values","works");
                signInUser();
            }
        });

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchRides();
            }
        });


    }

    //sign in
    private void signInUser() {
        if (authenticate()) {
            auth.signInWithEmailAndPassword(editTextEmail.getText().toString().trim(), editTextPassword.getText().toString().trim())
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(MainActivity.this, "Sign In Successful!", Toast.LENGTH_SHORT).show();
                            currentUserId = auth.getCurrentUser().getEmail().toString();
                            launchHome();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if(e.toString().matches("com.google.firebase.auth.FirebaseAuthInvalidCredentialsException: The password is invalid or the user does not have a password.")){
                                Toast.makeText(MainActivity.this, "The password is invalid or the user does not have a password", Toast.LENGTH_SHORT).show();
                            } else {
                            Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                            Log.i("error message", e.toString()); }
                        }
                    });
        }
    }

    //todo: this function should take a string that is either rider or driver and launch the respective homescreen
    private void launchHome(){
        Intent intent = new Intent(this, HomeActivityRider.class);
        startActivity(intent);
    }


//    method to open SignUp activity
    public void openSignUpActivity(){
        Intent intent = new Intent(this, SignUpActivity.class );
        startActivity(intent);
    }
    private boolean authenticate(){
        if(editTextEmail.getText().toString().matches("")){
            Toast.makeText(this, "Enter a Email", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(editTextPassword.getText().toString().matches("")){
            Toast.makeText(this, "Enter a password", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(editTextEmail.getText().toString().length() < 6){
            Toast.makeText(this, "password too short lol", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void launchRides() {
        Intent intent = new Intent(this, DisplayActiveRideRequestsActivity.class);
        startActivity(intent);
    }
}
