package ca.ualberta.boost;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

//    private static final String TAG = "MainActivity";
//    private static final String USERNAME= "name";
//    private static final String EMAIL = "email";


    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button signUpButton;
    private Button signInButton;

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
                    });
        }
    }


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
}
