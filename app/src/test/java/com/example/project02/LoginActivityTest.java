package com.example.project02;

import org.junit.Test;

public class LoginActivityTest {

    @Test
    public void activity_is_created_successfully() {
        // Verify that the activity is successfully created without crashing when onCreate is called with a null savedInstanceState.
        // TODO implement test
    }

    @Test
    public void activity_re_creation_with_saved_state() {
        // Verify that the activity is successfully re-created without crashing when onCreate is called with a non-null savedInstanceState bundle, simulating a configuration change like screen rotation.
        // TODO implement test
    }

    @Test
    public void correct_layout_file_is_set() {
        // Confirm that setContentView is called with the expected layout resource, R.layout.activity_login.
        // TODO implement test
    }

    @Test
    public void repository_is_initialized() {
        // Ensure that the PantryManagerRepository instance is not null after onCreate has been executed.
        // TODO implement test
    }

    @Test
    public void username_field_is_initialized() {
        // Verify that the usernameField (EditText) is correctly found and initialized, i.e., it is not null.
        // TODO implement test
    }

    @Test
    public void password_field_is_initialized() {
        // Verify that the passwordField (EditText) is correctly found and initialized, i.e., it is not null.
        // TODO implement test
    }

    @Test
    public void error_message_view_is_initialized() {
        // Verify that the errorMessage (TextView) is correctly found and initialized, i.e., it is not null.
        // TODO implement test
    }

    @Test
    public void login_button_is_initialized() {
        // Verify that the loginButton is correctly found and initialized, i.e., it is not null.
        // TODO implement test
    }

    @Test
    public void create_account_button_is_initialized() {
        // Verify that the createAccountButton is correctly found and initialized, i.e., it is not null.
        // TODO implement test
    }

    @Test
    public void login_button_has_a_click_listener() {
        // Confirm that an OnClickListener is successfully attached to the loginButton.
        // TODO implement test
    }

    @Test
    public void create_account_button_has_a_click_listener() {
        // Confirm that an OnClickListener is successfully attached to the createAccountButton.
        // TODO implement test
    }

    @Test
    public void create_Account_button_click_launches_correct_activity() {
        // Simulate a click on the createAccountButton and verify that an Intent to launch CreateAccountActivity is created and that startActivity is called with this intent.
        // TODO implement test
    }

}