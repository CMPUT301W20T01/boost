package ca.ualberta.boost.controllers;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import ca.ualberta.boost.R;

public class FirebaseFirestoreTestiew extends AppCompatActivity {
    FirebaseController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty_activity);

        controller = new FirebaseController();
        controller.retrieve("ride_requests");
    }
}
