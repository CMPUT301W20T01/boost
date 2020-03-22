package ca.ualberta.boost;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

/** Class that displays user contact information through a fragment
 */

public class UserContactInformationFragment extends DialogFragment {

    public String email;
    TextView riderPhoneNumber;
    TextView riderEmail;
    private CollectionReference collection;
    private FirebaseFirestore db;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        riderPhoneNumber = view.findViewById(R.id.userFragmentPhoneNumber);
        riderEmail = view.findViewById(R.id.userFragmentEmail);
        //get username from previous fragment
        email = getArguments().getString("username");
        db = FirebaseFirestore.getInstance();
        collection = db.collection("users");
        collection.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            String test = getArguments().getString("username");
                            for(QueryDocumentSnapshot document: task.getResult()){
                                if(test.matches(document.get("username").toString())){
                                    riderPhoneNumber.setText("Phone Number: " + document.get("phoneNumber").toString());
                                    riderEmail.setText("Email: " + document.get("email").toString());
                                }
                            }
                        }
                    }
                });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragmentdisplayinformation, container, false);
    }
}
