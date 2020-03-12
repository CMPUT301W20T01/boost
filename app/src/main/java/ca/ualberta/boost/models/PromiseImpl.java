package ca.ualberta.boost.models;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class PromiseImpl<TResult> implements Promise<TResult> {
    // Lambda functions to run on success or failure
    private OnSuccessListener<TResult> resolveCallback = null;
    private OnFailureListener rejectCallback = null;

    public PromiseImpl() {}

    public void resolve(TResult result) {
        if (resolveCallback != null) {
            resolveCallback.onSuccess(result);
        }
    }

    public void reject(Exception e) {
        if (rejectCallback != null) {
            rejectCallback.onFailure(e);
        }
    }

    public void addOnSuccessListener(OnSuccessListener<TResult> func) {
        resolveCallback = func;
    }

    public void addOnFailureListener(OnFailureListener func) {
        rejectCallback = func;
    }
}
