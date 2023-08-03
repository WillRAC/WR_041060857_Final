package uk.co.willrich.FinalProjectWR;

public interface LoginFragmentInterface {

    /**
     * This method helps the Login Fragment communicate with the Login Activity.
     * It takes in the username and user password from the Login Fragment, and passes them to the Login Activity for validation.
     *
     * @param username (String) The inputted username.
     * @param password (String) The inputted password.
     */
    void loginUser(String username, String password);
}
