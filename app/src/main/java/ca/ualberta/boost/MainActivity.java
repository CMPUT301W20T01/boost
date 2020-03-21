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


import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import ca.ualberta.boost.models.ActiveUser;
import ca.ualberta.boost.models.Promise;
import ca.ualberta.boost.models.PromiseImpl;
import ca.ualberta.boost.models.User;
import ca.ualberta.boost.models.UserType;
import ca.ualberta.boost.stores.UserStore;

/**
 * MainActivity is responsible for signing in the user
 * by making sure the username and password correspond to user information in firestore
 * the class also will launch the sign up page if that button is clicked
 */

public class MainActivity extends AppCompatActivity {

    private EditText loginEmailView;
    private EditText loginPasswordView;

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
    public void onResume() {
        super.onResume();
        // put your code here...
        circleProgressBar.setAlpha(0);
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
        loginEmailView = findViewById(R.id.sign_in_email);
        loginPasswordView = findViewById(R.id.sign_in_password);

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
                signInUser();
                //launching a thread here
//                run();
            }
        });
    }

    //sign in as a Driver or a rider
    private void signInUser() {
        circleProgressBar.setAlpha(1);
        final String email = loginEmailView.getText().toString().trim();
        final String password = loginPasswordView.getText().toString().trim();
        if (isValidInput(email, password)) {
            auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            UserStore.getUserByEmail(email).addOnSuccessListener(new OnSuccessListener<User>() {
                                @Override
                                public void onSuccess(User user) {
                                    Toast.makeText(MainActivity.this, "Sign in successful!", Toast.LENGTH_SHORT).show();
                                    ActiveUser.login(user);
                                    launchHome(user.getType());
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity.this, "Incorrect username or password.", Toast.LENGTH_SHORT).show();
                                    circleProgressBar.setAlpha(0);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Sign In", e.toString());
                            Toast.makeText(MainActivity.this, "Incorrect username or password.", Toast.LENGTH_SHORT).show();
                            circleProgressBar.setAlpha(0);
                        }
                    });
        } else {
            circleProgressBar.setAlpha(0);
        }
    }

    //method to open Rider or Driver HomePage
    private void launchHome(UserType type) {
        Log.d("MainActivity", type.toString());
        Intent intent;
        if (type == UserType.RIDER) {
            intent = new Intent(this, RiderMainPage.class);
        } else { // type == UserType.DRIVER
            intent = new Intent(this, DriverMainPage.class);
        }
        startActivity(intent);
        finish();
    }

    //method to open SignUp activity
    public void openSignUpActivity() {
        Intent intent = new Intent(this, SignUpActivity.class );
        startActivity(intent);
    }

    //method to check if user to login has signed up already
    private boolean isValidInput(String username, String password) {
        if(username.isEmpty()) {
            Toast.makeText(this, "Username required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(password.isEmpty()) {
            Toast.makeText(this, "Password required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

//    @Override
//    public void run() {
////        android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_DISPLAY);
//        signInUser();
//    }
}

