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
import ca.ualberta.boost.controllers.ActiveUser;
import ca.ualberta.boost.models.UserType;
import ca.ualberta.boost.stores.UserStore;

/**
 * Retrieves a user's profile to display
 */
public class UserProfileActivity extends AppCompatActivity implements EditUserProfileFragment.OnFragmentInteractionListener {

    TextView username;
    TextView userFirstName;
    TextView userEmail;
    TextView userPhoneNumber;
    ImageButton editButton;
    ImageButton backButton;
    ImageButton historyButton;
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
        historyButton = findViewById(R.id.history_button);

        setDriverViewsVisibility(View.GONE);
        historyButton.setVisibility(View.GONE);

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
                                historyButton.setVisibility(View.VISIBLE);
                                setDriverViewsVisibility(View.VISIBLE);
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
                                setDriverViewsVisibility(View.VISIBLE);
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

        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRideHistoryActivity();
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

    /**
     * Launches EmailActivity
     * @see EmailActivity
     */
    public void openEmailActivity() {
        String email = userEmail.getText().toString();
        Intent intent = new Intent(UserProfileActivity.this, EmailActivity.class);
        intent.putExtra("to", email);
        startActivity(intent);
    }

    /**
     * Launches CallActivity
     * @see CallActivity
     */
    public void openCallActivity() {
        String phone = userPhoneNumber.getText().toString();
        Intent intent = new Intent(UserProfileActivity.this, CallActivity.class);
        intent.putExtra("call", phone);
        Log.d("UserProfileActivity", phone);
        startActivity(intent);
    }

    /**
     * Launches RideHistoryActivity
     * @see RideHistoryActivity
     */
    public void openRideHistoryActivity() {
        Intent intent = new Intent(UserProfileActivity.this, RideHistoryActivity.class);
        startActivity(intent);
    }

    /**
     * Sets the driver's views' visibilities
     * @param visibility
     *      visibility to set the views to. One of:
     *      View.GONE, View.VISIBLE, View.INVISIBLE
     */
    private void setDriverViewsVisibility(int visibility) {
        thumbsDownIcon.setVisibility(visibility);
        thumbsUpIcon.setVisibility(visibility);
        userDownRating.setVisibility(visibility);
        userUpRating.setVisibility(visibility);
    }
}


