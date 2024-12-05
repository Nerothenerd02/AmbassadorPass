package com.example.ambassadorpass.view.user

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.ambassadorpass.R
import com.example.ambassadorpass.repository.PartyRegistrationRepository
import com.example.ambassadorpass.view.MainActivity
import com.example.ambassadorpass.viewmodel.PageFiveViewModel
import com.example.ambassadorpass.viewmodel.PageFiveViewModelFactory
import kotlin.concurrent.thread

class PageFiveActivity : AppCompatActivity() {

    private val viewModel: PageFiveViewModel by viewModels {
        PageFiveViewModelFactory(PartyRegistrationRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_five)

        // Initialize UI components
        val backButton: ImageButton = findViewById(R.id.backButton)
        val confirmButton: Button = findViewById(R.id.confirmButton)
        val partyDetailsTextView: TextView = findViewById(R.id.partyDetailsTextView)
        val userDetailsTextView: TextView = findViewById(R.id.userDetailsTextView)

        // Fetch data from intent
        val partyLink = intent.getStringExtra("partyLink") ?: ""

        // Observe ViewModel LiveData
        observeViewModel(partyDetailsTextView, userDetailsTextView)

        // Fetch party details
        viewModel.fetchPartyDetails(partyLink)

        // Back button functionality
        backButton.setOnClickListener {
            finish()
        }

        // Confirm button functionality
        confirmButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun observeViewModel(
        partyDetailsTextView: TextView,
        userDetailsTextView: TextView
    ) {
        viewModel.partyDetails.observe(this) { details ->
            details?.let {
                val location = it["partyLocation"] as? String ?: "Location not available"
                val date = it["partyDate"] as? String ?: "Date not available"

                // Format and set the text
                partyDetailsTextView.text = "📍 Location: $location"
                userDetailsTextView.text = "🗓️ Date: $date"
            }
        }

        viewModel.errorMessage.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
