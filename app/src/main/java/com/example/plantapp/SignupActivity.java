package com.example.plantapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class SignupActivity extends AppCompatActivity {

    EditText nameEditText, surnameEditText, usernameEditText, passwordEditText, confirmPasswordEditText;
    CheckBox acceptCookiesCheckBox;
    Button signupButton, backToLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize views
        nameEditText = findViewById(R.id.nameEditText);
        surnameEditText = findViewById(R.id.surnameEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        acceptCookiesCheckBox = findViewById(R.id.acceptCookiesCheckBox);
        signupButton = findViewById(R.id.signupButton);
        backToLoginButton = findViewById(R.id.backToLoginButton);

        // Sign Up button logic
        signupButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String surname = surnameEditText.getText().toString().trim();
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String confirmPassword = confirmPasswordEditText.getText().toString().trim();
            boolean accepted = acceptCookiesCheckBox.isChecked();

            // Validation
            if (name.isEmpty() || surname.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!accepted) {
                Toast.makeText(this, "You must accept cookies to proceed", Toast.LENGTH_SHORT).show();
                return;
            }

            // Temporary signup logic
            Toast.makeText(this, "Signup successful!", Toast.LENGTH_SHORT).show();

            // Return to Login page
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        // Back to Login button logic
        backToLoginButton.setOnClickListener(v -> finish());
    }
}
