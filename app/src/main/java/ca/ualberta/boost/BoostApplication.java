package ca.ualberta.boost;

import android.app.Application;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class BoostApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseFirestore store = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        store.setFirestoreSettings(settings);
    }
}
