package com.example.ambassadorpass.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.ambassadorpass.R
import com.example.ambassadorpass.repository.UserRepository
import com.example.ambassadorpass.viewmodel.LoginViewModel
import com.example.ambassadorpass.viewmodel.LoginViewModelFactory
import com.example.ambassadorpass.view.admin.AdminDashboard
import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.security.ProviderInstaller

class LoginActivityAdmin : AppCompatActivity() {

    private lateinit var usernameField: EditText
    private lateinit var passwordField: EditText
    private lateinit var loginButton: Button

    private val loginViewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(UserRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_admin)

        // Initialize views
        usernameField = findViewById(R.id.username)
        passwordField = findViewById(R.id.password)
        loginButton = findViewById(R.id.loginButton)
        val backButton: ImageButton = findViewById(R.id.backButton)

        // Ensure security provider is up-to-date
        ProviderInstaller.installIfNeededAsync(this, object : ProviderInstaller.ProviderInstallListener {
            override fun onProviderInstalled() {
                // Provider is up-to-date, no action needed
            }

            override fun onProviderInstallFailed(errorCode: Int, recoveryIntent: Intent?) {
                val dialog = GoogleApiAvailability.getInstance().getErrorDialog(this@LoginActivityAdmin, errorCode, 1001)
                dialog?.show()
            }
        })

        // Back button functionality
        backButton.setOnClickListener { finish() }

        // Login button functionality
        loginButton.setOnClickListener {
            val email = usernameField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            loginViewModel.login(email, password).observe(this, Observer { success ->
                if (success) {
                    val currentUser = FirebaseAuth.getInstance().currentUser
                    if (currentUser != null) {
                        val email = currentUser.email ?: ""
                        Log.d("LoginActivityAdmin", "Authentication successful. User email: $email")

                        // Fetch user data by email
                        loginViewModel.getUserByEmail(email).observe(this, Observer { user ->
                            if (user != null) {
                                val isFirstTimeUser = user.isFirstTimeUser // Default to true if the field is missing
                                Log.d("LoginActivityAdmin", "User isFirstTimeUser: $isFirstTimeUser")

                                if (isFirstTimeUser) {
                                    Log.d("LoginActivityAdmin", "Navigating to NewPassword for first-time user")

                                    // Update Firestore to set isFirstTimeUser to false
                                    loginViewModel.updateFirstTimeUserStatus(email).observe(this, Observer { updateSuccess ->
                                        if (updateSuccess) {
                                            Log.d("LoginActivityAdmin", "isFirstTimeUser updated successfully for $email")
                                        } else {
                                            Log.e("LoginActivityAdmin", "Failed to update isFirstTimeUser for $email")
                                        }
                                    })

                                    startActivity(Intent(this, NewPassword::class.java))
                                    finish()
                                    return@Observer
                                }

                                val tier = user.tier
                                Log.d("LoginActivityAdmin", "User tier: $tier")

                                // Navigate based on tier
                                when (tier) {
                                    "gold" -> {
                                        Log.d("LoginActivityAdmin", "Navigating to AdminDashboard for 'gold' tier")
                                        startActivity(Intent(this, AdminDashboard::class.java))
                                    }
                                    "silver" -> {
                                        Log.d("LoginActivityAdmin", "Navigating to AmbassadorDashboard for 'silver' tier")
                                        startActivity(Intent(this, AmbassadorDashboard::class.java))
                                    }
                                    else -> {
                                        Log.d("LoginActivityAdmin", "Unknown tier: $tier")
                                        Toast.makeText(this, "Unknown user tier", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                finish()
                            } else {
                                Log.d("LoginActivityAdmin", "User data not found for email: $email")
                                Toast.makeText(this, "Failed to retrieve user data", Toast.LENGTH_SHORT).show()
                            }
                        })
                    } else {
                        Log.d("LoginActivityAdmin", "No authenticated user found")
                        Toast.makeText(this, "No authenticated user found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.d("LoginActivityAdmin", "Authentication failed")
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
