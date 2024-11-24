package com.example.ambassadorpass.view.admin

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.ambassadorpass.R
import com.example.ambassadorpass.ViewModel.AssignAdminViewModel
import com.example.ambassadorpass.ViewModel.AssignAdminViewModelFactory

class CreateAdminActivity : AppCompatActivity() {

    private lateinit var adminNameEditText: EditText
    private lateinit var adminEmailEditText: EditText
    private lateinit var createAdminButton: Button
    private lateinit var backButton: ImageButton

    // Initialize ViewModel using ViewModelFactory
    private val viewModel: AssignAdminViewModel by viewModels {
        AssignAdminViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_admin)

        // Initialize views
        adminNameEditText = findViewById(R.id.adminName)
        adminEmailEditText = findViewById(R.id.adminEmail)
        createAdminButton = findViewById(R.id.createAdminButton)
        backButton = findViewById(R.id.backButton)

        // Back button logic
        backButton.setOnClickListener {
            finish() // Closes this activity and navigates back to the previous screen
        }

        // Create admin button logic
        createAdminButton.setOnClickListener {
            val adminName = adminNameEditText.text.toString().trim()
            val adminEmail = adminEmailEditText.text.toString().trim()

            if (adminName.isNotEmpty() && adminEmail.isNotEmpty()) {
                // Use ViewModel to create admin account
                viewModel.createAdminAccount(adminEmail, adminName) { success, message ->
                    if (success) {
                        Toast.makeText(this, "Admin account created successfully", Toast.LENGTH_SHORT).show()
                        finish() // Close the activity and navigate back
                    } else {
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
