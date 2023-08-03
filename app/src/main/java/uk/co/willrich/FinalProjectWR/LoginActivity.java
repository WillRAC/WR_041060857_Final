package uk.co.willrich.FinalProjectWR;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class LoginActivity extends AppCompatActivity implements LoginFragmentInterface {
    private static final String SHARED_PREFERENCES_CREDENTIALS_FILE = "uk.co.willrich.FinalProjectWR.SHARED_PREFERENCES_CREDENTIALS_FILE";
    private static final String SHARED_PREFERENCES_CREDENTIALS_USERNAME = "uk.co.willrich.FinalProjectWR.SHARED_PREFERENCES_CREDENTIALS_USERNAME";
    private static final String SHARED_PREFERENCES_CREDENTIALS_PASSWORD = "uk.co.willrich.FinalProjectWR.SHARED_PREFERENCES_CREDENTIALS_PASSWORD";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setUpLoginFragment();

    }

    /**
     * This method adds the Login Fragment onto the Fragment Container in this Activity.
     */
    private void setUpLoginFragment() {

        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.loginFragmentContainer, new LoginFragment());
        fragmentTransaction.commit();
    }

    /**
     * This method validates if the credentials entered by the user are equal to the credentials saved in the SharedPreferences of the app.
     * If they're not it returns false. If they are, it returns true.
     * Also, if it can't find any stored credentials it returns default empty credentials.
     *
     * @param username (String) User inputted username.
     * @param password (String) User inputted password.
     * @return (boolean) True if credentials match, else, false.
     */
    private boolean checkUserCredentials(String username, String password){

        SharedPreferences sharedPreferences = this.getSharedPreferences(SHARED_PREFERENCES_CREDENTIALS_FILE, Context.MODE_PRIVATE);
        String savedUsername = sharedPreferences.getString(SHARED_PREFERENCES_CREDENTIALS_USERNAME, "");
        String savedPassword = sharedPreferences.getString(SHARED_PREFERENCES_CREDENTIALS_PASSWORD, "");

        if (savedUsername.equals(username)&& savedPassword.equals(password)){
            return true;
        } else {

            Toast.makeText(LoginActivity.this, "Wrong Credentials", Toast.LENGTH_LONG).show();
            return false;
        }

    }

    /**
     * This method launches the Articles List Activity upon successful credentials matching.
     * It also finishes this Login Activity to ensure the user can not back-track to it using the back button.
     */
    private void startActivityArticlesList() {

        Intent inLaunchArticlesList = new Intent(LoginActivity.this, ArticleListActivity.class);
        this.startActivity(inLaunchArticlesList);
        this.finish();
    }

    @Override
    public void loginUser(String username, String password) {

        if (checkUserCredentials(username, password)){
            startActivityArticlesList();

        }



    }
}