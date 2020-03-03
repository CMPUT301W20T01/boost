package ca.ualberta.boost;


import androidx.annotation.NonNull;

import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

import ca.ualberta.boost.models.User;

public class UserStore {
    private static final String TAG = "UserStore";
    private CollectionReference userCollection;

    public UserStore saveUser(User user) {

        userCollection = FirebaseFirestore.getInstance().collection("users");
        Map<String, String> data = user.data();

        userCollection
                .document(user.getUsername())
                .set(user.data())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG,"User Data Save Success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "User Data Save Failed");
                    }
                });

        return this;
    }

}
