package ca.ualberta.boost;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import ca.ualberta.boost.models.ActiveUser;
import ca.ualberta.boost.models.User;

/**TAKE NEW UPDATE ON USER CONTACT (EMAIL, PHONE NUMBER)
 * UPDATE TEXTVIEW AND SEND DATA TO PREVIOUS ACTIVITY TO UPDATE ON FIREBASE
 */
public class EditUserProfileFragment extends DialogFragment {
    FirebaseUser currentUser;
    User user;


    private EditText email;
    private EditText phone;
    private EditText username;
    private EditText password;
    private OnFragmentInteractionListener listener;

    EditUserProfileFragment(){}
    EditUserProfileFragment(FirebaseUser currentUser){
        this.currentUser = currentUser;
    }

    public interface OnFragmentInteractionListener {
        void onOkPressedEdit(String newEmail, String newPhone,String newUsername, String newPassword);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "must implement OnFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstantState) {
        //Inflate the layout for this fragment
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.edit_user_profile,null);

        //Initialize
        email = view.findViewById(R.id.email_input);
        phone = view.findViewById(R.id.phone_input);
        username = view.findViewById(R.id.username_input);
        password = view.findViewById(R.id.password_input);


        //retrieve current User profile info
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        user = ActiveUser.getUser();

        //retrieve current User profile info
        username.setText(user.getUsername());
        email.setText(user.getEmail());
        phone.setText(user.getPhoneNumber());
        password.setText(user.getPassword());

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Edit Profile Contact")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String email_input = email.getText().toString();
                        String phone_input  = phone.getText().toString();
                        String username_input = username.getText().toString();
                        String password_input  = password.getText().toString();
                        listener.onOkPressedEdit(email_input, phone_input,username_input,password_input);
                    }
                }).create();

    }
}
