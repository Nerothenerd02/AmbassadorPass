package com.example.ambassadorpass

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivityAmbassador : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_ambassador)

        // Initialize the views
        val usernameField: EditText = findViewById(R.id.username)
        val passwordField: EditText = findViewById(R.id.password)
        val loginButton: Button = findViewById(R.id.loginButton)

        // Set up the back button functionality
        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            finish() // Closes this activity and returns to the previous one
        }

        // Set up the login button functionality
        loginButton.setOnClickListener {
            val username = usernameField.text.toString()
            val password = passwordField.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show()
            } else {
                // Placeholder for login authentication (replace with actual login logic if needed)
                if (username == "ambassador" && password == "1234") {
                    Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
                    // Add navigation if needed
                } else {
                    Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
