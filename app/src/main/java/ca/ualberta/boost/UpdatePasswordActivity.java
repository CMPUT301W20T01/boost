package ca.ualberta.boost;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UpdatePasswordActivity extends AppCompatActivity {

    EditText email;
    EditText password;
    EditText newPassword;
    Button confirmButton;

    //firebase
    FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        email = findViewById(R.id.newPasswordEmail);
        password = findViewById(R.id.newPasswordPassword);
        newPassword = findViewById(R.id.newPasswordConfirmPassword);
        confirmButton = findViewById(R.id.newPasswordButton);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePassword();
            }
        });

    }

    private void updatePassword(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(email.getText().toString(),password.getText().toString());
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(UpdatePasswordActivity.this, "Authenticated", Toast.LENGTH_SHORT).show();
                        FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
                        user1.updatePassword(newPassword.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(UpdatePasswordActivity.this, "Password Updated", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                });
        DocumentReference documentReference = db.collection("users").document(auth.getUid());
        Map<String, Object> map = new HashMap<>();
        map.put("Password",newPassword.getText().toString());
        documentReference.update(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
    }

    private void clear(){
        email.setText("");
        password.setText("");
        newPassword.setText("");
    }
}
