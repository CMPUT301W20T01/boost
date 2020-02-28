package ca.ualberta.boost;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class HomeActivityRider extends AppCompatActivity {

    FirebaseAuth auth;
    Button signout;

    //these are temporary text views to showcase how information is pulled out of the database
    TextView textViewEmail;
    TextView textViewId;
    TextView textViewAge;

    FirebaseFirestore db;
    CollectionReference ref1;
    Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_rider);

        db = FirebaseFirestore.getInstance();
        ref1 = db.collection("users");
        auth = FirebaseAuth.getInstance();
        signout = findViewById(R.id.signoutButton);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewId = findViewById(R.id.textViewID);
        textViewAge = findViewById(R.id.textViewAge);
        loadUserInformation();

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                launchStartPage();
                finish();
            }
        });
    }

    //launch home page
    private void launchStartPage() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    //query database and display user information in textViews
    private void loadUserInformation(){
        String test = auth.getCurrentUser().getEmail();
        String uid = auth.getCurrentUser().getUid();
        query = ref1.whereEqualTo("id",uid);
        textViewEmail.setText("email: "+test);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        textViewId.setText("ID: "+document.get("id").toString());
                        textViewAge.setText("Age: "+document.get("age").toString());
                    }
                }
            }
        });

    }
}
