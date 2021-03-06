package ca.ualberta.boost;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import ca.ualberta.boost.controllers.ActiveUser;
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

    private TextView signUpButton;
    private Button signInButton;

    private ProgressBar circleProgressBar;

    private FirebaseAuth auth;

    private String username;
    private String pwd;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
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
                finish();
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
            }
        });

        //get and check to see if the value taken from the shared preference is null
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String test1 = preferences .getString("User","");
        if(test1.matches("")) { }
        else {
            circleProgressBar.setAlpha(1);
            username = preferences.getString("User","");
            pwd = preferences.getString("pwd","");
            loginEmailView.setText(username);
            loginPasswordView.setText(pwd);
            signInButton.setVisibility(View.INVISIBLE);
            signUpButton.setVisibility(View.INVISIBLE);
            auth.signInWithEmailAndPassword(username, pwd)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            String testEmail = preferences.getString("User","");
                            UserStore.getUserByEmail(testEmail).addOnSuccessListener(new OnSuccessListener<User>() {
                                @Override
                                public void onSuccess(User user) {
                                    ActiveUser.login(user);
                                    launchHome(user.getType());
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("newValue","failed");
                            Toast.makeText(MainActivity.this, "did not work", Toast.LENGTH_SHORT).show();
                            circleProgressBar.setAlpha(0);
                        }
                    });
        }
    }

    /**
     * Signs in the user and launches the appropriate main page
     */
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
                                    Log.d("Sign In", e.toString());
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

    /**
     * Launches the appropriate main page for the user
     * RiderMainPage if they are a rider, DriverMainPage if they are a driver
     * @param type
     *      UserType specifying if the user is a RIDER or a DRIVER
     */
    private void launchHome(UserType type) {
        username = loginEmailView.getText().toString();
        pwd = loginPasswordView.getText().toString();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("User",username);
        editor.putString("pwd",pwd);
        editor.apply();
        editor.commit();
        Intent intent;
        if (type == UserType.RIDER) {
            finish();
            intent = new Intent(this, RiderMainPage.class);
        } else { // type == UserType.DRIVER
            finish();
            intent = new Intent(this, DriverMainPage.class);
        }
        startActivity(intent);
    }

    /**
     * Opens the sign up activity
     * @see SignUpActivity
     */
    public void openSignUpActivity() {
        Intent intent = new Intent(this, SignUpActivity.class );
        startActivity(intent);
    }

    /**
     * Checks if the username and password entered byt the user are valid
     * @param username
     *      username entered
     * @param password
     *      password entered
     * @return
     *      boolean, true if input is valid, false otherwise
     */
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
}

