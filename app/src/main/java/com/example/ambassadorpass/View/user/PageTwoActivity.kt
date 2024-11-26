package com.example.ambassadorpass.view.user

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.example.ambassadorpass.R
import com.example.ambassadorpass.repository.PartyRegistrationRepository
import com.example.ambassadorpass.viewmodel.PageTwoViewModel
import com.example.ambassadorpass.viewmodel.PageTwoViewModelFactory

class PageTwoActivity : AppCompatActivity() {

    private val viewModel: PageTwoViewModel by viewModels {
        PageTwoViewModelFactory(PartyRegistrationRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_two)

        // Get the partyLink passed from the previous activity
        val partyLink = intent.getStringExtra("PARTY_LINK") ?: ""

        // Initialize the back button
        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener { finish() }

        // Initialize the input fields
        val nameEditText = findViewById<TextInputEditText>(R.id.nameEditText)
        val genderEditText = findViewById<TextInputEditText>(R.id.genderEditText)
        val ageEditText = findViewById<TextInputEditText>(R.id.ageEditText)
        val emailEditText = findViewById<TextInputEditText>(R.id.emailEditText)
        val identificationEditText = findViewById<TextInputEditText>(R.id.identificationEditText)
        val phoneEditText = findViewById<TextInputEditText>(R.id.phoneEditText)
        val submitButton: Button = findViewById(R.id.submitButton) // Initialize the submitButton here

        // Set the click listener for the submit button
        submitButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val gender = genderEditText.text.toString().trim()
            val age = ageEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val phone = phoneEditText.text.toString().trim()
            val identification = identificationEditText.text.toString().trim()

            if (name.isEmpty() || gender.isEmpty() || age.isEmpty() || email.isEmpty() || phone.isEmpty() || identification.isEmpty()) {
                Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.submitAttendee(name, gender, age, email, phone, identification, partyLink)
            }
        }

        // Observe ViewModel LiveData
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.isSubmitting.observe(this) { isSubmitting ->
            // Show loading indicator if necessary
            if (isSubmitting) {
                Toast.makeText(this, "Submitting...", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.submissionSuccess.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        viewModel.errorMessage.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
