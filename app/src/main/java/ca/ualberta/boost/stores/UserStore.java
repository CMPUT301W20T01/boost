package ca.ualberta.boost.stores;

import androidx.annotation.NonNull;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

import ca.ualberta.boost.models.Driver;
import ca.ualberta.boost.models.Promise;
import ca.ualberta.boost.models.PromiseImpl;
import ca.ualberta.boost.models.Rider;
import ca.ualberta.boost.models.User;
import ca.ualberta.boost.models.UserType;

/**
 * This class stores and retrieves a User to and from the FireStore database.
 */

public class UserStore {
    private static final String TAG = "UserStore";
    private static UserStore instance = null;

    private CollectionReference userCollection;

    /**
     * Constructor
     */
    private UserStore() {
        userCollection = FirebaseFirestore.getInstance().collection("users");
    }

    /**
     * Singleton, creates an instance of a UserStore once
     * @return
     *      The instance of the UserStore
     */
    private static UserStore getInstance() {
        if (instance == null) {
            instance = new UserStore();
        }
        return instance;
    }

    /**
     * Saves a user in the user table of the database
     * @param user
     * @see User
     */
    public static void saveUser(User user) {
        UserStore store = getInstance();

        Map<String, Object> data = user.data();

        store.userCollection
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
    }

    /**
     * Tries to retrieve the data of a specified User
     * @param username
     *      username of the User wanted from database
     * @return
     *      Promise of the data for the requested User
     * @see User
     * @see Promise
     * @see PromiseImpl
     */
    public static Promise<User> getUser(final String username) {
        UserStore store = getInstance();

        final PromiseImpl<User> userPromise = new PromiseImpl<>();
        store.userCollection.document(username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (!task.isSuccessful()) {
                            userPromise.reject(new Exception("User does not exist"));
                            return;
                        }

                        DocumentSnapshot result = task.getResult();
                        if (result == null) {
                            userPromise.reject(new Exception("Something went wrong"));
                            return;
                        }

                        Map<String, Object> data = result.getData();

                        if (data == null) {
                            userPromise.reject(new Exception("Something went wrong"));
                            return;
                        }

                        User user;
                        if (data.get("type") == UserType.RIDER) {
                            user = Rider.build(data);
                        } else {
                            user = Driver.build(data);
                        }
                        userPromise.resolve(user);
                    }
                });

        return userPromise;
    }
}
