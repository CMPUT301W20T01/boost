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
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser currentUser;
    private FirebaseFirestore db;
    private CollectionReference collection;
//    DocumentReference documentReference;

    EditText et1;
    EditText et2;
    EditText et3;
    EditText et4;

    Button editButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        collection = db.collection("users");
        String id = auth.getUid();

        et1 = findViewById(R.id.epEmail);
        et2 = findViewById(R.id.epPassword);
        et3 = findViewById(R.id.epNewEmail);
//        et4 = findViewById(R.id.epNewPassword);
        editButton = findViewById(R.id.editButton);


        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateEmail();
            }
        });


//        editButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                AuthCredential credential = EmailAuthProvider.getCredential(et1.getText().toString(),et2.getText().toString());
//                user.reauthenticate(credential)
//                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                Toast.makeText(EditProfileActivity.this, "reAuthentication", Toast.LENGTH_SHORT).show();
//                                FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
//                                user1.updateEmail(et3.getText().toString())
//                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//                                                if(task.isSuccessful()){
////                                                    Toast.makeText(EditProfileActivity.this, "email updated", Toast.LENGTH_SHORT).show();
//                                                }
//                                            }
//                                        });
//                                user1.updatePassword(et4.getText().toString())
//                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//                                                if(task.isSuccessful()){
//                                                Toast.makeText(EditProfileActivity.this, "password updated", Toast.LENGTH_SHORT).show();
//                                                }
//                                            }
//                                        });
                            }
//                        });
//                FirebaseUser user2 = FirebaseAuth.getInstance().getCurrentUser();
//                AuthCredential credential2 = EmailAuthProvider.getCredential(et1.getText().toString(),et2.getText().toString());
//                Toast.makeText(EditProfileActivity.this, "here ;)", Toast.LENGTH_SHORT).show();
//                user2.updatePassword(et4.getText().toString())
//                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                if(task.isSuccessful()){
//                                    Toast.makeText(EditProfileActivity.this, "password updated", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
//            }
//        });
//                FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
//                AuthCredential credential1 = EmailAuthProvider.getCredential(et3.getText().toString(),et2.getText().toString());
//                user.reauthenticate(credential1).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
//                        user1.updatePassword(et4.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                if(task.isSuccessful()){
//                                    Toast.makeText(EditProfileActivity.this, "Password Updated", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
//
//                    }
//                });


//                currentUser.updateEmail(et1.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        Toast.makeText(EditProfileActivity.this, "reset email", Toast.LENGTH_SHORT).show();
//                    }
//                });

//    }

    private void updateEmail(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(et1.getText().toString(),et2.getText().toString());
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(EditProfileActivity.this, "reAuthentication", Toast.LENGTH_SHORT).show();
                        FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
                        user1.updateEmail(et3.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                                    Toast.makeText(EditProfileActivity.this, "email updated", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                });
        DocumentReference documentReference = db.collection("users").document(auth.getUid());
        Map<String, Object> map = new HashMap<>();
        map.put("Email",et3.getText().toString());
        documentReference.update(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });

    }

}
