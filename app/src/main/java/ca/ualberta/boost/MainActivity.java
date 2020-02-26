package ca.ualberta.boost;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.firestore.FirebaseFirestore;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity{

    private EditText email;
    private EditText password;

    private Button signUpButton;
    private Button signInButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.sign_in_email);
        password = findViewById(R.id.sign_in_password);



        //open SignUp activity when the sign_up_button is clicked by calling openSignUpActivity
        signUpButton = findViewById(R.id.sign_up_button);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               openSignUpActivity();
            }
        });

        //click signIn to go to user homepage
        signInButton = findViewById(R.id.sign_in_button);


    }

    //method to open SignUp activity
    public void openSignUpActivity(){
        Intent intent = new Intent(this, SignUp.class );
        startActivity(intent);


    }

}
