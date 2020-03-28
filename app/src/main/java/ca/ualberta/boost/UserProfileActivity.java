package ca.ualberta.boost;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

    //firebase
//    FirebaseUser currentUser;
    User user1;
    String test;

    TextView userName;
    TextView userFirstName;
    TextView userEmail;
    TextView userPhoneNum;
    Button editButton;
    TextView userRating;
    TextView userPassword;
    TextView password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_private);

        //Initialize
        userName = findViewById(R.id.userProfileUserName);
        userFirstName = findViewById(R.id.userProfileFirstName);
        userPassword = findViewById(R.id.userProfilePassword);
        userEmail = findViewById(R.id.userProfilePrivateEmail);
        userPhoneNum = findViewById(R.id.userProfilePrivatePhone);
        editButton = findViewById(R.id.userProfilePrivateButton);
        userRating = findViewById(R.id.userProfilePrivateRating);
        password = findViewById(R.id.userProfilePrivatePasswordText);



        //get the username from the fragment
//        try {
//            Intent i = getIntent();
//            test = i.getStringExtra("someUsername");
//            Log.i("alex", test);
//        } catch(Exception e){
//            Log.i("testValue",e.toString());
//        }

        Intent i = getIntent();
        if(i.getStringExtra("someUsername")==null){
            user1 = ActiveUser.getUser();
            userName.setText(user1.getUsername());
            userFirstName.setText(user1.getFirstName());
            userEmail.setText(user1.getEmail());
            userPhoneNum.setText(user1.getPhoneNumber());
            userPassword.setText(user1.getPassword());

        } else {
            test = i.getStringExtra("someUsername");
            UserStore.getUser(test)
                    .addOnSuccessListener(new OnSuccessListener<User>() {
                        @Override
                        public void onSuccess(User user) {
                            user1 = user;
                            userName.setText(user1.getUsername());
                            userFirstName.setText(user1.getFirstName());
                            userEmail.setText(user1.getEmail());
                            userPhoneNum.setText(user1.getPhoneNumber());
//                            userRating.setText(user1.);
                            userPassword.setVisibility(View.INVISIBLE);
                            password.setVisibility(View.INVISIBLE);
                            editButton.setVisibility(View.INVISIBLE);
//                            userPassword.setText(user1.getPassword());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UserProfileActivity.this, "didn't work", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

//        Log.i("alex",test);

//        if(test.matches("")){
//            user1 = ActiveUser.getUser();
//            userName.setText(user1.getUsername());
//            userFirstName.setText(user1.getFirstName());
//            userEmail.setText(user1.getEmail());
//            userPhoneNum.setText(user1.getPhoneNumber());
//            userPassword.setText(user1.getPassword());
//        } else {
//            UserStore.getUser(test)
//                    .addOnSuccessListener(new OnSuccessListener<User>() {
//                        @Override
//                        public void onSuccess(User user) {
//                            user1 = user;
//                            userName.setText(user1.getUsername());
//                            userFirstName.setText(user1.getFirstName());
//                            userEmail.setText(user1.getEmail());
//                            userPhoneNum.setText(user1.getPhoneNumber());
//                            userPassword.setText(user1.getPassword());
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(UserProfileActivity.this, "didn't work", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//        }


       // currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        user = ActiveUser.getUser();

        //retrieve current User profile info
//        userName.setText(user1.getUsername());
//        userFirstName.setText(user1.getFirstName());
//        userEmail.setText(user1.getEmail());
//        userPhoneNum.setText(user1.getPhoneNumber());
//        userPassword.setText(user1.getPassword());

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new EditUserProfileFragment().show(getSupportFragmentManager(),"Edit User Contact Info");
            }
        });

       userEmail.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String email = userEmail.getText().toString();
               Intent intent = new Intent(UserProfileActivity.this, EmailActivity.class);
               intent.putExtra("to", email);
               startActivity(intent);
           }
       });

       userPhoneNum.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String phone = userPhoneNum.getText().toString();
               Intent intent = new Intent(UserProfileActivity.this, CallActivity.class);
               intent.putExtra("call", phone);
               startActivity(intent);
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
        user1.setEmail(newEmail);
        user1.setFirstName(newName);
        user1.setPhoneNumber(newPhone);
        UserStore.saveUser(user1);

    }

}
