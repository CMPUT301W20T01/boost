package ca.ualberta.boost.models;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

/**
 * This interface holds a future result.
 * @param <TResult>
 */

public interface Promise<TResult> {
    /**
     * @param func
     *      The function to be called when the promise is resolved (the result is realized)
     * @see PromiseImpl
     */
    void addOnSuccessListener(OnSuccessListener<TResult> func);

    /**
     * @param func
     *      The function to be called when the promise is rejected (an error occurred)
     * @see PromiseImpl
     */
    void addOnFailureListener(OnFailureListener func);
}
