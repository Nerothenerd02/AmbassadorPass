package com.example.ambassadorpass.view

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.ambassadorpass.R
import com.example.ambassadorpass.repository.UserRepository
import com.example.ambassadorpass.viewmodel.ForgotPasswordViewModel
import com.example.ambassadorpass.viewmodel.ForgotPasswordViewModelFactory

class ForgotPassword : AppCompatActivity() {

    private lateinit var emailInput: EditText
    private lateinit var submitButton: Button
    private lateinit var backButton: ImageButton

    private val forgotPasswordViewModel: ForgotPasswordViewModel by viewModels {
        ForgotPasswordViewModelFactory(UserRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        // Initialize views
        emailInput = findViewById(R.id.emailInput)
        submitButton = findViewById(R.id.submitButton)
        backButton = findViewById(R.id.backButton)

        // Back button functionality
        backButton.setOnClickListener { finish() }

        // Submit button functionality
        submitButton.setOnClickListener {
            val email = emailInput.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Trigger ViewModel to send password reset email
            forgotPasswordViewModel.sendPasswordResetEmail(email).observe(this) { success ->
                if (success) {
                    Toast.makeText(
                        this,
                        "Password reset email sent to $email",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d("ForgotPassword", "Password reset email sent successfully.")
                    finish() // Close the activity
                } else {
                    Toast.makeText(
                        this,
                        "Failed to send password reset email. Please try again.",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("ForgotPassword", "Error sending password reset email.")
                }
            }
        }
    }
}
