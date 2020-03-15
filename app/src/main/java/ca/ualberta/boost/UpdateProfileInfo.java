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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UpdateProfileInfo extends AppCompatActivity {

    EditText name;
    EditText phone;
    EditText role;
    Button confirmButton;

    //firebase
    FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile_info);

        name = findViewById(R.id.updateProfileName);
        phone = findViewById(R.id.updatePhoneNumber);
        role = findViewById(R.id.updateProfileRole);
        confirmButton = findViewById(R.id.updateProfileButton);

        //firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateInformation(name.getText().toString(), phone.getText().toString(),role.getText().toString());
            }
        });
    }

    private void updateInformation(String name1 ,String phoneNumber, String role1){
        DocumentReference documentReference = db.collection("users").document(auth.getUid());
        if(!(name1.matches("")&&phoneNumber.matches("")&&role1.matches(""))){
            Map<String, Object> map = new HashMap<>();
            map.put("Name",name1);
            map.put("Phone",phoneNumber);
            map.put("role",role1);
            documentReference.update(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(UpdateProfileInfo.this, "Information Updated", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else{
            Toast.makeText(this, "Some fields are missing content", Toast.LENGTH_SHORT).show();
        }
//        Map<String, Object> map = new HashMap<>();
//        map.put("Name",name.getText().toString());
//        map.put("Phone",phone.getText().toString());
//        map.put("role",role.getText().toString());
//        documentReference.update(map)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//
//                    }
//                });
    }


}
