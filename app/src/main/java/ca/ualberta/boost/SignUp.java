package ca.ualberta.boost;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SignUp extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText signUpFirstName;
    private EditText signUpLastName;
    private EditText signUpUsername;
    private EditText signUpPassword;
    private EditText signUpConfirmPassword;
    private EditText signUpEmail;
    private EditText signUpPhoneNumber;


    private Button confirmSignUpButton;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);


        //change action bar
        getSupportActionBar().setTitle("Sign up");

        Spinner spinner = findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.userType, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        //Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //initialize EditText fields
        signUpFirstName = findViewById(R.id.sign_up_first_name);
        signUpLastName = findViewById(R.id.sign_up_last_name);
        signUpUsername = findViewById(R.id.sign_up_username);
        signUpPassword = findViewById(R.id.sign_up_password);
        signUpConfirmPassword = findViewById(R.id.sign_up_confirm_password);
        signUpEmail = findViewById(R.id.sign_up_email);
        signUpPhoneNumber = findViewById(R.id.sign_up_phone_number);

        confirmSignUpButton = findViewById(R.id.confirm_sign_up_button);
        confirmSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String firstName = signUpFirstName.getText().toString();
                final String lastName = signUpLastName.getText().toString();
                final String username = signUpUsername.getText().toString();
                final String password = signUpPassword.getText().toString();
                final String confirmPassword = signUpConfirmPassword.getText().toString();
                final String email = signUpEmail.getText().toString();
                final String phoneNumber = signUpPhoneNumber.getText().toString();

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            User user = new User( firstName, lastName, username, email, phoneNumber);
                            FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(!task.isSuccessful())
                                        Toast.makeText(getApplicationContext(),"signup unsuccessful: " + task.getException().getMessage(), //ADD THIS
                                                Toast.LENGTH_SHORT).show();

                                }


                            });

                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Login unsuccessful: " + task.getException().getMessage(), //ADD THIS
                                    Toast.LENGTH_SHORT).show();


                        }
                    }
                });
            }
        });
    }




    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
