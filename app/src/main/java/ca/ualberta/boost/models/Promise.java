package ca.ualberta.boost.models;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public interface Promise<TResult> {
    void addOnSuccessListener(OnSuccessListener<TResult> func);
    void addOnFailureListener(OnFailureListener func);
}
