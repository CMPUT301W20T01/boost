package ca.ualberta.boost;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

public class EmailActivity extends AppCompatActivity {
    EditText emailTo;
    EditText emailSubject;
    EditText emailMessage;

    String receiver ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        emailTo = findViewById(R.id.editText_to);
        emailSubject = findViewById(R.id.editText_subject);
        emailMessage = findViewById(R.id.editText_Message);

        receiver = getIntent().getExtras().getString("to");

        emailTo.setText(receiver);



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

        ImageView backButton = findViewById(R.id.email_activity_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}