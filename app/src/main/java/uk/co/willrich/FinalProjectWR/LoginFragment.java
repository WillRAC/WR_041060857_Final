package uk.co.willrich.FinalProjectWR;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class LoginFragment extends Fragment {

    private LoginFragmentInterface loginFragmentInterface;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try{
           loginFragmentInterface = (LoginFragmentInterface) context;
        } catch(ClassCastException ccex){
            ccex.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View loginFragment = inflater.inflate(R.layout.login_fragment, container, false);

        EditText usernameEditText = loginFragment.findViewById(R.id.userName);
        EditText passwordEditText = loginFragment.findViewById(R.id.password);

        Button enterButton = loginFragment.findViewById(R.id.loginEnter);
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Use the interface (LoginFragmentInterface) to communicate with the host Activity, i.e. activity hosting this fragment.
                loginFragmentInterface.loginUser(username, password);

            }
        });

        return loginFragment;
    }
}
