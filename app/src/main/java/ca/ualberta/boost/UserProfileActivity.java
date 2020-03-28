package ca.ualberta.boost;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.WriteResult;

import ca.ualberta.boost.models.User;
import ca.ualberta.boost.models.ActiveUser;
import ca.ualberta.boost.stores.UserStore;

/**RETRIEVE USER PROFILE AND DISPLAY IT
 * EDIT PROFILE TO FIREBASE IF REQUIRED
 */
public class UserProfileActivity extends AppCompatActivity implements EditUserProfileFragment.OnFragmentInteractionListener {

//    FirebaseUser currentUser;
    User user;
    TextView userName;
    TextView userFirstName;
    TextView userEmail;
    TextView userPhoneNum;
    Button editButton;
    Button homeButton;
    ImageView emailIcon;
    ImageView callIcon;
    TextView userRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_private);

        //Initialize
        userName = findViewById(R.id.userProfileUserName);
        userFirstName = findViewById(R.id.userProfileFirstName);
        userEmail = findViewById(R.id.userProfilePrivateEmail);
        userPhoneNum = findViewById(R.id.userProfilePrivatePhone);
        editButton = findViewById(R.id.userProfilePrivateButton);
        userRating = findViewById(R.id.userProfilePrivateRating);
        homeButton = findViewById(R.id.go_home_button);
        emailIcon = findViewById(R.id.email_icon_private);
        callIcon = findViewById(R.id.call_icon_private);

        // currentUser = FirebaseAuth.getInstance().getCurrentUser();
        user = ActiveUser.getUser();

        //retrieve current User profile info
        userName.setText(user.getUsername());
        userFirstName.setText(user.getFirstName());
        userEmail.setText(user.getEmail());
        userPhoneNum.setText(user.getPhoneNumber());

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new EditUserProfileFragment().show(getSupportFragmentManager(),"Edit User Contact Info");
            }
        });

       userEmail.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               openEmailActivity();
           }
       });

       userPhoneNum.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
              openCallActivity();
           }
       });

       emailIcon.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               openEmailActivity();
           }
       });

       callIcon.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               openCallActivity();
           }
       });
    }

    @Override
    public void onOkPressedEdit(String newEmail, String newPhone, String newName) {

        //UPDATE TEXTVIEW
        userEmail.setText(newEmail);
        userPhoneNum.setText(newPhone);
        userFirstName.setText(newName);


        //UPDATE FIREBASE
        user.setEmail(newEmail);
        user.setFirstName(newName);
        user.setPhoneNumber(newPhone);
        UserStore.saveUser(user);

    }

    public void openEmailActivity(){
        String email = userEmail.getText().toString();
        Intent intent = new Intent(UserProfileActivity.this, EmailActivity.class);
        intent.putExtra("to", email);
        startActivity(intent);
    }

    public void openCallActivity(){
        String phone = userPhoneNum.getText().toString();
        Intent intent = new Intent(UserProfileActivity.this, CallActivity.class);
        intent.putExtra("call", phone);
        startActivity(intent);

    }

}


