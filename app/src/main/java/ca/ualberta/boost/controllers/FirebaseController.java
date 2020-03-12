package ca.ualberta.boost.controllers;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class FirebaseController{
    //get references to fireStore
    FirebaseFirestore database;
    Query query;
    CollectionReference ref;
    public Context context;

    public FirebaseController(){
        database= FirebaseFirestore.getInstance();
    }

    public void retrieve(String collectionName) {
        ref = database.collection(collectionName);
        ref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        Log.d("QUERY RESULT", document.get("ID").toString());
                    }
                }
            }
        });
    }
}