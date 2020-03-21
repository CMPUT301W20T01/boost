package ca.ualberta.boost;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import ca.ualberta.boost.models.ActiveUser;
import ca.ualberta.boost.models.Driver;
import ca.ualberta.boost.models.Promise;
import ca.ualberta.boost.models.PromiseImpl;
import ca.ualberta.boost.models.Rider;
import ca.ualberta.boost.models.User;
import ca.ualberta.boost.stores.UserStore;

/**
 * SignUpActivity is responsible for signing up the user
 * on a successful registration the user is taken to the correct page depending on their role
 */

public class SignUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private FirebaseAuth auth;
    private EditText firstName;
    private EditText userName;
    private EditText email;
    private EditText phoneNumber;
    private EditText password;
    private Button signUpButton;
    private Spinner spinner;

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

        //Reference spinner
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this , R.array.userType, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        //Create and add user when button clicked
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser();
//                uniqueUserName(userName.getText().toString());
            }
        });
    }

    // adds user to database
    private void addUser() {
        if(authenticate()) {
            auth.createUserWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim())
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(SignUpActivity.this, "user account created", Toast.LENGTH_SHORT).show();
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

    //launches the home activity for a rider
    private void launchHomeRider(){
        Intent intent = new Intent(this, RiderMainPage.class);
        startActivity(intent);
    }

    //launches the home activity for a driver
    private void launchHomeDriver(){
        Intent intent = new Intent(this, DriverMainPage.class);
        startActivity(intent);
    }

    //stores user into database users
    private void storeUser() {
        User user;
        if (spinner.getSelectedItem().toString().equals("Rider")) {
            user = new Rider(firstName.getText().toString(), userName.getText().toString(),
                                   password.getText().toString(), email.getText().toString(),
                                   phoneNumber.getText().toString());
        } else {
            user = new Driver(firstName.getText().toString(), userName.getText().toString(),
                                     password.getText().toString(), email.getText().toString(),
                                     phoneNumber.getText().toString());
        }

        UserStore.saveUser(user);
    }

    //signs in user and launches the home activity
    private void signInUser () {
        //uniqueUserName(userName.getText().toString());
        if(authenticate()) {
            auth.signInWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim())
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                          //  ActiveUser.login(auth.getUid().get("username").toString(), ref.document(authResult.getUser().getUid()).get("password").toString());
                            if(spinner.getSelectedItem().toString().matches("Rider")) {
                                launchHomeRider();
                            } else {
                                launchHomeDriver();
                            }
                        }
                    });
        }
    }

    //Return true if fields have values and password is longer than 6 characters
    private boolean authenticate(){
        if(email.getText().toString().matches("")){
            Toast.makeText(this, "Enter a Email", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(password.getText().toString().matches("")){
            Toast.makeText(this, "Enter a password", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(email.getText().toString().length() < 6){
            Toast.makeText(this, "password too short lol", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    //spinner methods
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
       //String text = adapterView.getItemAtPosition(i).toString();
       //Toast.makeText(adapterView.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public Promise<Boolean> uniqueUsername(String username) {
        final PromiseImpl<Boolean> isUnique = new PromiseImpl<>();
        UserStore.getUser(username).addOnSuccessListener(new OnSuccessListener<User>() {
            @Override
            public void onSuccess(User user) {
                isUnique.resolve(false);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                isUnique.resolve(true);
            }
        });

        return isUnique;
    }
}