package ca.ualberta.boost;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

/**RETRIEVE USER PROFILE AND DISPLAY IT
 * EDIT PROFILE TO FIREBASE IF REQUIRED
 */
public class PrivateUserProfileActivity extends AppCompatActivity implements EditUserProfileFragment.OnFragmentInteractionListener {
    FirebaseAuth auth;
    FirebaseUser currentUser;
    private FirebaseFirestore db;
    private CollectionReference collection;
    DocumentReference documentReference;

    TextView userName;
    TextView userEmail;
    TextView userPhoneNum;
    Button editButton;
    Button updatePassword;
    Button updateUserInformation;
    TextView userRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_private);

        //Initialize
        userName = findViewById(R.id.userProfilePrivateName);
        userEmail = findViewById(R.id.userProfilePrivateEmail);
        userPhoneNum = findViewById(R.id.userProfilePrivatePhone);
        editButton = findViewById(R.id.userProfilePrivateButton);
        updatePassword = findViewById(R.id.button);
        updateUserInformation = findViewById(R.id.button2);
        userRating = findViewById(R.id.userProfilePrivateRating);


        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        collection = db.collection("users");

        //retrieve current User profile info
        collection.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document: task.getResult()){
                                    if((auth.getUid().matches(document.get("id").toString()))){
                                    userName.setText(document.get("Name").toString());
                                    userEmail.setText(document.get("Email").toString());
                                    userPhoneNum.setText(document.get("Phone").toString());
                                        /**
                                         *
                                         * ONLY UNCOMMENT THE FOLLOWING LINES IF YOU ARE USING A KNOWN USER PROFILE WITH THE
                                         * RatingUp AND Rating Down FIELDS IN THE DATABASE OR ELSE THE CODE WILL BREAK.
                                         */
//                                    userRating.setText(
//                                         document.get("RatingUp").toString()+"\uD83D\uDC4D    "
//                                         +document.get("RatingDown").toString()+"\uD83D\uDC4E");
                                }
                            }
                        }

                    }
                });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new EditUserProfileFragment().show(getSupportFragmentManager(),"Edit User Contact Info");
            }
        });
        updatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchUpdatePassword();
            }
        });

        updateUserInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchUpdateInformation();
            }
        });
    }

    @Override
    public void onOkPressedEdit(String newEmail, String newPhone) {

        userEmail = findViewById(R.id.userProfilePrivateEmail);
        userPhoneNum = findViewById(R.id.userProfilePrivatePhone);

        //UPDATE TEXTVIEW
        userEmail.setText(newEmail);
        userPhoneNum.setText(newPhone);

        //UPDATE FIREBASE
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        collection = db.collection("users");
        // Update an existing document
        //NOT COMPLETED
        DocumentReference docRef = collection.document(auth.getUid());
        docRef.update("Email", newEmail, "Phone", newPhone);
        auth.getCurrentUser().updateEmail(newEmail);


        // (async) Update one field

    }

    private void launchEditProfile(){
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivity(intent);
    }

    private void launchUpdatePassword(){
        Intent intent = new Intent(this, UpdatePasswordActivity.class);
        startActivity(intent);
    }

    private void launchUpdateInformation(){
        Intent intent = new Intent(this, UpdateProfileInfo.class);
        startActivity(intent);
    }


}
