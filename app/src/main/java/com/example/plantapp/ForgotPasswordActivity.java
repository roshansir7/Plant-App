package com.example.plantapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText emailEditText;
    Button resetButton, backToLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Initialize views
        emailEditText = findViewById(R.id.emailEditText);
        resetButton = findViewById(R.id.resetButton);
        backToLoginButton = findViewById(R.id.backToLoginButton);

        // Reset password logic (temporary)
        resetButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
            } else {
                // Temporary logic: replace with real password reset later
                Toast.makeText(this, "Password reset link sent to " + email, Toast.LENGTH_SHORT).show();
            }
        });

        // Back to Login button logic
        backToLoginButton.setOnClickListener(v -> {
            finish(); // closes ForgotPasswordActivity and returns to LoginActivity
        });
    }
}
