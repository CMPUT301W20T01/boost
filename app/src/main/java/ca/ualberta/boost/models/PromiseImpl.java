package ca.ualberta.boost.models;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class PromiseImpl<TResult> implements Promise<TResult> {
    // Lambda functions to run on success or failure
    private OnSuccessListener<TResult> resolveCallback = null;
    private OnFailureListener rejectCallback = null;

    public PromiseImpl() {}

    /**
     * @param result promised data
     */
    public void resolve(TResult result) {
        if (resolveCallback != null) {
            resolveCallback.onSuccess(result);
        }
    }

    /**
     * @param e exception, error occurred
     */
    public void reject(Exception e) {
        if (rejectCallback != null) {
            rejectCallback.onFailure(e);
        }
    }

    /**
     * @param func the function to be called when the promise is resolved (the result is realized)
     */
    public void addOnSuccessListener(OnSuccessListener<TResult> func) {
        resolveCallback = func;
    }

    /**
     * @param func the function to be called when the promise is rejected (an error occurred)
     */
    public void addOnFailureListener(OnFailureListener func) {
        rejectCallback = func;
    }
}
