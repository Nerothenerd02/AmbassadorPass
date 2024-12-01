package com.example.ambassadorpass.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.ambassadorpass.R
import com.example.ambassadorpass.view.admin.AdminDashboard
import com.example.ambassadorpass.viewmodel.NewPasswordViewModel
import com.example.ambassadorpass.viewmodel.NewPasswordViewModelFactory
import com.example.ambassadorpass.repository.UserRepository
import com.example.ambassadorpass.view.ambassador.AmbassadorDashboard

class NewPassword : AppCompatActivity() {

    private lateinit var passwordInput: EditText
    private lateinit var confirmPasswordInput: EditText
    private lateinit var submitButton: Button

    private val viewModel: NewPasswordViewModel by viewModels {
        NewPasswordViewModelFactory(UserRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_password)

        // Initialize views
        passwordInput = findViewById(R.id.passwordInput)
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput)
        submitButton = findViewById(R.id.submitButton)

        submitButton.setOnClickListener {
            val password = passwordInput.text.toString().trim()
            val confirmPassword = confirmPasswordInput.text.toString().trim()

            if (password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Both fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Update the password
            viewModel.updatePassword(password).observe(this) { success ->
                if (success) {
                    // Fetch user tier and navigate to respective page
                    viewModel.getUserTier().observe(this) { tier ->
                        when (tier) {
                            "gold" -> {
                                startActivity(Intent(this, AdminDashboard::class.java))
                            }
                            "silver" -> {
                                startActivity(Intent(this, AmbassadorDashboard::class.java))
                            }
                            else -> {
                                Toast.makeText(this, "Unknown user tier", Toast.LENGTH_SHORT).show()
                            }
                        }
                        finish()
                    }
                } else {
                    Toast.makeText(this, "Failed to update password", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
