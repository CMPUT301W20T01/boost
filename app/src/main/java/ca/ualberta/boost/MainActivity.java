package ca.ualberta.boost;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.firestore.FirebaseFirestore;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String USERNAME= "name";
    private static final String EMAIL = "email";

    private EditText editTextName;
    private EditText editTextEmail;


    private Button signUpButton;
    private Button signInButton;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextName = findViewById(R.id.sign_in_email);
        editTextName = findViewById(R.id.sign_in_password);



        //open SignUp activity when the sign_up_button is clicked by calling openSignUpActivity
        signUpButton = findViewById(R.id.sign_up_button);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               openSignUpActivity();
            }
        });

        signInButton = findViewById(R.id.sign_in_button);


    }

    //sign in
    public void saveNote(View v){
        String name = editTextName.getText().toString();

    }


    //method to open SignUp activity
    public void openSignUpActivity(){
        Intent intent = new Intent(this, SignUp.class );
        startActivity(intent);


    }

}
