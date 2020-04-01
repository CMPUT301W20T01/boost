package ca.ualberta.boost;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import ca.ualberta.boost.models.Driver;
import ca.ualberta.boost.models.User;
import ca.ualberta.boost.models.ActiveUser;
import ca.ualberta.boost.models.UserType;
import ca.ualberta.boost.stores.UserStore;

/**RETRIEVE USER PROFILE AND DISPLAY IT
 * EDIT PROFILE TO FIREBASE IF REQUIRED
 */
public class UserProfileActivity extends AppCompatActivity implements EditUserProfileFragment.OnFragmentInteractionListener {

    TextView username;
    TextView userFirstName;
    TextView userEmail;
    TextView userPhoneNumber;
    ImageButton editButton;
    ImageButton backButton;
    ImageView thumbsUpIcon;
    ImageView thumbsDownIcon;
    ImageView emailIcon;
    ImageView callIcon;
    TextView userUpRating;
    TextView userDownRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        //Initialize
        username = findViewById(R.id.profile_username);
        userFirstName = findViewById(R.id.profile_first_name);
        userEmail = findViewById(R.id.profile_email);
        userPhoneNumber = findViewById(R.id.profile_phone_number);
        editButton = findViewById(R.id.edit_button);
        userUpRating = findViewById(R.id.thumbs_up_text);
        userDownRating = findViewById(R.id.thumbs_down_text);
        thumbsDownIcon = findViewById(R.id.thumbs_down_image);
        thumbsUpIcon = findViewById(R.id.thumbs_up_image);
        backButton = findViewById(R.id.profile_go_back_button);
        emailIcon = findViewById(R.id.email_icon_private);
        callIcon = findViewById(R.id.call_icon_private);

        Intent i = getIntent();
        if(i.getStringExtra("username") == null) { // current user's profile
            UserStore.getUser(ActiveUser.getUser().getUsername())
                    .addOnSuccessListener(new OnSuccessListener<User>() {
                        @Override
                        public void onSuccess(User user) {
                            username.setText(user.getUsername());
                            userFirstName.setText(user.getFirstName());
                            userEmail.setText(user.getEmail());
                            userPhoneNumber.setText(user.getPhoneNumber());

                            if (user.getType() == UserType.DRIVER) {
                                Integer upVotes = new Integer(((Driver) user).getPositiveRating());
                                Integer downVotes = new Integer(((Driver) user).getNegativeRating());
                                userUpRating.setText(upVotes.toString());
                                userDownRating.setText(downVotes.toString());

                            } else { //if (user.getType() == UserType.RIDER)
                                thumbsDownIcon.setVisibility(View.GONE);
                                thumbsUpIcon.setVisibility(View.GONE);
                                userDownRating.setVisibility(View.GONE);
                                userUpRating.setVisibility(View.GONE);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("UserProfileActivity", e.toString());
                    Toast.makeText(UserProfileActivity.this, "Cannot show profile", Toast.LENGTH_SHORT).show();
                }
            });

        } else { // another user's profile
            final String otherUsername = i.getStringExtra("username");
            editButton.setVisibility(View.GONE);
            UserStore.getUser(otherUsername)
                    .addOnSuccessListener(new OnSuccessListener<User>() {
                        @Override
                        public void onSuccess(User user) {
                            Log.d("UserProfileActivity", "User Received");
                            userFirstName.setText(user.getFirstName());
                            username.setText(user.getUsername());
                            userEmail.setText(user.getEmail());
                            userPhoneNumber.setText(user.getPhoneNumber());

                            if (user.getType() == UserType.DRIVER) {
                                Driver otherUser = (Driver) user;
                                String upVotes = String.format("%d", otherUser.getPositiveRating());
                                String downVotes = String.format("%d", otherUser.getNegativeRating());
                                userUpRating.setText(upVotes);
                                userDownRating.setText(downVotes);

                            } else { //if (user.getType() == UserType.RIDER)
                                thumbsDownIcon.setVisibility(View.GONE);
                                thumbsUpIcon.setVisibility(View.GONE);
                                userDownRating.setVisibility(View.GONE);
                                userUpRating.setVisibility(View.GONE);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("UserProfileActivity", e.toString());
                            Toast.makeText(UserProfileActivity.this, "Cannot show profile", Toast.LENGTH_SHORT).show();
                }
            });
        }
      
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new EditUserProfileFragment().show(getSupportFragmentManager(),"Edit user contact info");
            }
        });

       userEmail.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               openEmailActivity();
           }
       });

       userPhoneNumber.setOnClickListener(new View.OnClickListener() {
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
        userPhoneNumber.setText(newPhone);
        userFirstName.setText(newName);

        //UPDATE FIREBASE
        ActiveUser.getUser().setEmail(newEmail);
        ActiveUser.getUser().setFirstName(newName);
        ActiveUser.getUser().setPhoneNumber(newPhone);
        UserStore.saveUser(ActiveUser.getUser());
    }

    public void openEmailActivity(){
        String email = userEmail.getText().toString();
        Intent intent = new Intent(UserProfileActivity.this, EmailActivity.class);
        intent.putExtra("to", email);
        startActivity(intent);
    }

    public void openCallActivity(){
        String phone = userPhoneNumber.getText().toString();
        Intent intent = new Intent(UserProfileActivity.this, CallActivity.class);
        intent.putExtra("call", phone);
        startActivity(intent);
    }
}


