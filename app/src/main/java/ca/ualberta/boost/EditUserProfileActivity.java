package ca.ualberta.boost;

//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.FirebaseAuth;
////import com.google.firebase.database.DatabaseReference;
////import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.firestore.CollectionReference;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//import com.google.firebase.firestore.QuerySnapshot;
//import com.google.firebase.firestore.SetOptions;
//
//import java.util.HashMap;
//import java.util.Map;
//
public class EditUserProfileActivity extends AppCompatActivity {
//    private FirebaseAuth auth;
//    private FirebaseFirestore db;
//    //private DatabaseReference firebaseData;
//    private CollectionReference collection;
//    DocumentReference documentReference;
//
//    EditText name;
//    EditText email;
//    EditText phone;
//    EditText role;
//    Button confirmChangesButton;
//    Button discardChangesButton;
//
//    private String userID;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.edit_user_profile);
//
//        //Initialize
//        name = findViewById(R.id.editUserProfileName);
//        email = findViewById(R.id.editUserProfileEmail);
//        phone = findViewById(R.id.editUserProfilePhone);
//        role = findViewById(R.id.editUserProfileRole);
//        confirmChangesButton = findViewById(R.id.editUserProfileConfirmButton);
//        discardChangesButton = findViewById(R.id.editUserProfileCancelButton);
//
//        auth = FirebaseAuth.getInstance();
//        db = FirebaseFirestore.getInstance();
//        collection = db.collection("users");
//        userID = auth.getUid();
//        documentReference = db.collection("users").document(userID);
//
//        //firebaseData = FirebaseDatabase.getInstance().getReference();
//        //retrieve current User profile info
//        collection.get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if(task.isSuccessful()){
//                            for(QueryDocumentSnapshot document: task.getResult()){
//                                if((auth.getUid().matches(document.get("id").toString()))){
//                                    name.setText(document.get("Name").toString());
//                                    email.setText(document.get("Email").toString());
//                                    phone.setText(document.get("Phone").toString());
//                                    role.setText(document.get("Role").toString());
//                                }
//                            }
//                        }
//
//                    }
//                });
//
//
//
//        confirmChangesButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                firebaseData = FirebaseDatabase.getInstance().getReference();
//                Map<String, Object> map = new HashMap<>();
//                map.put("Name", name.getText().toString());
//                map.put("Email", email.getText().toString());
//                map.put("Phone", phone.getText().toString());
//                map.put("id", auth.getUid());
//                map.put("role", role.getText().toString());
//
////                Map<String, Object> map = new HashMap<>();
////                map.put("Name", name.getText().toString());
////                map.put("Email", email.getText().toString());
////                map.put("Phone",phone.getText().toString());
////                documentReference.set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
////                    @Override
////                    public void onComplete(@NonNull Task<Void> task) {
////                        Toast.makeText(EditUserProfileActivity.this, "updated", Toast.LENGTH_SHORT).show();
////                    }
////                });
//                documentReference.update(map);
////                firebaseData.child("users").child(userID).child("Name").setValue(name.getText());
////                firebaseData.child("users").child(userID).child("Email").setValue(email.getText());
////                firebaseData.child("users").child(userID).child("Phone").setValue(phone.getText())
////                        .addOnCompleteListener(new OnCompleteListener<Void>() {
////                            @Override
////                            public void onComplete(@NonNull Task<Void> task) {
////                                Toast.makeText(EditUserProfileActivity.this, "Profile Updated", Toast.LENGTH_LONG).show();
////                            }
////                        });
//
////                        .addOnSuccessListener(new OnSuccessListener<Void>() {
////                    @Override
////                    public void onSuccess(Void aVoid) {
////                        Toast.makeText(EditUserProfileActivity.this,"update success");
////                    }
////                })
////                        .addOnFailureListener(new OnFailureListener() {
////                            @Override
////                            Toast.makeText(EditUserProfileActivity.this,"update fail");
////                        });
//                finish();
//            }
//        });
//
//        discardChangesButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//
//    }
//
}
