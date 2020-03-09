package ca.ualberta.boost;

import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements Runnable {

    private EditText loginEmail;
    private EditText loginPassword;

    private Button signUpButton;
    private Button signInButton;

    private ProgressBar circleProgressBar;

    private FirebaseAuth auth;

    //Stores User's generated ID from firebase
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

        circleProgressBar = findViewById(R.id.progressBar);
        circleProgressBar.setAlpha(0);

        //get references to fireStore
        auth = FirebaseAuth.getInstance();

        //initialize EditText views
        loginEmail = findViewById(R.id.sign_in_email);
        loginPassword = findViewById(R.id.sign_in_password);

        //open SignUp activity when the sign_up_button is clicked by calling openSignUpActivity
        signUpButton = findViewById(R.id.sign_up_button);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSignUpActivity();
            }
        });

        //Call signInUser when signInButton is clicked
        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("values","works");
//                signInUser();
                //launching a thread here
                run();
            }
        });
    }

    //sign in as a Driver or a rider
    private void signInUser() {
        circleProgressBar.setAlpha(1);
        if (authenticate()) {
            auth.signInWithEmailAndPassword(loginEmail.getText().toString().trim(), loginPassword.getText().toString().trim())
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(MainActivity.this, "Sign In Successful!", Toast.LENGTH_SHORT).show();
                            currentUserId = auth.getCurrentUser().getEmail().toString();
                            //function to check if user that just signed in is a driver or rider respectively
                            launchHome();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            circleProgressBar.setAlpha(0);
                        }
                    });
        } else{
            circleProgressBar.setAlpha(0);
        }
    }

    //method to open Rider or Driver HomePage
    private void launchHome(){
        Intent intent = new Intent(this, RiderMainPage.class);
        startActivity(intent);
//        circleProgressBar.setAlpha(0);
    }


    //method to open SignUp activity
    public void openSignUpActivity(){
        Intent intent = new Intent(this, SignUpActivity.class );
        startActivity(intent);
    }

    //method to check if user to login has signed up already
    private boolean authenticate(){
        if(loginEmail.getText().toString().matches("")){
            Toast.makeText(this, "Enter a Email", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(loginPassword.getText().toString().matches("")){
            Toast.makeText(this, "Enter a password", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(loginEmail.getText().toString().length() < 6){
            Toast.makeText(this, "password too short lol", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void run(){
        android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_DISPLAY);
        signInUser();
    }
}
