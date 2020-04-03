package ca.ualberta.boost;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

/* Reference
         =>YouTube tutorial by 'Android Coding' on 'How to Send Email in Android Studio | SendEmail | Android Coding'
           URI to Video = https://www.youtube.com/watch?v=aV7-vcwEeRM
           URI to username = https://www.youtube.com/channel/UCUIF5MImktJLDWDKe5oTdJQ
*/

/**
 * EmailActivity is responsible for handling messages between Rider and Driver using intent
 */

public class EmailActivity extends AppCompatActivity {
    private EditText emailTo;
    private EditText emailSubject;
    private EditText emailMessage;
    private ImageButton backButton;

    //holds email from UserProfileActivity that was clicked
    private String receiver ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        //initialize Views
        emailTo = findViewById(R.id.editText_to);
        emailSubject = findViewById(R.id.subject_field);
        emailMessage = findViewById(R.id.message_field);
        backButton = findViewById(R.id.message_go_back_button);

        //get email from UserProfileActivity and set emailTo
        receiver = getIntent().getExtras().getString("to");
        emailTo.setText(receiver);

        // open email client when sendButton is clicked
        Button sendButton = findViewById(R.id.send_message_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW
                        , Uri.parse("mailto:" + emailTo.getText().toString()));
                intent.putExtra(Intent.EXTRA_SUBJECT, emailSubject.getText().toString());
                intent.putExtra(Intent.EXTRA_TEXT, emailMessage.getText().toString());
                startActivity(intent);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}