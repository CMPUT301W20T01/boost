package ca.ualberta.boost;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import ca.ualberta.boost.models.ActiveUser;
import ca.ualberta.boost.models.User;

/**TAKE NEW UPDATE ON USER CONTACT (EMAIL, PHONE NUMBER)
 * UPDATE TEXTVIEW AND SEND DATA TO PREVIOUS ACTIVITY TO UPDATE ON FIREBASE
 */
public class EditUserProfileFragment extends DialogFragment {
//    FirebaseUser currentUser;
    User user;

    private EditText email;
    private EditText phone;
    private EditText name;
    private OnFragmentInteractionListener listener;

    EditUserProfileFragment(){}

    public interface OnFragmentInteractionListener {
        void onOkPressedEdit(String newEmail, String newPhone,String newName);
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
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_edit_profile,null);

        //Initialize
        email = view.findViewById(R.id.email_input);
        phone = view.findViewById(R.id.phone_input);
        name = view.findViewById(R.id.name_input);

        user = ActiveUser.getUser();

        //retrieve current User profile info
        name.setText(user.getFirstName());
        email.setText(user.getEmail());
        phone.setText(user.getPhoneNumber());

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String email_input = email.getText().toString();
                        String phone_input  = phone.getText().toString();
                        String name_input = name.getText().toString();
                        listener.onOkPressedEdit(email_input, phone_input,name_input);
                    }
                }).create();

    }
}
