package ca.ualberta.boost.models;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

/**
 * This class resolves or rejects a Promise.
 * @param <TResult>
 * @see Promise
 */

public class PromiseImpl<TResult> implements Promise<TResult> {
    // Callback functions to run on success or failure
    private OnSuccessListener<TResult> resolveCallback = null;
    private OnFailureListener rejectCallback = null;

    public PromiseImpl() {}

    /**
     * @param result
     *      The promised data
     */
    public void resolve(TResult result) {
        if (resolveCallback != null) {
            resolveCallback.onSuccess(result);
        }
    }

    /**
     * @param e
     *      An exception, error occurred
     */
    public void reject(Exception e) {
        if (rejectCallback != null) {
            rejectCallback.onFailure(e);
        }
    }

    /**
     * @param func
     *      The function to be called when the promise is resolved (the result is realized)
     */
    public Promise<TResult> addOnSuccessListener(OnSuccessListener<TResult> func) {
        resolveCallback = func;
        return this; // return the same object, add ability to add on failure listener
    }

    /**
     * @param func
     *      The function to be called when the promise is rejected (an error occurred)
     */
    public void addOnFailureListener(OnFailureListener func) {
        rejectCallback = func;
    }
}
